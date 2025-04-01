FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit relative_symlinks

SRC_URI:append = " \
    file://dlt-system.conf \
    file://dlt-system.service \
    file://dlt.service \
    file://dlt.conf \
"

PACKAGECONFIG:remove = "dlt-examples dlt-adaptor dlt-adaptor-udp udp-connection"

# Disable IPV6 to remove err 97
EXTRA_OECMAKE:append = " -DWITH_DLT_USE_IPv6=OFF -DWITH_DLT_CXX11_EXT=ON -DWITH_DLT_TESTS=OFF"

# Enable Journald log
do_install:append() {
    install -Dm 0644 ${WORKDIR}/dlt-system.conf ${D}${sysconfdir}/dlt-system.conf
    install -Dm 0644 ${WORKDIR}/dlt.conf ${D}${sysconfdir}/dlt.conf
    install -Dm 0644 ${WORKDIR}/dlt.service ${D}${systemd_system_unitdir}/dlt.service
    install -Dm 0644 ${WORKDIR}/dlt-system.service ${D}${systemd_system_unitdir}/dlt-system.service
    install -Dm 0644 ${WORKDIR}/dlt_logstorage.conf ${D}${sysconfdir}/dlt_logstorage.conf
}

SYSTEMD_SERVICE:${PN} += "dlt.service"
SYSTEMD_SERVICE:${PN} += "dlt-system.service"

FILES:${PN}:append = " \
    ${sysconfdir}/dlt_logstorage.conf \
    ${sysconfdir}/dlt.conf \
    ${sysconfdir}/dlt-system.conf \
"
