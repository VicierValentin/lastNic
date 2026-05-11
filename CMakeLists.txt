cmake_minimum_required(VERSION 3.16)

project(lastnic
    VERSION 1.0.0
    DESCRIPTION "BBB USB HID keyboard emulator"
    LANGUAGES C
)

set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

# ---------------------------------------------------------------------------
# Cross-compilation support
# ---------------------------------------------------------------------------
# Option A — CMake toolchain file:
#   mkdir build-arm && cd build-arm
#   cmake -DCMAKE_TOOLCHAIN_FILE=../cmake/arm-bbb.cmake ..
#   make
#
# Option B — convenience script (recommended):
#   ./cross-compile.sh           # requires: sudo apt install gcc-arm-linux-gnueabihf
#   ./cross-compile.sh deploy    # build + scp to BBB
#
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Sources
# ---------------------------------------------------------------------------
set(SOURCES
    src/main.c
)

set(HEADERS
    include/keymap.h
    include/special_keys.h
)

add_executable(lastnic ${SOURCES} ${HEADERS})

target_include_directories(lastnic PRIVATE
    ${CMAKE_SOURCE_DIR}/include
)

target_compile_options(lastnic PRIVATE
    -Wall
    -Wextra
    -Wpedantic
    -Wformat=2
    -Wshadow
    -O2
)

# ---------------------------------------------------------------------------
# Install
# ---------------------------------------------------------------------------
install(TARGETS lastnic
    RUNTIME DESTINATION bin
)

install(FILES scripts/setup_gadget.sh
    DESTINATION bin
    PERMISSIONS
        OWNER_READ OWNER_WRITE OWNER_EXECUTE
        GROUP_READ GROUP_EXECUTE
        WORLD_READ WORLD_EXECUTE
)
