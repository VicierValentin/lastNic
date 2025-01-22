require recipes-core/images/core-image-base.bb

SUMMARY = "Pulse Lafon Installer(Test)"

inherit image-buildinfo

#IMAGE_FEATURES:append = " \
#	ssh-server-dropbear \
#"

IMAGE_FEATURES:remove = "splash"
# Misc
IMAGE_INSTALL:append = " \
	util-linux \
    sdcardboot \
	store-image-lafon \
	script-install-image \
    bmap-tools \
"


# Lafon
IMAGE_INSTALL:append = " \
	systeminit \
"

# Kernel
IMAGE_INSTALL:append = " \
	kernel-image \
	kernel-devicetree \
	haveged \
	packagegroup-base \
"

IMAGE_BUILDINFO_VARS = " \
    BB_VERSION \
    BUILD_SYS  \
    NATIVELSBSTRING \
    TARGET_SYS \
    MACHINE \
    DISTRO \
    DISTRO_VERSION \
    TUNE_FEATURES \
    TARGET_FPU \
"

buildinfo () {
cat > ${IMAGE_ROOTFS}${sysconfdir}/${MACHINE}-build-info << END
Build Configuration:
${@buildinfo_target(d)}
${@get_layer_revs(d)}
END
}	

export IMAGE_BASENAME = "${IMAGE_EASY_NAME}-installer"
