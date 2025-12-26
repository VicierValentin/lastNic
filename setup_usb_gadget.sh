#!/bin/bash
# USB HID Keyboard Gadget Setup Script for BeagleBone Black
# This script configures the BBB to act as a USB keyboard device

# Exit on error
set -e

# Create gadget directory
cd /sys/kernel/config/usb_gadget/
mkdir -p keyboard
cd keyboard

# USB IDs (change these if needed)
echo 0x413C > idVendor  # DELL Inc.
echo 0x2003 > idProduct # Multifunction Composite Gadget
echo 0x0100 > bcdDevice # v1.0.0
echo 0x0200 > bcdUSB    # USB 2.0

# Create strings directory for USB device description
mkdir -p strings/0x409
echo "fedcba9876543210" > strings/0x409/serialnumber
echo "DELL" > strings/0x409/manufacturer
echo "USB Keyboard" > strings/0x409/product

# Create configuration
mkdir -p configs/c.1/strings/0x409
echo "Config 1: USB Keyboard" > configs/c.1/strings/0x409/configuration
echo 250 > configs/c.1/MaxPower

# Create HID function
mkdir -p functions/hid.usb0
echo 1 > functions/hid.usb0/protocol    # Keyboard
echo 1 > functions/hid.usb0/subclass    # Boot Interface Subclass
echo 8 > functions/hid.usb0/report_length

# HID Report Descriptor for standard keyboard
echo -ne \\x05\\x01\\x09\\x06\\xa1\\x01\\x05\\x07\\x19\\xe0\\x29\\xe7\\x15\\x00\\x25\\x01\\x75\\x01\\x95\\x08\\x81\\x02\\x95\\x01\\x75\\x08\\x81\\x03\\x95\\x05\\x75\\x01\\x05\\x08\\x19\\x01\\x29\\x05\\x91\\x02\\x95\\x01\\x75\\x03\\x91\\x03\\x95\\x06\\x75\\x08\\x15\\x00\\x25\\x65\\x05\\x07\\x19\\x00\\x29\\x65\\x81\\x00\\xc0 > functions/hid.usb0/report_desc

# Link function to configuration
ln -s functions/hid.usb0 configs/c.1/

# Enable gadget
UDC_DEVICE=$(ls /sys/class/udc 2>/dev/null | head -n1)

if [ -z "$UDC_DEVICE" ]; then
    echo "Error: No USB Device Controller (UDC) found!"
    echo "This script requires hardware with USB gadget/OTG support (like BeagleBone Black)."
    echo "Available UDCs: $(ls /sys/class/udc 2>/dev/null || echo 'none')"
    exit 1
fi

echo "Using UDC: $UDC_DEVICE"
echo $UDC_DEVICE > UDC

# Set permissions on HID device
sleep 1
if [ -e /dev/hidg0 ]; then
    chmod 666 /dev/hidg0
else
    echo "Warning: /dev/hidg0 not found. The gadget may not be fully initialized."
    exit 1
fi

echo "USB HID Keyboard gadget configured successfully!"
echo "Device is available at /dev/hidg0"
