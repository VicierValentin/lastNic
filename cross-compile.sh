#!/bin/bash
# =============================================================================
# cross-compile.sh — Build lastnic for BeagleBone Black on Ubuntu
#
# Prerequisites (run once):
#   sudo apt install gcc-arm-linux-gnueabihf
#
# Usage:
#   ./cross-compile.sh            # build
#   ./cross-compile.sh clean      # remove build artefacts
#   ./cross-compile.sh deploy     # build + scp to BBB (set BBB_HOST below)
# =============================================================================

set -e

# ---------------------------------------------------------------------------
# Configuration — adjust as needed
# ---------------------------------------------------------------------------
CROSS_CC="arm-linux-gnueabihf-gcc"
CROSS_STRIP="arm-linux-gnueabihf-strip"

BUILD_DIR="build-arm"
BINARY="lastnic"

# SSH target for 'deploy' sub-command (user@host or user@ip)
BBB_HOST="${BBB_HOST:-vvicier@10.0.0.221}"
BBB_DEST_DIR="${BBB_DEST_DIR:-/home/vvicier}"

# ARM Cortex-A8 flags for BeagleBone Black (AM335x)
ARCH_FLAGS="-march=armv7-a -mfpu=vfpv3-d16 -mfloat-abi=hard"

# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------
check_toolchain() {
    if ! command -v "${CROSS_CC}" &>/dev/null; then
        echo "Error: cross-compiler '${CROSS_CC}' not found."
        echo "Install it with:"
        echo "  sudo apt install gcc-arm-linux-gnueabihf"
        exit 1
    fi
}

do_build() {
    check_toolchain
    mkdir -p "${BUILD_DIR}"

    echo "[cross-compile] CC=${CROSS_CC}"
    echo "[cross-compile] ARCH_FLAGS=${ARCH_FLAGS}"
    echo "[cross-compile] Output: ${BUILD_DIR}/${BINARY}"

    "${CROSS_CC}" \
        -std=c11 \
        -Wall -Wextra -Wpedantic \
        ${ARCH_FLAGS} \
        -O2 \
        -Iinclude \
        -o "${BUILD_DIR}/${BINARY}" \
        src/main.c

    echo "[cross-compile] Stripping binary..."
    "${CROSS_STRIP}" "${BUILD_DIR}/${BINARY}"

    echo "[cross-compile] Done."
    file "${BUILD_DIR}/${BINARY}"
}

do_clean() {
    echo "[cross-compile] Cleaning ${BUILD_DIR}/"
    rm -rf "${BUILD_DIR}"
}

do_configure() {
    echo "[cross-compile] Configuring BBB system at ${BBB_HOST}…"

    # 1. BlueZ: AutoEnable (power on adapter at boot) + AlwaysPairable
    ssh -t "${BBB_HOST}" "
        sudo sed -i 's/^#*[[:space:]]*AutoEnable[[:space:]]*=.*/AutoEnable = true/' /etc/bluetooth/main.conf
        sudo sed -i 's/^#*[[:space:]]*AlwaysPairable[[:space:]]*=.*/AlwaysPairable = true/' /etc/bluetooth/main.conf
        grep -q 'AlwaysPairable' /etc/bluetooth/main.conf || \
            sudo sed -i '/^\[Policy\]/a AlwaysPairable = true' /etc/bluetooth/main.conf
    "

    # 2. bluetoothd compat mode (allows raw AF_BLUETOOTH RFCOMM sockets)
    ssh -t "${BBB_HOST}" "
        BLUETOOTHD=\$(grep ExecStart /lib/systemd/system/bluetooth.service | grep -o '/[^ ]*bluetoothd')
        sudo mkdir -p /etc/systemd/system/bluetooth.service.d
        printf '[Service]\nExecStart=\nExecStart=%s --compat\n' \"\$BLUETOOTHD\" | \
            sudo tee /etc/systemd/system/bluetooth.service.d/compat.conf > /dev/null
    "

    # 3. Install and enable the lastnic-bt systemd service
    ssh -t "${BBB_HOST}" "
        sudo cp '${BBB_DEST_DIR}/lastnic-bt.service' /etc/systemd/system/lastnic-bt.service
        sudo systemctl daemon-reload
        sudo systemctl enable lastnic-bt
        sudo systemctl restart bluetooth
        sudo systemctl restart lastnic-bt
    "

    echo "[cross-compile] BBB configuration done."
}

do_deploy() {
    do_build

    echo "[cross-compile] Deploying to ${BBB_HOST}:${BBB_DEST_DIR}/"

    # Create remote directories
    ssh "${BBB_HOST}" "mkdir -p '${BBB_DEST_DIR}/macros'"

    # --- lastnic binary + USB gadget script ---
    scp "${BUILD_DIR}/${BINARY}"          "${BBB_HOST}:${BBB_DEST_DIR}/${BINARY}"
    scp "scripts/setup_gadget.sh"         "${BBB_HOST}:${BBB_DEST_DIR}/setup_gadget.sh"
    ssh "${BBB_HOST}" "chmod +x '${BBB_DEST_DIR}/${BINARY}' '${BBB_DEST_DIR}/setup_gadget.sh'"

    # --- Bluetooth daemon ---
    scp "src/bt_server.py"                "${BBB_HOST}:${BBB_DEST_DIR}/bt_server.py"
    ssh "${BBB_HOST}" "chmod +x '${BBB_DEST_DIR}/bt_server.py'"

    # --- Macro files ---
    scp "scripts/macrong"                 "${BBB_HOST}:${BBB_DEST_DIR}/macros/macrong"
    scp "scripts/macrong2"                "${BBB_HOST}:${BBB_DEST_DIR}/macros/macrong2"

    # --- systemd service ---
    scp "pdeServices/lastnic-bt.service"  "${BBB_HOST}:${BBB_DEST_DIR}/lastnic-bt.service"

    # do_configure

    echo "[cross-compile] Deployed and configured successfully."
    echo ""
    echo "BBB MAC: \$(ssh ${BBB_HOST} hciconfig hci0 | grep 'BD Address' | awk '{print \$3}')"
    echo ""
    echo "One-time BT pairing (first deploy only):"
    echo "  On BBB : bluetoothctl agent NoInputNoOutput"
    echo "           bluetoothctl default-agent"
    echo "           bluetoothctl discoverable on"
    echo "           bluetoothctl pairable on"
    echo "  On PC  : bluetoothctl  # then: pair BC:FC:E7:26:35:F3 / trust BC:FC:E7:26:35:F3"
    echo "  On BBB : bluetoothctl discoverable off"
    echo ""
    echo "Usage from PC (MAC baked in — see BBB_MAC in scripts/bt_client.py):"
    echo "  python3 scripts/bt_client.py list"
    echo "  python3 scripts/bt_client.py run macrong"
    echo "  python3 scripts/bt_client.py          # interactive REPL"
}

# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------
case "${1:-build}" in
    build)      do_build     ;;
    clean)      do_clean     ;;
    configure)  do_configure ;;
    deploy)     do_deploy    ;;
    *)
        echo "Usage: $0 [build|clean|configure|deploy]"
        echo "  build      — cross-compile for BBB ARM (default)"
        echo "  clean      — remove build-arm/ directory"
        echo "  configure  — apply BBB system config only (BlueZ, compat, service)"
        echo "  deploy     — build + copy to BBB + configure"
        echo ""
        echo "Environment variables:"
        echo "  BBB_HOST      SSH target (default: ${BBB_HOST})"
        echo "  BBB_DEST_DIR  Remote directory (default: ${BBB_DEST_DIR})"
        exit 1
        ;;
esac
