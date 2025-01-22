SUMMARY = "Add database for multilingue"
DESCRIPTION = "Install multilingue database for ihm using"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""


# Created on  13 february 2024 by cdemenou

SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/multilangue.git;protocol=ssh;branch=${VAR_BRANCH_LANG}"

# Modify these as desired
PV = "1.0+git${SRCPV}"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
PR = "r0"

inherit systemd

do_install() {
	# Specify install commands here
	install -m 0755 -d ${D}/root/easy/bdd_lang
	install -m 0755 ${S}/lg_db/*.db ${D}/root/easy/bdd_lang/
}

FILES:${PN} = "/root/easy/bdd_lang"

INSANE_SKIP:${PN} = "ldflags"
