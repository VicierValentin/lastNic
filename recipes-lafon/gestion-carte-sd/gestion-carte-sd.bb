SUMMARY = "Initial boot script"
DESCRIPTION = "Script flash eMMC a first start"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit systemd

SRC_URI = "file://40-add_sd.rules"
SRC_URI += "file://40-remove_sd.rules"
SRC_URI += "file://add_sd_script"
SRC_URI += "file://remove_sd_script"
SRC_URI += "file://mount-sd@.service"
SRC_URI += "file://umount-sd@.service"


# Modify these as desired
SRCREV = "${AUTOREV}"

do_install () {
	# Specify install commands here
	install -d ${D}/etc/udev/rules.d
	install -m 0644 ${WORKDIR}/40-add_sd.rules ${D}/etc/udev/rules.d/40-add_sd.rules
	install -m 0644 ${WORKDIR}/40-remove_sd.rules ${D}/etc/udev/rules.d/40-remove_sd.rules
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/add_sd_script ${D}${bindir}/add_sd_script
	install -m 0755 ${WORKDIR}/remove_sd_script ${D}${bindir}/remove_sd_script
	install -d ${D}${systemd_unitdir}/system
	install -m 0644 ${WORKDIR}/mount-sd@.service ${D}${systemd_unitdir}/system/
	install -m 0644 ${WORKDIR}/umount-sd@.service ${D}/${systemd_unitdir}/system/
}

SYSTEMD_SERVICE:${PN} += "mount-sd@.service "
SYSTEMD_SERVICE:${PN} += "umount-sd@.service "
FILES:${PN} = "/root/easy \
				/etc/systemd/system \
				${systemd_unitdir}/system\
				/etc/udev/rules.d \
				${bindir} \
				/data"
