FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append_lafon-installer = " file://fstab"
SRC_URI += "file://001-add-date-to-PS1-in-profile.patch"
dirs755:append = " /data"
dirs755:append = " /transac"
hostname = "easy"

