FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://patchboot.cfg;subdir=git"
SRC_URI += "file://bootcmd.cfg;subdir=git"
     
RDEPENDS:${PN} += "u-boot-pulse"
