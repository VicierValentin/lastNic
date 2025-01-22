SUMMARY = "Scripts to support a BBB eMMC installation fit image_lafon"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
	file://update_imageOCPP15_lafon.sh \
	file://update_imageOCPP15.service \
	file://HTTP_test.py \
	file://led-update-sequence \
	file://read \
	file://authorize \
"

inherit systemd

S = "${WORKDIR}"

do_install:append () {
    install -d ${D}${bindir}
    install -m 0755 update_imageOCPP15_lafon.sh ${D}${bindir}
    install -m 0755 HTTP_test.py ${D}${bindir}
    install -m 0755 authorize ${D}${bindir}
    install -m 0755 read ${D}${bindir}
    install -m 0755 led-update-sequence ${D}${bindir}
    install -Dm 0644 ${WORKDIR}/update_imageOCPP15.service ${D}${systemd_system_unitdir}/update_imageOCPP15.service
}

SYSTEMD_SERVICE:${PN} = "update_imageOCPP15.service"
FILES:${PN} = "${bindir}" 
