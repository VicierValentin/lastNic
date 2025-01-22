SUMMARY = "Scripts to support a BBB eMMC installation fir image_lafon"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
	file://install_image_terrain_lafon.sh \
"
inherit systemd

S = "${WORKDIR}"

do_install:append () {
    install -d ${D}${bindir}
    install -m 0755 install_image_terrain_lafon.sh ${D}${bindir}
}

FILES:${PN} = "${bindir}" 
