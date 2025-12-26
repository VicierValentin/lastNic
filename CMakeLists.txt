cmake_minimum_required(VERSION 3.10)
project(usb_keyboard VERSION 1.0.0 LANGUAGES CXX)

# Set C++ standard
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Compiler flags
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -Wall -Wextra")
set(CMAKE_CXX_FLAGS_RELEASE "-O2")

# Executable
add_executable(usb_keyboard main.cpp)

include(FindPkgConfig)
pkg_check_modules(LOG REQUIRED liblog)

# Headers are header-only, just need to be in the include path
target_include_directories(usb_keyboard PRIVATE ${CMAKE_CURRENT_SOURCE_DIR})
target_include_directories(usb_keyboard SYSTEM PUBLIC ${LOG_INCLUDE_DIRS})
target_link_libraries(usb_keyboard ${LOG_LIBRARIES})

# Install targets
install(TARGETS usb_keyboard DESTINATION bin)
install(PROGRAMS setup_usb_gadget.sh DESTINATION bin)
