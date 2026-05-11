#!/bin/bash
# =============================================================================
# setup_gadget.sh — Configure BeagleBone Black as a USB HID keyboard gadget
#
# Must be run as root before launching the lastnic application.
# Creates /dev/hidg0 which is used to send HID keyboard reports to the host PC.
#
# Usage: sudo ./setup_gadget.sh [teardown]
# =============================================================================

set -e

GADGET_NAME="lastnic"
GADGET_DIR="/sys/kernel/config/usb_gadget/${GADGET_NAME}"

# ---------------------------------------------------------------------------
# Preserve eth0 connectivity — removing g_multi brings down usb0 which can
# temporarily disrupt the kernel routing table even on unrelated interfaces.
# Save and restore the eth0 default route around any USB manipulation.
# ---------------------------------------------------------------------------
ETH_IFACE="eth0"
ETH_GW=""

save_eth_route() {
    ETH_GW=$(ip route show dev "${ETH_IFACE}" 2>/dev/null | awk '/default/ {print $3; exit}')
}

restore_eth_route() {
    # Ensure eth0 is still up
    ip link set "${ETH_IFACE}" up 2>/dev/null || true
    if [ -n "${ETH_GW}" ]; then
        ip route replace default via "${ETH_GW}" dev "${ETH_IFACE}" 2>/dev/null || true
    fi
    # Bring down usb0 cleanly so it doesn't interfere with routing
    if ip link show usb0 &>/dev/null; then
        ip link set usb0 down 2>/dev/null || true
        ip addr flush dev usb0 2>/dev/null || true
    fi
}

# ---------------------------------------------------------------------------
# Neutralize the BBB default gadget launcher so it can't override us.
# bb-start-acm-ncm-rndis-old-gadget is called by a udev rule when the MUSB
# hardware is detected. We replace it with a no-op and restore on teardown.
# ---------------------------------------------------------------------------
BB_GADGET_SCRIPT="/usr/bin/bb-start-acm-ncm-rndis-old-gadget"
BB_GADGET_BACKUP="/usr/bin/bb-start-acm-ncm-rndis-old-gadget.lastnic-bak"

neutralize_bb_gadget() {
    if [ -f "${BB_GADGET_SCRIPT}" ] && [ ! -f "${BB_GADGET_BACKUP}" ]; then
        echo "[lastnic] Neutralizing ${BB_GADGET_SCRIPT}"
        cp "${BB_GADGET_SCRIPT}" "${BB_GADGET_BACKUP}"
    fi
    # Replace with a no-op that just logs and exits
    cat > "${BB_GADGET_SCRIPT}" << 'NOOP'
#!/bin/bash
# Disabled by lastnic — HID keyboard gadget is active
echo "[bb-gadget] Skipped: lastnic HID gadget is active" >&2
exit 0
NOOP
    chmod +x "${BB_GADGET_SCRIPT}"
}

restore_bb_gadget() {
    if [ -f "${BB_GADGET_BACKUP}" ]; then
        echo "[lastnic] Restoring ${BB_GADGET_SCRIPT}"
        mv "${BB_GADGET_BACKUP}" "${BB_GADGET_SCRIPT}"
    fi
}

# ---------------------------------------------------------------------------
# Teardown / cleanup
# ---------------------------------------------------------------------------
teardown() {
    echo "[lastnic] Tearing down HID gadget..."
    if [ -f "${GADGET_DIR}/UDC" ]; then
        echo "" > "${GADGET_DIR}/UDC" 2>/dev/null || true
    fi
    if [ -L "${GADGET_DIR}/configs/c.1/hid.usb0" ]; then
        rm -f "${GADGET_DIR}/configs/c.1/hid.usb0"
    fi
    for dir in \
        "${GADGET_DIR}/configs/c.1/strings/0x409" \
        "${GADGET_DIR}/configs/c.1" \
        "${GADGET_DIR}/functions/hid.usb0" \
        "${GADGET_DIR}/strings/0x409" \
        "${GADGET_DIR}"; do
        [ -d "$dir" ] && rmdir "$dir" 2>/dev/null || true
    done
    restore_bb_gadget
    echo "[lastnic] Gadget removed."
}

if [ "$1" = "teardown" ]; then
    teardown
    exit 0
fi

# ---------------------------------------------------------------------------
# Sanity checks
# ---------------------------------------------------------------------------
if [ "$(id -u)" -ne 0 ]; then
    echo "Error: must be run as root." >&2
    exit 1
fi

# Save eth0 route BEFORE any USB manipulation
save_eth_route

# Neutralize the BBB gadget launcher FIRST so udev can't reload g_multi
neutralize_bb_gadget

# Remove legacy gadget modules that conflict with configfs gadget.
# modprobe -r handles dependencies cleanly; fallback to rmmod --force.
for mod in g_multi g_ether g_serial g_mass_storage g_cdc g_acm; do
    if lsmod | grep -q "^${mod} "; then
        echo "[lastnic] Removing conflicting module: ${mod}"
        modprobe -r "${mod}" 2>/dev/null || rmmod --force "${mod}" 2>/dev/null || true
        sleep 0.5
    fi
done

# Restore eth0 route now that usb0/g_multi is gone
restore_eth_route

# Blacklist g_multi so the system doesn't reload it during our session
if [ -d /etc/modprobe.d ]; then
    echo "blacklist g_multi" > /etc/modprobe.d/lastnic-no-gmulti.conf 2>/dev/null || true
fi

# Load required modules
modprobe libcomposite 2>/dev/null || true

# Release UDC from any other configfs gadget that may be using it
# (BBB Debian may auto-configure a g_cdc / usb0 network gadget at boot)
release_udc() {
    local udc="$1"
    for g in /sys/kernel/config/usb_gadget/*/; do
        [ -d "$g" ] || continue
        local gname
        gname=$(basename "$g")
        [ "${gname}" = "${GADGET_NAME}" ] && continue  # skip our own gadget
        local bound
        bound=$(cat "${g}UDC" 2>/dev/null || true)
        if [ "${bound}" = "${udc}" ]; then
            echo "[lastnic] Releasing UDC from foreign gadget: ${gname}"
            echo "" > "${g}UDC" 2>/dev/null || true
        fi
    done
}

# Wait until the UDC reports 'not attached' (max 5 s)
wait_udc_free() {
    local udc="$1"
    local state_file="/sys/class/udc/${udc}/state"
    local i=0
    while [ $i -lt 50 ]; do
        local state
        state=$(cat "${state_file}" 2>/dev/null || echo "not attached")
        [ "${state}" = "not attached" ] && return 0
        sleep 0.1
        i=$((i + 1))
    done
    echo "Warning: UDC '${udc}' state is still '$(cat ${state_file} 2>/dev/null)' after 5 s" >&2
    return 1
}

# Mount configfs if not already mounted
if ! mountpoint -q /sys/kernel/config; then
    mount -t configfs none /sys/kernel/config
fi

# Detect the UDC name (BBB = musb-hdrc.0)
UDC_NAME=$(ls /sys/class/udc/ 2>/dev/null | head -n1)
if [ -z "${UDC_NAME}" ]; then
    echo "Error: No UDC found in /sys/class/udc/. Is the MUSB driver loaded?" >&2
    exit 1
fi
echo "[lastnic] UDC: ${UDC_NAME}"

# If gadget already exists and is bound AND the device node is present, skip setup
if [ -d "${GADGET_DIR}" ] && [ -s "${GADGET_DIR}/UDC" ] && [ -e /dev/hidg0 ]; then
    echo "[lastnic] Gadget already configured and /dev/hidg0 is present."
    chmod 666 /dev/hidg0
    exit 0
fi

# Gadget appears bound but /dev/hidg0 is missing — stale configfs state.
# Tear down fully and re-create.
if [ -d "${GADGET_DIR}" ] && [ -s "${GADGET_DIR}/UDC" ] && [ ! -e /dev/hidg0 ]; then
    echo "[lastnic] WARNING: gadget bound but /dev/hidg0 missing — stale state, rebuilding..."
    teardown
fi

# Tear down any remaining stale partial config
if [ -d "${GADGET_DIR}" ]; then
    echo "[lastnic] Stale gadget directory found, cleaning up..."
    teardown
fi

# Free the UDC from any foreign gadget and wait for it to be available
release_udc "${UDC_NAME}"
wait_udc_free "${UDC_NAME}" || true

# ---------------------------------------------------------------------------
# Create gadget
# ---------------------------------------------------------------------------
echo "[lastnic] Creating HID keyboard gadget..."

mkdir -p "${GADGET_DIR}"
cd "${GADGET_DIR}"

# USB device descriptors
echo 0x0200 > bcdUSB         # USB 2.0
echo 0x0000 > bDeviceClass   # defined at interface level
echo 0x0000 > bDeviceProtocol
echo 0x0000 > bDeviceSubClass
echo 0x0008 > bMaxPacketSize0
echo 0x413c > idVendor       # Dell Inc.
echo 0x2113 > idProduct      # Dell KB216 Wired Keyboard
echo 0x0100 > bcdDevice      # v1.0.0

# String descriptors (English)
mkdir -p strings/0x409
echo "KB216AA"             > strings/0x409/serialnumber
echo "Dell Inc."           > strings/0x409/manufacturer
echo "Dell KB216 Keyboard" > strings/0x409/product

# HID function
mkdir -p functions/hid.usb0
echo 1 > functions/hid.usb0/protocol       # 1 = Keyboard
echo 1 > functions/hid.usb0/subclass       # 1 = Boot Interface Subclass
echo 8 > functions/hid.usb0/report_length  # 8 bytes per report

# Standard keyboard HID report descriptor
# Key slots use Input(Data,Variable,Absolute) = 0x81 0x02 so that
# Linux usbhid creates a proper evdev keyboard input device.
# Written via python3 to guarantee correct binary output.
python3 -c "
import sys
desc = bytes([
    0x05, 0x01,  # Usage Page: Generic Desktop
    0x09, 0x06,  # Usage: Keyboard
    0xa1, 0x01,  # Collection: Application
    0x05, 0x07,  #   Usage Page: Keyboard/Keypad
    0x19, 0xe0,  #   Usage Minimum: Left Control
    0x29, 0xe7,  #   Usage Maximum: Right GUI
    0x15, 0x00,  #   Logical Minimum: 0
    0x25, 0x01,  #   Logical Maximum: 1
    0x75, 0x01,  #   Report Size: 1
    0x95, 0x08,  #   Report Count: 8
    0x81, 0x02,  #   Input: Data, Variable, Absolute  (modifier byte)
    0x95, 0x01,  #   Report Count: 1
    0x75, 0x08,  #   Report Size: 8
    0x81, 0x03,  #   Input: Constant                  (reserved byte)
    0x95, 0x05,  #   Report Count: 5
    0x75, 0x01,  #   Report Size: 1
    0x05, 0x08,  #   Usage Page: LEDs
    0x19, 0x01,  #   Usage Minimum: Num Lock
    0x29, 0x05,  #   Usage Maximum: Kana
    0x91, 0x02,  #   Output: Data, Variable, Absolute (LED byte)
    0x95, 0x01,  #   Report Count: 1
    0x75, 0x03,  #   Report Size: 3
    0x91, 0x03,  #   Output: Constant                 (LED padding)
    0x95, 0x06,  #   Report Count: 6
    0x75, 0x08,  #   Report Size: 8
    0x15, 0x00,  #   Logical Minimum: 0
    0x25, 0x65,  #   Logical Maximum: 101
    0x05, 0x07,  #   Usage Page: Keyboard/Keypad
    0x19, 0x00,  #   Usage Minimum: Reserved
    0x29, 0x65,  #   Usage Maximum: Keyboard Application
    0x81, 0x00,  #   Input: Data, Array, Absolute  (6 key slots — each byte is a keycode)
    0xc0,        # End Collection
])
sys.stdout.buffer.write(desc)
" > functions/hid.usb0/report_desc

# Configuration
mkdir -p configs/c.1/strings/0x409
echo 0x80  > configs/c.1/bmAttributes   # Bus powered
echo 100   > configs/c.1/MaxPower       # 100 mA
echo "HID Keyboard" > configs/c.1/strings/0x409/configuration

# Link function into configuration
ln -s "${GADGET_DIR}/functions/hid.usb0" configs/c.1/

# Bind to UDC — this activates the gadget and creates /dev/hidg0
# Retry a few times in case the UDC needs a moment to fully release
BIND_OK=0
for attempt in 1 2 3 4 5; do
    if echo "${UDC_NAME}" > UDC 2>/dev/null; then
        BIND_OK=1
        break
    fi
    echo "[lastnic] Bind attempt ${attempt} failed, retrying in 1 s..."
    sleep 1
done

if [ "${BIND_OK}" -eq 0 ]; then
    echo "Error: could not bind gadget to UDC '${UDC_NAME}' after 5 attempts." >&2
    echo "       Check 'dmesg | tail -20' and 'cat /sys/class/udc/${UDC_NAME}/state'" >&2
    exit 1
fi

# Give the PC time to enumerate the new HID device and load its driver.
# Do NOT release the UDC here — that would let g_multi re-grab it.
echo "[lastnic] Bound to UDC. Waiting for PC to enumerate HID device (~3 s)..."
sleep 3

if [ -e /dev/hidg0 ]; then
    chmod 666 /dev/hidg0
    echo "[lastnic] Gadget ready. /dev/hidg0 is available."
else
    echo "Warning: /dev/hidg0 not found after bind. Check dmesg for errors." >&2
    exit 1
fi
