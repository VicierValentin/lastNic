SUMMARY = "U-Boot uEnv.txt SD boot environment generation for Zynq targets"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"

PR = "r1"

SRC_URI +="file://uEnv.txt"

inherit deploy

do_deploy() {
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 0644 ${WORKDIR}/uEnv.txt ${DEPLOY_DIR_IMAGE}/uEnv.txt
}

#addtask deploy before do_package after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${BOOTFILES_DIR_NAME}"

PACKAGE_ARCH = "${MACHINE_ARCH}"
