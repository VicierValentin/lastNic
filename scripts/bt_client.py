#!/usr/bin/env python3
"""
bt_client.py — Bluetooth RFCOMM client for the lastnic daemon on the BBB.

Usage:
  # One-shot mode (run a single command and exit):
  python3 bt_client.py list
  python3 bt_client.py run macrong
  python3 bt_client.py stop
  python3 bt_client.py status

  # Interactive REPL mode (no args):
  python3 bt_client.py

  # Override the MAC for a different device:
  BBB_MAC=AA:BB:CC:DD:EE:FF python3 bt_client.py list

RFCOMM_CHANNEL must match the value configured in bt_server.py (default: 1).
"""

import os
import socket
import sys

RFCOMM_CHANNEL: int = 1

# Default BBB Bluetooth MAC — override with BBB_MAC env var if needed
BBB_MAC: str = os.environ.get("BBB_MAC", "BC:FC:E7:26:35:F3")


def recv_reply(sock: socket.socket) -> None:
    """
    Read and print the server reply.
    Multi-line replies (e.g. 'list') are terminated by a line containing 'END'.
    Single-line replies (OK/ERR/running/idle) are printed and we return.
    """
    buf = ""
    while True:
        chunk = sock.recv(1024)
        if not chunk:
            break
        buf += chunk.decode(errors="replace")
        while "\n" in buf:
            line, buf = buf.split("\n", 1)
            line = line.rstrip()
            if line == "END":
                return
            print(line)
            # Single-line replies start with OK/ERR or are 'idle'/'running …'
            if line.startswith(("OK", "ERR", "idle", "running")):
                return


def send_command(sock: socket.socket, command: str) -> None:
    sock.sendall((command.strip() + "\n").encode())
    recv_reply(sock)


def connect(mac: str) -> socket.socket:
    sock = socket.socket(socket.AF_BLUETOOTH, socket.SOCK_STREAM, socket.BTPROTO_RFCOMM)
    sock.settimeout(10)
    sock.connect((mac, RFCOMM_CHANNEL))
    sock.settimeout(None)
    return sock


def run_oneshot(mac: str, command: str) -> None:
    try:
        sock = connect(mac)
    except OSError as exc:
        print(f"ERR connection failed: {exc}", file=sys.stderr)
        sys.exit(1)
    try:
        send_command(sock, command)
    finally:
        sock.close()


def run_interactive(mac: str) -> None:
    try:
        sock = connect(mac)
    except OSError as exc:
        print(f"ERR connection failed: {exc}", file=sys.stderr)
        sys.exit(1)
    print(f"Connected to {mac} on channel {RFCOMM_CHANNEL}. Type 'quit' to exit.")
    try:
        while True:
            try:
                line = input("> ").strip()
            except (EOFError, KeyboardInterrupt):
                print()
                break
            if not line:
                continue
            if line.lower() in ("quit", "exit"):
                break
            send_command(sock, line)
    finally:
        sock.close()


def main() -> None:
    mac = BBB_MAC

    if len(sys.argv) == 1:
        run_interactive(mac)
    else:
        command = " ".join(sys.argv[1:])
        run_oneshot(mac, command)


if __name__ == "__main__":
    main()
