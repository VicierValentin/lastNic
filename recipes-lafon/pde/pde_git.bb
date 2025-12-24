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
SRC_URI = "git://github.com/MADIC-industries/pde.git;protocol=ssh;branch=${VAR_BRANCH}"
S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"
inherit systemd pkgconfig cmake


EXTRA_OECMAKE = ""

# Modify these as desired
PV = "1.0+git${SRCPV}"
PR = "r0"

DEPENDS:append= "curl czmq cppzmq sqlite3 lib-log gestion-bdd systemd libgpiod private-key sha256 openssl-native" 
do_compile:prepend() {
    cp ${STAGING_DATADIR}/private-key/private_key.pem ${WORKDIR}/build
}


do_install:prepend() {
	# Specify install commands here
	install -m 0755 -d ${D}/root/easy
	install -m 0755 -d ${D}/usr/appid/shas/yocto
	install -m 0755 -d ${D}/usr/appid/sigs/

	install -m 0755 ./pde ${D}/root/easy/EB_Pde
	#install -m 0755 ./journal.sig ${D}/usr/appid/sigs/EB_Jrnl
}

do_install() {
	sha256sum ${D}/root/easy/EB_Pde | awk '{print $1}' > ./EB_Pde.sha256

	# Sign EB_Pde.sha256
	openssl dgst -sha256 -sign ${STAGING_DATADIR}/private-key/private_key.pem -out ./EB_Pde.sha256.sig ./EB_Pde.sha256

	install -m 0755 EB_Pde.sha256 ${D}/usr/appid/shas/yocto/EB_Pde
	install -m 0755 EB_Pde.sha256.sig ${D}/usr/appid/sigs/EB_Pde
}

PROVIDES += " pde"

INSANE_SKIP:${PN} += "ldflags"


#INSANE_SKIP:${PN} = "ldflags"
FILES:${PN} = "/root/easy \
	/usr/appid/ \
	/usr/appid/shas/ \
	/usr/appid/shas/yocto/ \
	/usr/appid/shas/yocto/EB_Pde \
	/usr/appid/sigs/EB_Pde \
	/usr/appid/sigs/EB_Pde.sha256.sig"