SRCREV = "39687e808fd0b84357f646ad390a0eb26764cb3f"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " \
	file://lv_conf.h \
    file://001-custom-cmake.patch;subdir=lvgl \
"

PV = "1.0+git${SRCPV}"
PR = "r0"

do_configure:prepend() {
        install -Dm 0644 ${WORKDIR}/lv_conf.h ${S}/lv_conf.h
}

TOOLCHAIN_TARGET_TASK:append = " lvgl-dev"

PROVIDES += " lvgl"

DEPENDS:append = ""
FILES:${PN} += "\
	usr/include/ \
	usr/lib/ \
"
