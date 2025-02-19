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

#FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/ihm.git;protocol=ssh;branch=${VAR_BRANCH}"
S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"
inherit systemd pkgconfig cmake


EXTRA_OECMAKE = ""

# Modify these as desired
PV = "1.0+git${SRCPV}"
PR = "r0"

# NOTE: no Makefile found, unable to determine what needs to be done

DEPENDS:append= "sqlite3 lib-log gestion-bdd systemd lvgl lv-drivers czmq libgpiod lib-lon" 

#do_compile () {
#	cd src
#	oe_runmake yocto
#}

do_install () {
	# Specify install commands here
	install -m 0755 -d ${D}/root/easy
	install -m 0755 ./ihm ${D}/root/easy/EB_Ihm
}

#INSANE_SKIP:${PN} = "ldflags"
FILES:${PN} = "/root/easy"
