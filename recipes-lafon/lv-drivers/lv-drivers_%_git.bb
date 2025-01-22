LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d6fc0df890c5270ef045981b516bb8f2"
SRC_URI = "git://github.com/lvgl/lv_drivers.git;protocol=http;branch=release/v8.3"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'wayland fbdev', d)}"
TARGET_CFLAGS += "-DLV_CONF_INCLUDE_SIMPLE=1"

S = "${WORKDIR}/git"

inherit cmake
