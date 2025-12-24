require recipes-core/images/core-image-base.bb

SUMMARY = "EasyBorn Lafon MasterDev"

inherit image-buildinfo

LICENSE_FLAGS_ACCEPTED += " commercial"

#IMAGE_FEATURES += "ssh-server-dropbear"
IMAGE_FEATURES += "ssh-server-openssh"
# Misc
IMAGE_INSTALL:append = " \
	openssh-sftp-server \
	libatomic \
	coreutils \
	kernel-modules \
	pulse-node \
	acpid \
	iptables \
	curl \
	sqlite3 \
	i2c-tools \
	parted \
	e2fsprogs-e2fsck \
	e2fsprogs-resize2fs \
	e2fsprogs-mke2fs \
	gdbserver \
	util-linux \
	psmisc \
	evtest \
	libgpiod \
	libgpiod-tools \
	bmap-tools \
	dlt-daemon \
	dlt-daemon-systemd \
	openssl \
"

# node modules and so on

# Lafon
IMAGE_INSTALL:append = " \
	lafon-growfs \
	sdcardboot \
	gestion-carte-sd \
	multilingue \
	lib-log \
	gestion-bdd \
	lv-drivers \
	lvgl \
	images-ihm \
	ihm \
	controleur \
	application \
	webserver \
	bmap-tools \
	ssh-key \
	journal \
	pde \
	ifsf-controleur \
	sha256 \
	cppzmq \
"
#TOOLCHAIN_TARGET_TASK:append = " gestion_bdd-staticdev lib-lon-staticdev"

# Kernel
IMAGE_INSTALL:append = " \
	kernel-image \
	kernel-devicetree \
	haveged \
	packagegroup-base \
	swupdate \
	libubootenv-bin \
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

export IMAGE_BASENAME = "easyborn-lafon-masterdev"
