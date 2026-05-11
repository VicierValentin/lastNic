# lastnic — BBB USB HID Keyboard Emulator

Turns a BeagleBone Black into a USB keyboard. It reads a text file and types
its content on the PC it is connected to via the mini-USB port.

---

## 1. Build on Ubuntu (cross-compile)

```bash
# Install the ARM cross-compiler (once)
sudo apt install gcc-arm-linux-gnueabihf

# Build
./cross-compile.sh
# → binary: build-arm/lastnic
```

---

## 2. Deploy to the BBB

```bash
# Copy SSH key so no password is needed (once)
ssh-copy-id vvicier@10.0.0.221

# Build + deploy in one step
./cross-compile.sh deploy
# Copies lastnic and scripts/setup_gadget.sh to vvicier@10.0.0.221:~/
```

Or manually:
```bash
scp build-arm/lastnic scripts/setup_gadget.sh vvicier@10.0.0.221:~/
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
| `-v` | Verbose output | off |
| `-h` | Help | — |
