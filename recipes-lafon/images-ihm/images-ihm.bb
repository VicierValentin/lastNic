
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


# Created on 05 july 2023 by cdemenou

SUMMARY = "Add images for IHM"
DESCRIPTION = "Install images for ihm using"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""


SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/images-ihm.git;protocol=ssh;branch=${VAR_BRANCH_IMG}"

# Modify these as desired
PV = "1.0+git${SRCPV}"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
PR = "r0"

inherit systemd



do_install() {
	# Specify install commands here
	install -m 0755 -d ${D}/data/img
	
	install -m 0644 ${S}/images/*.png ${D}/data/img/

}

FILES:${PN} = "/data/img"

INSANE_SKIP:${PN} = "ldflags"
