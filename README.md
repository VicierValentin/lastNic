# lastnic — BBB USB HID Keyboard Emulator

Turns a BeagleBone Black into a USB keyboard. It reads a text file and types
its content on the PC it is connected to via the mini-USB port.

---

## 1. Build on Ubuntu (cross-compile)

```bash
# Install the ARM cross-compiler (once)
sudo apt install gcc-arm-linux-gnueabihf

# Build only
./cross-compile.sh build       # → build-arm/lastnic

# Build + deploy to BBB
./cross-compile.sh deploy

# Apply BBB system config only (BlueZ, compat mode, service install)
./cross-compile.sh configure

# Remove build artefacts
./cross-compile.sh clean
```

Environment overrides:
```bash
BBB_HOST=root@192.168.1.10 ./cross-compile.sh deploy
```

---

## 2. Deploy to the BBB

```bash
# Copy SSH key so no password is needed (once)
ssh-copy-id vvicier@10.0.0.221

# Build + deploy in one step
./cross-compile.sh deploy
# Copies: lastnic, setup_gadget.sh, bt_server.py, macros/, lastnic-bt.service
```

---

## 3. First-time setup on the BBB

SSH into the BBB and run the gadget setup script **once per boot**
(or install the systemd service to do it automatically):

```bash
# One-shot setup
sudo ~/scripts/setup_gadget.sh
# → creates /dev/hidg0

# Or install as a systemd service (persistent across reboots)
sudo cp ~/scripts/setup_gadget.sh /usr/local/bin/
sudo cp pdeServices/lastnic-gadget.service /etc/systemd/system/
sudo systemctl enable --now lastnic-gadget.service
```

> **Note:** connect the BBB to the PC with a **data-capable** mini-USB cable
> (not a charge-only cable) before running the setup script.

---

## 4. Run

```bash
~/lastnic myfile.txt
~/lastnic -d 20 -v myfile.txt    # 20 ms between keystrokes, verbose
```

### Special keys

Use `<TAG>` syntax in the text file:

| Tag | Key |
|-----|-----|
| `<ENTER>` | Enter |
| `<TAB>` | Tab |
| `<ESC>` | Escape |
| `<BACKSPACE>` | Backspace |
| `<DELETE>` | Delete |
| `<UP>` `<DOWN>` `<LEFT>` `<RIGHT>` | Arrow keys |
| `<HOME>` `<END>` `<PGUP>` `<PGDN>` | Navigation |
| `<F1>` … `<F12>` | Function keys |
| `<CTRL+C>` | Ctrl+C |
| `<CTRL+ALT+DEL>` | Ctrl+Alt+Del |
| `<WIN+R>` | Win+R |
| `<ALT+TAB>` | Alt+Tab |
| `<SHIFT+F1>` | Shift+F1 |

Use `<<` to type a literal `<` character.

### Example file

```
<WIN+R>notepad<ENTER>
Hello, World!<ENTER>
<CTRL+A><DELETE>
Done.<ENTER>
```

---

## 5. Options

| Flag | Description | Default |
|------|-------------|---------|
| `-d <ms>` | Delay between keystrokes (ms) | `10` |
| `-D <path>` | HID device path | `/dev/hidg0` |
| `-l <layout>` | Keyboard layout: `us` or `fr` (AZERTY) | `us` |
| `-v` | Verbose output | off |
| `-h` | Help | — |

---

## 6. Bluetooth remote control

The BBB can receive macro commands wirelessly from a PC over Bluetooth RFCOMM.
A USB BT dongle (USB-BT 500) is plugged into the BBB's USB-A port.

### First-time pairing (once)

```bash
# On BBB
bluetoothctl agent NoInputNoOutput
bluetoothctl default-agent
bluetoothctl discoverable on
bluetoothctl pairable on

# On PC
bluetoothctl
  pair BC:FC:E7:26:35:F3
  trust BC:FC:E7:26:35:F3

# On BBB — turn off discovery when done
bluetoothctl discoverable off
```

### BBB daemon — `bt_server.py`

Installed and auto-started by the `lastnic-bt` systemd service (deployed by `./cross-compile.sh deploy`).

```bash
# Manual start (for testing)
python3 ~/bt_server.py

# Service management
sudo systemctl status  lastnic-bt
sudo systemctl restart lastnic-bt
sudo journalctl -u lastnic-bt -f
```

### PC client — `scripts/bt_client.py`

The BBB MAC is baked in (`BC:FC:E7:26:35:F3`). Override with `BBB_MAC` env var.

```bash
# One-shot commands
python3 scripts/bt_client.py list           # list available macros
python3 scripts/bt_client.py run macrong    # run a macro
python3 scripts/bt_client.py run macrong2
python3 scripts/bt_client.py status         # is a macro running?
python3 scripts/bt_client.py stop           # stop running macro
python3 scripts/bt_client.py layout fr      # switch layout (us/fr)

# Interactive REPL (no args)
python3 scripts/bt_client.py

# Override MAC
BBB_MAC=XX:XX:XX:XX:XX:XX python3 scripts/bt_client.py list
```

### Macro file format

Macros live in `~/macros/` on the BBB. Same tag syntax as the local `lastnic` command.
The active keyboard layout (`us` / `fr`) is set in `bt_server.py` (`LASTNIC_LAYOUT`) and
can be changed at runtime with `layout <name>`.
