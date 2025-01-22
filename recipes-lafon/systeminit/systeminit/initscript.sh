#!/bin/sh

logger "################  start initscript  #####################"

# disable script before copying

systemctl disable initscript.service
systemctl disable blinkled.service


# copy files from sdCard to eMMC

#dd if=/dev/mmcblk0 of=/dev/mmcblk1
# /usr/sbin/parted -s /dev/mmcblk1 resizepart 4 100%
logger "################  initscript done  ######################"


logger "################  initscript disable  ###################"

#systemctl stop blinkled.service

#reboot now
