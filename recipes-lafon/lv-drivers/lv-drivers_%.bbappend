SRCREV = "8cdabe8d42bf798d5a31e75cbd5a2cd816c29e5c"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " \
	file://lv_drv_conf.h \
    file://lv_drivers.pc.in \
    file://002-Ajout-pkgconfig.patch;subdir=lv-drivers \
"

PV = "0.1+git${SRCPV}"
PR = "r0"

do_configure:prepend() {
    install -Dm 0644 ${WORKDIR}/lv_drv_conf.h ${S}/lv_drv_conf.h
    install -Dm 0644 ${WORKDIR}/lv_drivers.pc.in ${S}/lv_drivers.pc.in
}

TOOLCHAIN_TARGET_TASK:append = " lv-drivers-dev"

DEPENDS = "lvgl"

EXTRA_OECMAKE = ""

PROVIDES += " lv-drivers"
FILES:${PN}:append = " \
	usr/lib/ \
"
