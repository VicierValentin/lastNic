#!/usr/bin/env python3
"""
bt_server.py — Bluetooth RFCOMM daemon for the BeagleBone Black.

Listens on RFCOMM channel RFCOMM_CHANNEL and dispatches commands to lastnic.

Supported commands (newline-terminated):
  list            — list available macro files in MACROS_DIR
  status          — report whether lastnic is currently running
  run <name>      — launch lastnic with ~/macros/<name>
  stop            — terminate the running lastnic process
  <anything else> — ERR unknown command

Responses:
  OK ...          — success
  ERR ...         — failure / bad input
  <lines>\nEND   — multi-line reply (used by 'list')
  running <name>  — status reply (busy)
  idle            — status reply (free)

Architecture note:
  All per-connection logic lives in handle_client().
  To support multiple simultaneous connections in the future, wrap the
  handle_client() call in the accept loop with:
      threading.Thread(target=handle_client, args=(conn, addr), daemon=True).start()
  No other changes are required.
"""

import os
import re
import socket
import subprocess
import sys
import time
import logging

# ---------------------------------------------------------------------------
# Configuration — adjust to match deployment paths on the BBB
# ---------------------------------------------------------------------------
RFCOMM_CHANNEL: int  = 1
MACROS_DIR: str      = os.path.expanduser("~/macros")
LASTNIC_BIN: str     = os.path.expanduser("~/lastnic")

# Keyboard layout of the target PC: "us" or "fr"
# Can be changed at runtime via the 'layout <name>' command.
LASTNIC_LAYOUT: str  = "us"

_VALID_LAYOUTS: set  = {"us", "fr"}

# Delay (seconds) before starting lastnic after a 'run' command.
# Gives the target window time to gain focus so no keystrokes are lost.
STARTUP_DELAY_S: float = 0.0

# Allowed macro filename characters (no path separators, no spaces)
_VALID_NAME: re.Pattern = re.compile(r'^[A-Za-z0-9_.\-]+$')

# ---------------------------------------------------------------------------
# Logging
# ---------------------------------------------------------------------------
logging.basicConfig(
    level=logging.INFO,
    format="[bt_server] %(levelname)s %(message)s",
    stream=sys.stdout,
)
log = logging.getLogger("bt_server")

# ---------------------------------------------------------------------------
# Global subprocess handle (single-client architecture)
# ---------------------------------------------------------------------------
_running_proc: subprocess.Popen | None = None
_running_name: str = ""


def _send(conn: socket.socket, msg: str) -> None:
    """Send a newline-terminated UTF-8 message to the client."""
    conn.sendall((msg + "\n").encode())


def _kill_current() -> None:
    """Terminate the currently running lastnic process if any."""
    global _running_proc, _running_name
    if _running_proc is not None and _running_proc.poll() is None:
        _running_proc.terminate()
        try:
            _running_proc.wait(timeout=3)
        except subprocess.TimeoutExpired:
            _running_proc.kill()
    _running_proc = None
    _running_name = ""


# ---------------------------------------------------------------------------
# Command handlers
# ---------------------------------------------------------------------------

def cmd_list(conn: socket.socket) -> None:
    try:
        names = sorted(
            f for f in os.listdir(MACROS_DIR)
            if os.path.isfile(os.path.join(MACROS_DIR, f))
        )
    except OSError as exc:
        _send(conn, f"ERR cannot read macros dir: {exc}")
        return
    for name in names:
        _send(conn, name)
    _send(conn, "END")


def cmd_status(conn: socket.socket) -> None:
    global _running_proc, _running_name
    if _running_proc is not None and _running_proc.poll() is None:
        _send(conn, f"running {_running_name}")
    else:
        _running_proc = None
        _running_name = ""
        _send(conn, "idle")


def cmd_stop(conn: socket.socket) -> None:
    if _running_proc is None or _running_proc.poll() is not None:
        _send(conn, "OK nothing running")
        return
    _kill_current()
    _send(conn, "OK stopped")


def cmd_layout(conn: socket.socket, name: str) -> None:
    global LASTNIC_LAYOUT
    if name not in _VALID_LAYOUTS:
        _send(conn, f"ERR unknown layout '{name}' (supported: {', '.join(sorted(_VALID_LAYOUTS))})")
        return
    LASTNIC_LAYOUT = name
    _send(conn, f"OK layout set to {name}")
    log.info("layout changed to %s", name)


def cmd_run(conn: socket.socket, name: str) -> None:
    global _running_proc, _running_name

    if not _VALID_NAME.match(name):
        _send(conn, "ERR invalid filename")
        return

    macro_path = os.path.join(MACROS_DIR, name)
    if not os.path.isfile(macro_path):
        _send(conn, f"ERR file not found: {name}")
        return

    _kill_current()

    if STARTUP_DELAY_S > 0:
        _send(conn, f"OK starting {name} in {STARTUP_DELAY_S:.1f}s…")
        time.sleep(STARTUP_DELAY_S)

    try:
        log_file = open(os.path.expanduser("~/lastnic.log"), "a")
        _running_proc = subprocess.Popen(
            [LASTNIC_BIN, "-l", LASTNIC_LAYOUT, macro_path],
            stdout=subprocess.DEVNULL,
            stderr=subprocess.DEVNULL,
        )
        _running_name = name
        _send(conn, f"OK running {name}")
        log.info("started lastnic pid=%d macro=%s", _running_proc.pid, name)
    except OSError as exc:
        _send(conn, f"ERR failed to start lastnic: {exc}")


# ---------------------------------------------------------------------------
# Per-connection handler (isolated for future threading)
# ---------------------------------------------------------------------------

def handle_client(conn: socket.socket, addr: tuple) -> None:
    """
    Handle all commands from a single connected client.

    This function is intentionally self-contained so it can be run in a
    thread (threading.Thread(target=handle_client, args=(conn, addr))) when
    multi-client support is needed.
    """
    log.info("client connected: %s", addr)
    buf = ""
    try:
        while True:
            chunk = conn.recv(1024)
            if not chunk:
                break
            buf += chunk.decode(errors="replace")
            while "\n" in buf:
                line, buf = buf.split("\n", 1)
                line = line.strip()
                if not line:
                    continue
                log.info("rx: %r", line)
                parts = line.split(None, 1)
                verb = parts[0].lower()
                arg  = parts[1] if len(parts) > 1 else ""

                if verb == "list":
                    cmd_list(conn)
                elif verb == "status":
                    cmd_status(conn)
                elif verb == "stop":
                    cmd_stop(conn)
                elif verb == "layout":
                    if not arg:
                        _send(conn, f"OK current layout: {LASTNIC_LAYOUT}")
                    else:
                        cmd_layout(conn, arg.lower())
                elif verb == "run":
                    if not arg:
                        _send(conn, "ERR usage: run <name>")
                    else:
                        cmd_run(conn, arg)
                else:
                    _send(conn, "ERR unknown command")
    except OSError as exc:
        log.warning("connection error: %s", exc)
    finally:
        conn.close()
        log.info("client disconnected: %s", addr)


# ---------------------------------------------------------------------------
# Main — RFCOMM server loop
# ---------------------------------------------------------------------------

def main() -> None:
    os.makedirs(MACROS_DIR, exist_ok=True)

    server = socket.socket(socket.AF_BLUETOOTH, socket.SOCK_STREAM, socket.BTPROTO_RFCOMM)
    server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    # Bind to all local BT adapters on the configured channel
    # RFCOMM requires a valid BD address; "00:00:00:00:00:00" means any adapter
    server.bind(("00:00:00:00:00:00", RFCOMM_CHANNEL))
    server.listen(1)

    log.info("RFCOMM server listening on channel %d", RFCOMM_CHANNEL)
    log.info("macros dir : %s", MACROS_DIR)
    log.info("lastnic bin: %s", LASTNIC_BIN)

    try:
        while True:
            log.info("waiting for connection…")
            conn, addr = server.accept()
            # Single-client: handle inline.
            # For multi-client: replace the line below with:
            #   threading.Thread(target=handle_client, args=(conn, addr), daemon=True).start()
            handle_client(conn, addr)
    except KeyboardInterrupt:
        log.info("interrupted, shutting down")
    finally:
        _kill_current()
        server.close()


if __name__ == "__main__":
    main()
