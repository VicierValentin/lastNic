SUMMARY = "lafon Growfs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI += "file://lafon-grow-data.service"

S = "${WORKDIR}"

inherit systemd

do_install () {
	install -d ${D}${systemd_system_unitdir}
	install -m 0644 ${WORKDIR}/lafon-grow-data.service ${D}${systemd_system_unitdir}/lafon-grow-data.service 
}

SYSTEMD_SERVICE:${PN} = "lafon-grow-data.service"
FILES:${PN} = "${systemd_system_unitdir}/lafon-grow-data.service"
