# cmake/arm-bbb.cmake — CMake toolchain file for BeagleBone Black cross-compilation
#
# Usage:
#   mkdir build && cd build
#   cmake -DCMAKE_TOOLCHAIN_FILE=../cmake/arm-bbb.cmake ..
#   make
#
# Requires: arm-linux-gnueabihf-gcc toolchain
#   sudo apt install gcc-arm-linux-gnueabihf

set(CMAKE_SYSTEM_NAME Linux)
set(CMAKE_SYSTEM_PROCESSOR arm)

# Toolchain prefix — adjust if your toolchain uses a different prefix
set(CROSS_COMPILE arm-linux-gnueabihf-)

set(CMAKE_C_COMPILER   ${CROSS_COMPILE}gcc)
set(CMAKE_CXX_COMPILER ${CROSS_COMPILE}g++)
set(CMAKE_STRIP        ${CROSS_COMPILE}strip)

# Tune for AM335x Cortex-A8 (BeagleBone Black)
set(CMAKE_C_FLAGS_INIT   "-march=armv7-a -mfpu=vfpv3-d16 -mfloat-abi=hard")
set(CMAKE_CXX_FLAGS_INIT "-march=armv7-a -mfpu=vfpv3-d16 -mfloat-abi=hard")

# Sysroot (optional — set if you have a BBB rootfs for header/library matching)
# set(CMAKE_SYSROOT /opt/bbb-sysroot)
# set(CMAKE_FIND_ROOT_PATH ${CMAKE_SYSROOT})
# set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
# set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY ONLY)
# set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE ONLY)
# set(CMAKE_FIND_ROOT_PATH_MODE_PACKAGE ONLY)
