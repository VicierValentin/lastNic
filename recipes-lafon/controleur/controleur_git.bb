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
inherit systemd pkgconfig cmake


SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/easyborn-controleur-ftp.git;protocol=ssh;branch=${VAR_BRANCH}"


# Modify these as desired
PV = "1.0+git${SRCPV}"
#SRCREV = "08545006b50baa2f523c66ee1c389a19cbf6d6d9"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
PR = "r0"
# NOTE: no Makefile found, unable to determine what needs to be done

DEPENDS += "curl openssl sqlite3 gestion-bdd lib-log systemd libgpiod lib-lon"

#do_compile () {
#	cd src
#	oe_runmake yocto
#}


do_install () {
	# Specify install commands here
	install -d ${D}/root/easy
	install -m 0755 ./ctrl ${D}/root/easy/EB_Ctrl
	install -d ${D}/data/bdd
}


INSANE_SKIP:${PN} = "ldflags"
FILES:${PN} = "/root/easy"
FILES:${PN} += "/data/bdd"
