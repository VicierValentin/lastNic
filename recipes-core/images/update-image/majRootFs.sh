#!/bin/sh
	cp /boot/extlinux/extlinux.conf /home/root/save_extlinux.conf
	nbOccurence=$(grep "\-03" /boot/extlinux/extlinux.conf | wc -l)
	echo $nbOccurence
	if [ $nbOccurence -eq 1 ]
	then
		echo "modification de partition rootfs 03 => 02"
		sed -i 's/-03/-02/' /boot/extlinux/extlinux.conf
		echo "modification ok"
		exit 0
	else
		echo "modification de partition rootfs 02 => 03"
		sed -i 's/-02/-03/' /boot/extlinux/extlinux.conf
		echo "modification ok"
		exit 0
	fi
	echo "probleme de modification partition"
	exit 1

