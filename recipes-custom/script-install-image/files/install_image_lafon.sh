#!/bin/sh
ONE="/sys/class/leds/beaglebone:green:heartbeat"
TWO="/sys/class/leds/beaglebone:green:mmc0"
THREE="/sys/class/leds/beaglebone:green:usr2"
FOUR="/sys/class/leds/beaglebone:green:usr3"

systemctl start blinkled.service
echo "deuxieme etape"
dd if=/usr/share/base-image-lafon-beaglebone-lafon.rootfs.wic of=/dev/mmcblk1 bs=512k
if [ $? = 0 ];then
	echo ok
	systemctl stop blinkled.service
	echo gpio > ${ONE}/trigger
	echo gpio > ${TWO}/trigger
	echo gpio > ${THREE}/trigger
	echo gpio > ${FOUR}/trigger
	echo 1 > ${ONE}/brightness
	echo 1 > ${TWO}/brightness
	echo 1 > ${THREE}/brightness
	echo 1 > ${FOUR}/brightness
	halt
else 
	echo no
	systemctl stop blinkled.service
	echo 1 > ${ONE}/brightness
	echo 1 > ${TWO}/brightness
fi

