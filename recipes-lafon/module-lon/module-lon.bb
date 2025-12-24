SUMMARY = "lon Linux kernel module"
DESCRIPTION = "${SUMMARY}"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "git://github.com/MADIC-industries/module-lon.git;protocol=ssh;branch=prod"
#SRC_URI = "git://github.com/martonmiklos/dialog_u60_linux_driver.git;"

S = "${WORKDIR}/git"

# Modify these as desired
PV = "1.0+git${SRCPV}"
#SRCREV = "08545006b50baa2f523c66ee1c389a19cbf6d6d9"
SRCREV = "${AUTOREV}"
inherit module
DEPENDS += "systemd"


#MACHINE_EXTRA_RRECOMMENDS += "kernel-module-u50"