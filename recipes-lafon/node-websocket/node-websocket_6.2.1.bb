# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

SUMMARY = "Simple to use, blazing fast and thoroughly tested websocket client and server for Node.js"
HOMEPAGE = "https://github.com/websockets/ws"
# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=95833e8f03687308b0584a377b9e12b0 \
                    file://node_modules/async-limiter/LICENSE;md5=4b83a79a0c223073786a52b5ece4619d \
                    file://package.json;md5=f2cdd113049c9d00265843d6b4f9e292 \
                    file://node_modules/async-limiter/package.json;md5=1023ed343652ddb4c1948df4c789fe8a"

SRC_URI = " \
    https://github.com/websockets/ws/archive/6.2.1.tar.gz \
    npmsw://${THISDIR}/${BPN}/npm-shrinkwrap.json \
    "
SRC_URI[md5sum] = "f2c5c08bf367e1b1ecf24e09cffba156"
SRC_URI[sha1sum] = "ebd50d70c6697cf0f8e0fe65994a9170f5ab9a4c"
SRC_URI[sha256sum] = "cf28b049a1f52721364e97b7b05f544240d2828c960d629abd2c09306f2ab61a"
SRC_URI[sha384sum] = "9dbda0294c456e9532cd71182ae93a3aaab5430756103dc656061b8432bdb4d3ee3cd28384bc8924fec99b8cfd5826aa"
SRC_URI[sha512sum] = "5848c0dd4fb169ab785e2a0ec29941ab4a58b67d5159d183cd37b77027a03f140269c3b304ea04f8a8b7b01243e0a1967d572cd23370218c17f20a13aa86932a"

S = "${WORKDIR}/ws-${PV}"

inherit npm

LICENSE:${PN} = "MIT"
LICENSE:${PN}-async-limiter = "MIT"
