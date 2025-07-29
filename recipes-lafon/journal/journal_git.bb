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
SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/journal.git;protocol=ssh;branch=main"
S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"
inherit systemd pkgconfig cmake


EXTRA_OECMAKE = ""

# Modify these as desired
PV = "1.0+git${SRCPV}"
PR = "r0"

# NOTE: no Makefile found, unable to determine what needs to be done

DEPENDS:append= "curl sqlite3 lib-log gestion-bdd systemd lvgl lv-drivers libgpiod lib-lon private-key sha256 openssl-native" 
do_compile:prepend() {
    cp ${STAGING_DATADIR}/private-key/private_key.pem ${WORKDIR}/build
}

do_install:prepend() {
	# Specify install commands here
	install -m 0755 -d ${D}/root/easy
	install -m 0755 -d ${D}/usr/appid/shas/yocto
	install -m 0755 -d ${D}/usr/appid/sigs/

	install -m 0755 ./journal ${D}/root/easy/EB_Jrnl
	#install -m 0755 ./journal.sig ${D}/usr/appid/sigs/EB_Jrnl
}

do_install() {
	sha256sum ${D}/root/easy/EB_Jrnl | awk '{print $1}' > ./EB_Jrnl.sha256

	# Sign EB_Jrnl.sha256
	openssl dgst -sha256 -sign ${STAGING_DATADIR}/private-key/private_key.pem -out ./EB_Jrnl.sha256.sig ./EB_Jrnl.sha256

	install -m 0755 EB_Jrnl.sha256 ${D}/usr/appid/shas/yocto/EB_Jrnl
	install -m 0755 EB_Jrnl.sha256.sig ${D}/usr/appid/sigs/EB_Jrnl
}

PROVIDES += " journal"

INSANE_SKIP:${PN} += "ldflags already-stripped"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"


#INSANE_SKIP:${PN} = "ldflags"
FILES:${PN} = "/root/easy \
	/usr/appid/ \
	/usr/appid/shas/ \
	/usr/appid/shas/yocto/ \
	/usr/appid/shas/yocto/EB_Jrnl \
	/usr/appid/sigs/EB_Jrnl \
	/usr/appid/sigs/EB_Jrnl.sha256.sig"
