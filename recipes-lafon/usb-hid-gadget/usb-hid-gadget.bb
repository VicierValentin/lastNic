SUMMARY = "USB HID keyboard gadget setup"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835..."

SRC_URI = " \
    file://usb-hid-gadget.service \
    file://gadget-init.sh \
    file://usb_keyboard \
    file://kbemu.service \
"

S = "${WORKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "usb-hid-gadget.service"
SYSTEMD_AUTO_ENABLE = "enable"

RDEPENDS:${PN} = "bash"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/gadget-init.sh ${D}${bindir}/usb-hid-gadget-init.sh
    install -m 0755 ${WORKDIR}/usb_keyboard ${D}${bindir}/usb_keyboard

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/usb-hid-gadget.service \
        ${D}${systemd_system_unitdir}
}