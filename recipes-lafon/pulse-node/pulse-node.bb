SUMMARY = "Magfleet node js"
DESCRIPTION = "Install node js in linux"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
	file://node-v20.4.0-linux-armv7l.tar.xz \
	"
	
S = "${WORKDIR}"

do_install() {
	install -d ${D}/sbin
	install -d -m 0755 ${D}/usr/local/lib/nodejs/ 	
	cp -a -r -f --no-preserve=ownership --no-dereference --preserve=mode,links ${S}/node-v20.4.0-linux-armv7l/* ${D}/usr/local/lib/nodejs/
}

do_install:append() {
	ln -sf /usr/local/lib/nodejs/bin/node ${D}/sbin
	ln -sf /usr/local/lib/nodejs/bin/npm ${D}/sbin
}
FILES:${PN} += "/usr/local/lib/nodejs"
FILES:${PN} += "/usr/sbin"
RDEPENDS:${PN} += "bash python3-core"
INSANE_SKIP:${PN} = " ldflags file-rdeps staticdev already-stripped"

