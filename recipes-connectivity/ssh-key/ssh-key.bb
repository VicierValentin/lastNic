SUMMARY = "Pre-generated host keys"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = "file://ssh_host_rsa_key \
                 file://ssh_host_rsa_key.pub \
                 file://ssh-easyborn-key.pub \
file://ssh_host_rsa_key.pub"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

INHIBIT_DEFAULT_DEPS = "1"

do_install () {
    install -d ${D}${sysconfdir}/ssh
    install ${WORKDIR}/ssh_host_*_key* ${D}${sysconfdir}/ssh/
    chmod 0600 ${D}${sysconfdir}/ssh/*
    chmod 0644 ${D}${sysconfdir}/ssh/*.pub

    install -d ${D}/root/.ssh/
    install ${WORKDIR}/ssh-easyborn-key.pub ${D}/root/.ssh/authorized_keys
    chmod 0644 ${D}/root/.ssh/authorized_keys
}

FILES:${PN} += "${sysconfdir}/ssh/* ${sysconfdir}/ssh/ssh_host_*_key* \
                 ${sysconfdir}/ssh/ssh-easyborn-key.pub \
                 /root/.ssh/authorized_keys"