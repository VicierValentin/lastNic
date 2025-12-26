#!/bin/bash
# Example script demonstrating USB keyboard usage

echo "USB Keyboard Demo Script"
echo "========================"

# Make sure the device is configured
if [ ! -e /dev/hidg0 ]; then
    echo "USB gadget not configured. Run: sudo ./setup_usb_gadget.sh"
    exit 1
fi

# Simple text
echo "1. Typing simple French text..."
./usb_keyboard -t "Bonjour! Ceci est un test."
sleep 2

# Numbers and special characters
echo "2. Typing numbers and special characters..."
./usb_keyboard -t "1234567890 &é\"'(-è_çà"
sleep 2

# Multiple lines
echo "3. Typing multiple lines..."
./usb_keyboard -t "Première ligne\nDeuxième ligne\nTroisième ligne"
sleep 2

# French accents
echo "4. Testing French accents..."
./usb_keyboard -t "àéèùç ÀÉÈÙÇ êâôûî"
sleep 2

echo "Demo complete!"
