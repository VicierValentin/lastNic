SUMMARY = "Fichier de données partagé entre recettes"
LICENSE = "CLOSED"
SRC_URI = "file://private_key.pem"

do_install() {
    install -Dm644 ${WORKDIR}/private_key.pem ${D}${datadir}/private-key/private_key.pem
}

PROVIDES = "private-key"

do_compile[noexec] = "1"
do_configure[noexec] = "1"

# Ne rien installer dans l'image finale
FILES:${PN} = ""
INSANE_SKIP:${PN} = "ldflags installed-vs-shipped"