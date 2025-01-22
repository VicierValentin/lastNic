FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://10-rndis.network \
    file://10-eth.network \
"

FILES:${PN} += " \
    ${systemd_unitdir}/network/10-rndis.network \
    ${systemd_unitdir}/network/10-eth.network \
"

do_install:append() {
    install -d ${D}${systemd_unitdir}/network
    install -m 0644 ${WORKDIR}/10-rndis.network ${D}${systemd_unitdir}/network
    install -m 0644 ${WORKDIR}/10-eth.network ${D}${systemd_unitdir}/network
}
