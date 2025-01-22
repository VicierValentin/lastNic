SUMMARY = "Scripts to support a BBB eMMC installation fir image_lafon"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
	file://install_image_magfleet.sh \
	file://install_image.service \
	file://authorize \
"
inherit systemd

S = "${WORKDIR}"

do_install:append () {
    install -d ${D}${bindir}
    install -m 0755 install_image_magfleet.sh ${D}${bindir}
    install -m 0755 authorize ${D}${bindir}
    install -Dm 0644 ${WORKDIR}/install_image.service ${D}${systemd_system_unitdir}/install_image.service
}

SYSTEMD_SERVICE:${PN} += "install_image.service"

FILES:${PN} = "${bindir} \
lib \
lib/systemd \
lib/systemd/system \
lib/systemd/system/install_image.service" 
