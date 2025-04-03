FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit relative_symlinks systemd pkgconfig cmake

SRC_URI:append = " \
    file://dlt-system.conf \
    file://dlt-system.service \
    file://dlt.service \
    file://dlt.conf \
"

PACKAGECONFIG:remove = "dlt-examples dlt-adaptor dlt-adaptor-udp udp-connection"

# Disable IPV6 to remove err 97
EXTRA_OECMAKE:append = " -DWITH_DLT_USE_IPv6=OFF -DWITH_DLT_CXX11_EXT=ON -DWITH_DLT_TESTS=OFF -DWITH_DLT_PKGCONFIG=ON"

# Enable Journald log
do_install:append() {
    install -Dm 0644 ${WORKDIR}/dlt-system.conf ${D}${sysconfdir}/dlt-system.conf
    install -Dm 0644 ${WORKDIR}/dlt.conf ${D}${sysconfdir}/dlt.conf
    install -Dm 0644 ${WORKDIR}/dlt.service ${D}${systemd_system_unitdir}/dlt.service
    install -Dm 0644 ${WORKDIR}/dlt-system.service ${D}${systemd_system_unitdir}/dlt-system.service

    install -m 0755 -d ${D}/data/logs
    install -Dm 0644 ${WORKDIR}/dlt_logstorage.conf ${D}/data/logs/
}

SYSTEMD_SERVICE:${PN} += "dlt.service"
SYSTEMD_SERVICE:${PN} += "dlt-system.service"

TOOLCHAIN_TARGET_TASK:append = " dlt-daemon-dev"

FILES:${PN}:append = " \
    ${sysconfdir}/dlt.conf \
    ${sysconfdir}/dlt-system.conf \
    /data/logs/dlt_logstorage.conf \
"
