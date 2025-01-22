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

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "git://git@bu-gitlab.lafon.fr/bu-alternative-energies/easyborn/webServer_node.git;protocol=ssh;branch=${VAR_BRANCH_WEB}"

# Modify these as desired
PV = "1.0+git${SRCPV}"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"
PR = "r0"

inherit systemd pkgconfig

do_install() {
	install -d ${D}${systemd_system_unitdir}
	install -m 0744 ${S}/service/webserver.service ${D}${systemd_system_unitdir}/webserver.service
	install -d ${D}/root/easy/webServer
	install -m 0766 ${S}/*.js ${D}/root/easy/webServer/
	install -d ${D}/root/easy/webServer/obj
	cp -R --no-dereference --preserve=mode,links -v ${S}/obj/* ${D}/root/easy/webServer/obj/
	#install -m 0766 ${S}/obj/* ${D}/root/easy/webServer/obj/
	install -d ${D}/root/easy/webServer/views
	cp -R --no-dereference --preserve=mode,links -v ${S}/views/* ${D}/root/easy/webServer/views/
	install -d ${D}/root/easy/webServer/node_modules
	cp -R --no-dereference --preserve=mode,links -v ${S}/node_modules/* ${D}/root/easy/webServer/node_modules/
	install -d ${D}/root/easy/webServer/logs
}

SYSTEMD_SERVICE:${PN} = "webserver.service"
FILES:${PN} += "${systemd_system_unitdir}/webserver.service"
FILES:${PN} += "/root/easy/webServer"
FILES:${PN} += "/root/easy/webServer/obj"
FILES:${PN} += "/root/easy/webServer/views"
FILES:${PN} += "/root/easy/webServer/logs"
FILES:${PN} += "/root/easy/webServer/node_modules"

INSANE_SKIP:${PN} = "ldflags file-rdeps staticdev already-stripped"
