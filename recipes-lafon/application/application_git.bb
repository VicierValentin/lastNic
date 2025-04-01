# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# Unable to find any files that looked like license statements. Check the accompanying
# documentation and source headers and set LICENSE and LIC_FILES_CHKSUM accordingly.
#
# NOTE: LICENSE is being set to "CLOSED" to allow you to at least start building - if
# this is not accurate with respect to the licensing of the software being built (it
# will not be in most cases) you must specify the correct value before using this
# recipe for anything other than initial testing/development!
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
inherit systemd

SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/main.git;protocol=ssh;branch=master"
SRC_URI += "file://magfleet-app.service"
SRC_URI += "file://magfleet-ihm.service"
SRC_URI += "file://magfleet-ctrl.service"
SRC_URI += "file://magfleet-jrnl.service"
SRC_URI += "file://lon.service"
SRC_URI += "file://magfleet-application.target"
SRC_URI += "file://magfleet-mid.target"
SRC_URI += "file://20-i2c-screen.rules"
SRC_URI += "file://30-btn.rules"
SRC_URI += "file://tty-no-cursor-blink.service"
SRC_URI += "file://no-cursor-blink.sh"
SRC_URI += "file://public.pem"
SRC_URI += "file://script_check_sd_card"


# Modify these as desired
PV = "1.0+git${SRCPV}"
#SRCREV = "08545006b50baa2f523c66ee1c389a19cbf6d6d9"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
PR = "r0"
# NOTE: no Makefile found, unable to determine what needs to be done

DEPENDS = "systemd lib-log"

do_compile () {
	oe_runmake default
}

do_install () {
	# Specify install commands here
	install -d ${D}/root/easy
	install -m 0755 ./main ${D}/root/easy/EB_App
	install -m 0755 ${WORKDIR}/no-cursor-blink.sh ${D}/root/easy/no-cursor-blink.sh
	install -m 0755 ${WORKDIR}/script_check_sd_card ${D}/root/easy/script_check_sd_card
	install -d ${D}/etc/udev/rules.d
	install -m 644 ${WORKDIR}/20-i2c-screen.rules ${D}/etc/udev/rules.d/20-i2c-screen.rules
	install -m 644 ${WORKDIR}/30-btn.rules ${D}/etc/udev/rules.d/30-btn.rules
	install -d ${D}${systemd_unitdir}/system
	install -m 644 ${WORKDIR}/magfleet-application.target ${D}/${systemd_unitdir}/system
	install -m 644 ${WORKDIR}/magfleet-mid.target ${D}/${systemd_unitdir}/system
	install -m 644 ${WORKDIR}/lon.service ${D}/${systemd_unitdir}/system	
	install -m 644 ${WORKDIR}/magfleet-app.service ${D}/${systemd_unitdir}/system
	install -m 644 ${WORKDIR}/magfleet-ihm.service ${D}/${systemd_unitdir}/system
	install -m 644 ${WORKDIR}/magfleet-ctrl.service ${D}/${systemd_unitdir}/system
	install -m 644 ${WORKDIR}/magfleet-jrnl.service ${D}/${systemd_unitdir}/system
	install -d ${D}/data
	install -m 0644 ${WORKDIR}/public.pem ${D}/data/public.pem
#	install -m 644 ${WORKDIR}/tty-no-cursor-blink.service ${D}/${systemd_unitdir}/system	
#	install -d ${D}/etc/systemd/system
#	install -m 644 ${WORKDIR}/easyborn.service ${D}/etc/systemd/system	
}


SYSTEMD_PACKAGES = "${@bb.utils.contains('DISTRO_FEATURES','systemd','${PN}','',d)}"
SYSTEMD_SERVICE:${PN} += "magfleet-application.target"
SYSTEMD_SERVICE:${PN} += "magfleet-app.service"
SYSTEMD_SERVICE:${PN} += "magfleet-ihm.service"
SYSTEMD_SERVICE:${PN} += "magfleet-ctrl.service"
SYSTEMD_SERVICE:${PN} += "magfleet-mid.target"
SYSTEMD_SERVICE:${PN} += "magfleet-jrnl.service"
SYSTEMD_SERVICE:${PN} += "lon.service"
INSANE_SKIP:${PN} = "ldflags"
FILES:${PN} = "/root/easy \
				/etc/systemd/system \
				${systemd_unitdir}/system\
				/etc/udev/rules.d \
				/data"
