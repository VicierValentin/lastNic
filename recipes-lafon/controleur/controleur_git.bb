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

EXTRA_OECMAKE = ""
#SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/easyborn-controleur-ftp.git;protocol=ssh;branch=${VAR_BRANCH}"
SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/easyborn-controleur-ftp.git;protocol=ssh;branch=midVal"


# Modify these as desired
PV = "1.0+git${SRCPV}"
#SRCREV = "08545006b50baa2f523c66ee1c389a19cbf6d6d9"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
PR = "r0"
# NOTE: no Makefile found, unable to determine what needs to be done

DEPENDS += "curl openssl sqlite3 gestion-bdd lib-log systemd libgpiod lib-lon private-key sha256 openssl-native"

do_compile:prepend() {
    cp ${STAGING_DATADIR}/private-key/private_key.pem ${WORKDIR}/build
}


#do_compile () {
#	cd src
#	oe_runmake yocto
#}


do_install:prepend() {
	# Specify install commands here
	install -d ${D}/root/easy
	install -m 0755 -d ${D}/usr/appid/shas/yocto
	install -m 0755 -d ${D}/usr/appid/sigs/
	install -d ${D}/data/bdd

	#install -m 0755 ./ctrl.sig ${D}/usr/appid/sigs/EB_Ctrl
}

do_install() {

	install -m 0755 ./ctrl ${D}/root/easy/EB_Ctrl
	
	sha256sum ${D}/root/easy/EB_Ctrl | awk '{print $1}' > ./EB_Ctrl.sha256
	
	# Sign EB_Ctrl.sha256
	openssl dgst -sha256 -sign ${STAGING_DATADIR}/private-key/private_key.pem -out ./EB_Ctrl.sha256.sig ./EB_Ctrl.sha256
	
	
	install -m 0755 EB_Ctrl.sha256 ${D}/usr/appid/shas/yocto/EB_Ctrl
	install -m 0755 EB_Ctrl.sha256.sig ${D}/usr/appid/sigs/EB_Ctrl
}


INSANE_SKIP:${PN} += "ldflags already-stripped"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"

FILES:${PN} = "/root/easy \
	/usr/appid/shas/yocto/EB_Ctrl \
	/usr/appid/sigs/EB_Ctrl \
	/usr/appid/sigs/EB_Ctrl.sha256.sig \
	/usr/appid/shas/yocto/EB_Ctrl.sha256"
FILES:${PN} += "/data/bdd"
