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

do_deploy() {
    do_build

    echo "[cross-compile] Deploying to ${BBB_HOST}:${BBB_DEST_DIR}/"
    ssh "${BBB_HOST}" "mkdir -p '${BBB_DEST_DIR}/scripts'"
    scp "${BUILD_DIR}/${BINARY}"          "${BBB_HOST}:${BBB_DEST_DIR}/${BINARY}"
    scp "scripts/setup_gadget.sh"         "${BBB_HOST}:${BBB_DEST_DIR}/setup_gadget.sh"
    ssh "${BBB_HOST}" "chmod +x '${BBB_DEST_DIR}/${BINARY}' '${BBB_DEST_DIR}/setup_gadget.sh'"

    echo "[cross-compile] Deployed successfully."
    echo ""
    echo "On the BBB, run:"
    echo "  sudo ${BBB_DEST_DIR}/scripts/setup_gadget.sh"
    echo "  ${BBB_DEST_DIR}/${BINARY} [options] <textfile>"
}

# ---------------------------------------------------------------------------
# Entry point
# ---------------------------------------------------------------------------
case "${1:-build}" in
    build)   do_build  ;;
    clean)   do_clean  ;;
    deploy)  do_deploy ;;
    *)
        echo "Usage: $0 [build|clean|deploy]"
        echo "  build   — cross-compile for BBB ARM (default)"
        echo "  clean   — remove build-arm/ directory"
        echo "  deploy  — build + copy to BBB via SSH"
        echo ""
        echo "Environment variables:"
        echo "  BBB_HOST      SSH target (default: ${BBB_HOST})"
        echo "  BBB_DEST_DIR  Remote directory (default: ${BBB_DEST_DIR})"
        exit 1
        ;;
esac
