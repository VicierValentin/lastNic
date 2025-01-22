#!/bin/bash
ONE="/sys/class/leds/beaglebone:green:heartbeat"
TWO="/sys/class/leds/beaglebone:green:mmc0"
THREE="/sys/class/leds/beaglebone:green:usr2"
FOUR="/sys/class/leds/beaglebone:green:usr3"

echo "création du répertoire de montage pour update sys"
mkdir /mnt/saveOldInstall 2> /dev/null
echo "Montage de la partition root du système à updater dans /mnt/saveOldInstall"
mount /dev/mmcblk1p2 /mnt/saveOldInstall 2> /dev/null
echo "Création d'un répertoire de sauvegarde des bdds"
mkdir /home/root/saveBDD 2> /dev/null
echo "Copie des bdd dans le rép"
cp /mnt/saveOldInstall/root/OCPP/*db /home/root/saveBDD/ 
echo "Démontage du système de fichier"
sleep 2
umount /mnt/saveOldInstall/
sleep 2
echo "recopie de la nouvelle image"
systemctl start blinkled.service
dd if=/usr/share/base-image-lafon-beaglebone-lafon.rootfs.wic of=/dev/mmcblk1 bs=512k
if [ $? ];then
    echo "recopie des bdd dans la nouvelle installation"
    mount /dev/mmcblk1p2 /mnt/saveOldInstall/
    cp /home/root/saveBDD/*.db /mnt/saveOldInstall/root/OCPP/
    umount /mnt/saveOldInstall/
    mount /dev/mmcblk1p3 /mnt/saveOldInstall/
    cp /home/root/saveBDD/*.db /mnt/saveOldInstall/root/OCPP/
    umount /mnt/saveOldInstall/
    mount /dev/mmcblk1p4 /mnt/saveOldInstall/
    cp /home/root/saveBDD/*.db /mnt/saveOldInstall/data/
    umount /mnt/saveOldInstall/
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

