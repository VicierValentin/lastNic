SUMMARY = "Initial boot script"
DESCRIPTION = "Script flash eMMC a first start"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
#LIC_FILES_CHKSUM = "file://licence.lic;md5=dbbf8220893d497d403bb9cdf49db7a4"

SRC_URI = "\
	file://initscript.sh \
	file://initscript.service \
	file://blinkled.sh \
	file://blinkled.service \
	file://licence.lic \
"
	
inherit systemd

do_install() {
	install -m 0755 -d ${D}/root
	
	install -m 0755 ${WORKDIR}/initscript.sh ${D}/root
	install -Dm 0644 ${WORKDIR}/initscript.service ${D}${systemd_system_unitdir}/initscript.service
	
	install -m 0755 ${WORKDIR}/blinkled.sh ${D}/root
	install -Dm 0644 ${WORKDIR}/blinkled.service ${D}${systemd_system_unitdir}/blinkled.service
	
	install -m 0755 ${WORKDIR}/licence.lic ${D}/root

}

SYSTEMD_SERVICE:${PN} = "initscript.service"
SYSTEMD_SERVICE:${PN} += "blinkled.service"
FILES:${PN} = "/root"

INSANE_SKIP:${PN} = "ldflags"
