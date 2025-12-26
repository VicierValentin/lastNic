#!/bin/bash
# List available input devices for USB keyboard forwarding

echo "Available input devices:"
echo "========================"
echo

for device in /dev/input/event*; do
    if [ -e "$device" ]; then
        name=$(cat "/sys/class/input/$(basename $device)/device/name" 2>/dev/null)
        if [ -n "$name" ]; then
            echo "Device: $device"
            echo "  Name: $name"
            echo
        fi
    fi
done

echo "========================"
echo "To forward a USB keyboard, run:"
echo "  sudo ./usb_keyboard -u /dev/input/eventX"
echo
echo "Where X is the event number of your keyboard device"
echo "Note: You may need sudo to access input devices"
