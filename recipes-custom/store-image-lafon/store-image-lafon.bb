SUMMARY = "Stock image a installer sur beaglebone"
SECTION = "examples"
#LICENSE = "MIT"
#LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

#datadir => /usr/share
FILESEXTRAPATHS:prepend := "${DEPLOY_DIR}/images/beaglebone-lafon:"

SRC_URI = "file://${IMAGE_EASY_NAME}-beaglebone-lafon.wic"
SRC_URI += "file://${IMAGE_EASY_NAME}-beaglebone-lafon.wic.bmap"

do_install() {
		install -d ${D}${datadir}
		install -m 0777 ${DEPLOY_DIR}/images/beaglebone-lafon/${IMAGE_EASY_NAME}-beaglebone-lafon.wic ${D}${datadir}/base-image-lafon-beaglebone-lafon.rootfs.wic
		install -m 0777 ${DEPLOY_DIR}/images/beaglebone-lafon/${IMAGE_EASY_NAME}-beaglebone-lafon.wic.bmap ${D}${datadir}/base-image-lafon-beaglebone-lafon.rootfs.wic.bmap
}
	
FILES:${PN} += "${datadir}"
