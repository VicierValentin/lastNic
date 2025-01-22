SUMMARY = "LVGL - Light and Versatile Graphics Library"
DESCRIPTION = "LVGL is a free and open-source graphics library providing everything \
you need to create embedded GUI with easy-to-use graphical elements, beautiful \
visual effects, and low memory footprint."

LIC_FILES_CHKSUM = "file://LICENCE.txt;md5=bf1198c89ae87f043108cea62460b03a"

LICENSE = "MIT"
SRC_URI = "git://github.com/lvgl/lvgl.git;protocol=http;branch=release/v8.3"

ALLOW_EMPTY:${PN} = "1"

S = "${WORKDIR}/git"

inherit cmake

LVGL_CONFIG_LV_MEM_CUSTOM ?= "0"
LVGL_CONFIG_LV_COLOR_DEPTH ?= "32"

# Upstream does not support a default configuration
# but propose a default "disabled" template, which is used as reference
# More configuration can be done using external configuration variables
do_configure:prepend() {
    [ -r "${S}/lv_conf.h" ] \
        || sed -e 's|#if 0 .*Set it to "1" to enable .*|#if 1 // Enabled|g' \
	    -e "s|\(#define LV_COLOR_DEPTH \).*|\1 ${LVGL_CONFIG_LV_COLOR_DEPTH}|g" \
	    \
	    -e "s|\(#define LV_MEM_CUSTOM .*\)0|\1${LVGL_CONFIG_LV_MEM_CUSTOM}|g" \
	    \
	    -e "s|\(#define LV_TICK_CUSTOM \).*|\1 1|g" \
	    -e "s|\(#define LV_TICK_CUSTOM_INCLUDE \).*|\1 <stdint.h>|g" \
	    -e "s|\(#define LV_TICK_CUSTOM_SYS_TIME_EXPR \).*|extern uint32_t custom_tick_get(void);\n\1 (custom_tick_get())|g" \
	    \
            < "${S}/lv_conf_template.h" > "${S}/lv_conf.h"
}
