SUMMARY = "ACM Linux kernel module"
DESCRIPTION = "${SUMMARY}"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

#SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/module-acm.git;protocol=ssh;branch=main"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "file://cdc-acm.ko"

#S = "${WORKDIR}"

# Modify these as desired
#PV = "1.0+git${SRCPV}"
#SRCREV = "08545006b50baa2f523c66ee1c389a19cbf6d6d9"
SRCREV = "${AUTOREV}"
inherit module

#DEPENDS += "systemd"

do_compile[noexec] = "1"

do_install () {
	# Specify install commands here
	install -m 0755 -d ${D}/lib/modules/${KERNEL_SRC}/extra
	install -m 0755 ${WORKDIR}/cdc-acm.ko ${D}/lib/modules/${KERNEL_SRC}/extra/cdc-adm.ko
}

FILES:${PN}:append = "/lib/modules/${KERNEL_SRC}/extra"
