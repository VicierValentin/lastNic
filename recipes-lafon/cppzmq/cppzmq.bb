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

#FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI = "git://git@github.com/zeromq/cppzmq.git;protocol=ssh;branch=master"
S = "${WORKDIR}/git"
SRCREV = "${AUTOREV}"
inherit cmake pkgconfig

# Add -DCPPZMQ_BUILD_TESTS=OFF to avoid building tests
EXTRA_OECMAKE = "-DCPPZMQ_BUILD_TESTS=OFF"

DEPENDS += "systemd czmq"

PV = "1.0+git${SRCPV}"
PR = "r0"

PROVIDES += " cppzmq"

FILES:${PN}-dev += "\
	usr/include/ \
	usr/lib/ \
"

ALLOW_EMPTY:${PN} = "1"
TOOLCHAIN_TARGET_TASK:append = " cppzmq"