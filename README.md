# BeagleBone Black USB Keyboard Emulator - French AZERTY

A C++ application that turns your BeagleBone Black into a USB keyboard with French AZERTY layout support. This allows your BBB to act as a physical keyboard device when connected to any computer via USB.

## Features

- ✅ Full French AZERTY keyboard layout support
- ✅ USB HID (Human Interface Device) implementation
- ✅ Type text strings, files, or interactive input
- ✅ Configurable typing speed
- ✅ Support for special characters (é, è, à, ç, ù, etc.)
- ✅ Simple command-line interface

## Hardware Requirements

- BeagleBone Black (or compatible boards)
- USB cable (connected to the USB client port)
- Linux kernel with USB gadget support (ConfigFS)

## Software Requirements

- Linux with USB gadget support (kernel 3.19+)
- g++ compiler with C++11 support
- Root access for USB gadget configuration

## Installation

### 1. Build the application

```bash
make
```

### 2. Configure USB Gadget (must be run once after each boot)

```bash
sudo ./setup_usb_gadget.sh
```

This script:
- Loads the USB gadget modules
- Configures the BBB as a USB HID keyboard device
- Creates the `/dev/hidg0` interface
- Sets proper permissions

### 3. Optional: Install system-wide

```bash
sudo make install
```

## Usage

### Basic Text Typing

Type a simple text string:
```bash
./usb_keyboard -t "Bonjour le monde!"
```

### French Characters

The keyboard supports all French AZERTY characters:
```bash
./usb_keyboard -t "àéèùç ÀÉÈÙÇ êâôûî"
./usb_keyboard -t "Voilà! C'est génial."
```

### Type from a File

```bash
./usb_keyboard -f mytext.txt
```

### Interactive Mode

Type from standard input (useful with pipes):
```bash
./usb_keyboard -i
# Then type your text...

# Or use with pipe:
echo "Hello from BBB" | ./usb_keyboard -i
```

### Adjust Typing Speed

Set delay between keystrokes (in milliseconds):
```bash
./usb_keyboard -t "Slow typing..." -d 50
./usb_keyboard -t "Fast typing!" -d 5
```

### Command Line Options

```
Options:
  -t <text>     Type the specified text
  -f <file>     Type the contents of a file
  -d <ms>       Set delay between keystrokes (default: 10ms)
  -i            Interactive mode - type from stdin
  -h            Show help message
```

## AZERTY Layout

The French AZERTY keyboard layout differs from QWERTY:

### Letter Positions:
- A ↔ Q positions swapped
- W ↔ Z positions swapped
- M is on the semicolon key position

### Number Row (without Shift):
- 1: &
- 2: é
- 3: "
- 4: '
- 5: (
- 6: -
- 7: è
- 8: _
- 9: ç
- 0: à

### Number Row (with Shift):
- Shift+1: 1
- Shift+2: 2
- ...and so on

## Project Structure

```
.
├── main.cpp              # Main application entry point
├── usb_keyboard.h        # USB HID keyboard controller class
├── azerty_keymap.h       # French AZERTY layout mapping
├── setup_usb_gadget.sh   # USB gadget configuration script
├── Makefile              # Build system
└── README.md             # This file
```

## Technical Details

### USB HID Report Format

The application uses the standard 8-byte HID keyboard report:
```
Byte 0: Modifier keys (Ctrl, Shift, Alt, GUI)
Byte 1: Reserved (always 0)
Byte 2-7: Up to 6 simultaneous key presses
```

### Device Path

The USB HID device is located at `/dev/hidg0` after running the setup script.

### USB Gadget Configuration

The setup script uses Linux ConfigFS to configure the USB gadget:
- **Vendor ID**: 0x1d6b (Linux Foundation)
- **Product ID**: 0x0104 (Multifunction Composite Gadget)
- **Device Class**: HID Keyboard

## Troubleshooting

### "Cannot open USB HID device"

Make sure you ran the setup script with sudo:
```bash
sudo ./setup_usb_gadget.sh
```

### Device not recognized by host computer

1. Check the USB cable connection (use the USB client port on BBB)
2. Verify the gadget is configured: `ls /dev/hidg0`
3. Check if the UDC is enabled: `cat /sys/kernel/config/usb_gadget/keyboard/UDC`

### Wrong characters appear

This application is specifically designed for French AZERTY layout. Make sure:
- The host computer is expecting an AZERTY keyboard, OR
- You're aware of the character mapping differences

### Permission denied on /dev/hidg0

Run the setup script again, or manually:
```bash
sudo chmod 666 /dev/hidg0
```

## Run at Boot (Optional)

To automatically configure the USB gadget at boot, create a systemd service:

```bash
sudo nano /etc/systemd/system/usb-keyboard.service
```

Add:
```ini
[Unit]
Description=USB Keyboard Gadget Setup
After=local-fs.target

[Service]
Type=oneshot
ExecStart=/usr/local/bin/setup_usb_gadget.sh
RemainAfterExit=yes

[Install]
WantedBy=multi-user.target
```

Enable it:
```bash
sudo systemctl enable usb-keyboard.service
sudo systemctl start usb-keyboard.service
```

## Examples

### Script Automation

Create a script that types commands:
```bash
#!/bin/bash
./usb_keyboard -t "ls -la" -d 20
sleep 1
./usb_keyboard -t "\n"
```

### Remote Command Execution

Type commands on a connected computer:
```bash
./usb_keyboard -t "notepad\n"
sleep 2
./usb_keyboard -t "Bonjour depuis BeagleBone Black!\n"
```

### Type Code

```bash
./usb_keyboard -f script.py
```

## License

This project is provided as-is for educational and development purposes.

## Contributing

Feel free to submit issues and enhancement requests!

## Author

vvicier

## Acknowledgments

- BeagleBone Black community
- Linux USB Gadget framework documentation
- USB HID specification

## References

- [Linux USB Gadget ConfigFS](https://www.kernel.org/doc/Documentation/usb/gadget_configfs.txt)
- [USB HID Usage Tables](https://www.usb.org/hid)
- [BeagleBone Black Documentation](https://beagleboard.org/black)
# lastNic
