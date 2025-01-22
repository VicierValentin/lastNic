FILESEXTRAPATHS:prepend := "${THISDIR}/files:"


SRC_URI += "file://psplash-madicgroup-img.h \
			file://psplash-bar-img.h \
			file://psplash-colors.h \
"

SPLASH_IMAGES = "file://psplash-madicgroup-img.h;outsuffix=default"


do_configure:append () {
	cd ${S}
	cp ../psplash-colors.h ./
	cp ../psplash-bar-img.h ./
}

