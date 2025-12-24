# meta-magfleet

**Yocto/OpenEmbedded BSP layer for Lafon Technologies EasyBorn platform**

This layer provides support for building embedded Linux distributions for the EasyBorn Lafon system, targeting BeagleBone-based hardware with custom Lafon extensions.

## Overview

`meta-magfleet` is a Yocto Project BSP (Board Support Package) layer that contains:
- Custom machine configurations for BeagleBone Lafon variants
- Lafon Technologies custom distribution (distro) configuration
- Application recipes for the EasyBorn platform
- Secure update infrastructure using SWUpdate
- LON (Local Operating Network) bus support
- Custom U-Boot and kernel configurations

## Layer Information

- **Layer Name:** meta-magfleet
- **Layer Priority:** 17
- **Compatible Series:** Kirkstone (Yocto 4.0)
- **Maintainer:** Jocelyn Millard <jocelyn.millard@lafon.fr>

## Dependencies

This layer depends on the following layers:

- **poky** (Kirkstone) - Core Yocto Project reference distribution
- **meta-openembedded** (Kirkstone) - Additional OE recipes
  - meta-oe
  - meta-python
  - meta-networking
  - meta-perl
- **meta-ti** (Kirkstone) - Texas Instruments BSP layer
- **meta-arm** (Yocto 4.0) - ARM architecture support
- **meta-security** - Security hardening features
- **meta-swupdate** (Kirkstone) - Software update framework

## Prerequisites

### System Requirements

- Ubuntu 20.04 LTS or later (recommended)
- Minimum 50GB free disk space
- 8GB RAM (16GB recommended for parallel builds)

### Required Packages

```bash
sudo apt-get update
sudo apt-get install -y gawk wget git diffstat unzip texinfo gcc build-essential \
    chrpath socat cpio python3 python3-pip python3-pexpect xz-utils debianutils \
    iputils-ping python3-git python3-jinja2 libegl1-mesa libsdl1.2-dev \
    pylint3 xterm python3-subunit mesa-common-dev zstd liblz4-tool
```

### Install KAS Build Tool

```bash
pip3 install kas
```

## Building Images

This layer uses [KAS](https://kas.readthedocs.io/) for simplified build configuration management.

### Available Build Configurations

The layer provides several KAS configuration files in `kas-files/`:

| Configuration File | Target Machine | Distribution | Purpose |
|-------------------|----------------|--------------|---------|
| `kas-easy-lafon-master.yml` | beaglebone-lafon | lafon | Production master image |
| `kas-easy-lafon-master-dev.yml` | beaglebone-lafon | lafon | Development master image |
| `kas-easy-lafon-master-installer.yml` | beaglebone-lafon-installer | lafon-installer | Installation image |

### Build Instructions

1. **Clone the repository:**
   ```bash
   git clone git@github.com:MADIC-industries/meta-magfleet.git
   cd meta-magfleet
   ```

2. **Build the desired image using KAS:**
   
   For production master image:
   ```bash
   kas -d build kas-files/kas-easy-lafon-master.yml
   ```
   
   For development master image:
   ```bash
   kas -d build kas-files/kas-easy-lafon-master-dev.yml
   ```
   
   For installer image:
   ```bash
   kas -d build kas-files/kas-easy-lafon-master-dev-installer.yml
   ```

   For inotify error:
   ```bash
   sudo sysctl fs.inotify.max_user_watches=524288
   ```

3. **Build artifacts location:**
   
   Built images will be located in:
   ```
   kas-files/build/tmp/deploy/images/beaglebone-lafon/
   ```

### Build Output Artifacts

The build process generates several image formats:
- **`.wic`** - Bootable disk image for SD card
- **`.wic.bmap`** - Block map for efficient flashing with bmaptool
- **`.ext4.gz`** - Compressed root filesystem
- **`.tar.gz`** - Tarball of root filesystem
- **`.swu`** - SWUpdate package for OTA updates (from update-image)

## Machine Configurations

### beaglebone-lafon
Standard BeagleBone configuration with Lafon customizations for production deployment.

### beaglebone-lafon-installer
BeagleBone configuration optimized for installation and provisioning workflows.

## Distribution Features

The Lafon distribution (`lafon.conf`) includes:

- **Init System:** systemd
- **Update Mechanism:** SWUpdate with signed image support
- **Security Features:** From meta-security layer
- **Removed Features:** Bluetooth, ALSA, APM, RTC (minimal footprint)

## Recipes and Applications

### Core Applications (recipes-lafon/)

- **application** - Main Lafon application
- **controleur** - Controller component
- **ihm** - Human-Machine Interface (HMI)
- **webserver** - Web server component
- **systeminit** - System initialization scripts
- **lonifd-daemon** - LON interface daemon
- **dlt-daemon** - Diagnostic Log and Trace daemon
- **gestion-bdd** - Database management
- **gestion-carte-sd** - SD card management

### LON Bus Support

- **module-lon** - LON bus kernel module
- **lib-lon** - LON library
- **lonifd-daemon** - LON interface daemon

### Display and UI

- **lvgl** - LittlevGL graphics library
- **lv-drivers** - Display drivers for LVGL
- **images-ihm** - HMI image assets

### System Components

- **lafon-growfs** - Filesystem expansion utility
- **swupdate** - Software update client (with custom configuration)
- **private-key** - Secure key management
- **journal** - System journaling

## Flashing Images to SD Card

### Using bmaptool (Recommended)

```bash
sudo bmaptool copy --bmap easyborn-lafon-master-beaglebone-lafon.wic.bmap \
    easyborn-lafon-master-beaglebone-lafon.wic /dev/sdX
```

### Using dd

```bash
sudo dd if=easyborn-lafon-master-beaglebone-lafon.wic of=/dev/sdX bs=4M status=progress
sudo sync
```

Replace `/dev/sdX` with your SD card device.

## Software Updates

The system supports secure OTA updates using SWUpdate:

1. **Generate update package:**
   ```bash
   bitbake update-image
   ```

2. **Deploy the `.swu` file** to the target device

3. **Trigger update:**
   ```bash
   swupdate -i <update-package>.swu
   ```

Updates are cryptographically signed and verified before installation.

## Development Workflow

### Adding New Recipes

1. Create recipe directory: `recipes-<category>/<recipe-name>/`
2. Add recipe file: `<recipe-name>_<version>.bb`
3. Add to image by modifying `recipes-core/images/easyborn-lafon-*.bb`

### Customizing the Kernel

Kernel modifications are in `recipes-kernel/linux/`. Use `.bbappend` files to add patches or configuration fragments.

### Modifying U-Boot

U-Boot customizations are in `recipes-bsp/u-boot/` and `recipes-bsp/u-boot-pulse/`.

## Configuration Files

### Layer Configuration
- `conf/layer.conf` - Layer metadata and dependencies

### Distribution Configuration
- `conf/distro/lafon.conf` - Lafon distribution settings
- `conf/distro/lafon-installer.conf` - Installer distribution settings

### Machine Configuration
- `conf/machine/beaglebone-lafon.conf` - Production machine settings
- `conf/machine/beaglebone-lafon-installer.conf` - Installer machine settings

### Site Configuration
- `conf/site.conf` - Site-specific settings (git-ignored, create locally if needed)

## WIC Image Layouts

Custom WIC kickstart files define disk partitioning:
- `wic/easyborn.wks` - Standard dual-partition layout for A/B updates
- `wic/easybornInstall.wks` - Installation media layout
- `wic/easyborn (MID).wks` - Alternative layout variant

## Troubleshooting

### Build Failures

1. **Check disk space:** Builds require significant space
   ```bash
   df -h
   ```

2. **Clean temporary files:**
   ```bash
   kas shell kas-files/kas-easy-lafon-master-dev.yml -c "bitbake -c cleanall <recipe-name>"
   ```

3. **Full rebuild:**
   ```bash
   rm -rf kas-files/build/tmp
   kas build kas-files/kas-easy-lafon-master-dev.yml
   ```

### Common Issues

- **Git SSL errors:** Check proxy settings and certificates
- **Fetch failures:** Verify network connectivity and mirror availability
- **Package conflicts:** Check layer priorities and BBMASK settings

## Directory Structure

```
meta-magfleet/
├── conf/                      # Configuration files
│   ├── layer.conf            # Layer configuration
│   ├── distro/               # Distribution configs
│   └── machine/              # Machine configs
├── kas-files/                # KAS build configurations
├── recipes-bsp/              # Bootloader and firmware
├── recipes-connectivity/     # Network and SSH
├── recipes-core/             # Core system recipes
│   └── images/              # Image definitions
├── recipes-kernel/           # Kernel recipes
├── recipes-lafon/            # Lafon-specific applications
├── recipes-support/          # Support packages (SWUpdate)
└── wic/                      # Disk layout definitions
```

## Contributing

### Coding Standards

- Follow Yocto Project coding guidelines
- Use 4 spaces for indentation in recipes
- Add SPDX license identifiers to new files
- Document recipe variables and purpose

### Submitting Changes

1. Create a feature branch from `dev`
2. Make changes and test builds
3. Commit with descriptive messages
4. Create merge request targeting `dev` branch

### Testing

Test changes by building all image variants:
```bash
kas build kas-files/kas-easy-lafon-master.yml
kas build kas-files/kas-easy-lafon-master-dev.yml
kas build kas-files/kas-easy-lafon-master-installer.yml
```

## License

This layer is provided under the MIT License. See individual recipe files for component-specific licenses.

## Maintainer

**Jocelyn Millard**  
Email: jocelyn.millard@lafon.fr  
Organization: Lafon Technologies

## Additional Resources

- [Yocto Project Documentation](https://docs.yoctoproject.org/)
- [OpenEmbedded Layer Index](https://layers.openembedded.org/)
- [KAS Documentation](https://kas.readthedocs.io/)
- [SWUpdate Documentation](https://sbabic.github.io/swupdate/)

## Project Status

Active development. Current version: 3.0 (Kirkstone-based)

