LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

#FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI = "file://verify.sh"
SRC_URI += "file://key.pem"

# Modify these as desired
PV = "1.0"
PR = "r0"


# NOTE: no Makefile found, unable to determine what needs to be done


do_install () {
	# Specify install commands here
	install -m 0755 -d ${D}/usr/appid
	install -m 0755 -d ${D}/usr/appid/keys
	install -m 0755 -d ${D}/usr/appid/sigs
	install -m 0755 -d ${D}/usr/appid/shas/yocto
	install -m 0755 -d ${D}/usr/appid/shas/local

	install -m 0755 ${WORKDIR}/key.pem ${D}/usr/appid/keys/key.pem
	install -m 0755 ${WORKDIR}/verify.sh ${D}/usr/appid/verify.sh
}

#INSANE_SKIP:${PN} = "ldflags"
FILES:${PN} = "/usr/appid/sha256.sh \
				/usr/appid \
				/usr/appid/keys \
				/usr/appid/keys/key.pem \
				/usr/appid/verify.sh \
				/usr/appid/sigs \
				/usr/appid/shas/yocto \
				/usr/appid/shas/local"
RDEPENDS:${PN} = "bash"