#!/bin/bash
ONE="/sys/class/leds/beaglebone:green:heartbeat"
TWO="/sys/class/leds/beaglebone:green:mmc0"
THREE="/sys/class/leds/beaglebone:green:usr2"
FOUR="/sys/class/leds/beaglebone:green:usr3"

/usr/bin/led-update-sequence update_sequence&
PIDLED=$(echo $!)
echo $PIDLED
echo "création du répertoire de montage pour update sys" >> /home/root/logInstall 2>?1
mkdir /mnt/saveOldInstall 2> /dev/null
echo "Montage de la partition root du système à updater dans /mnt/saveOldInstall" >> /home/root/logInstall 2>?1
mount /dev/mmcblk1p2 /mnt/saveOldInstall 2> /dev/null 
echo "Modification du routeur v7 => v8 + telnet" >> /home/root/logInstall 2>?1
/mnt/saveOldInstall/usr/bin/python2.7 /usr/bin/HTTP_test.py
echo "Création d'un répertoire de sauvegarde des bdds" >> /home/root/logInstall 2>?1
mkdir /home/root/saveBDD >> /home/root/logInstall 2>?1
echo "Copie des bdd dans le rép"
cp /mnt/saveOldInstall/root/OCPP/*db /home/root/saveBDD/
cp /mnt/saveOldInstall/root/OCPP/config.client /home/root/saveBDD/ 
cp /mnt/saveOldInstall/root/OCPP/config.exp /home/root/saveBDD/ 
cp /mnt/saveOldInstall/root/OCPP/config.sql /home/root/saveBDD/  
echo "Démontage du système de fichier"
sleep 2
umount /mnt/saveOldInstall/
sleep 2
echo "recopie de la nouvelle image"
systemctl start blinkled.service
/usr/bin/read
if [ $? -eq 2 ];then
	kill $PIDLED
	/usr/bin/led-update-sequence failed
	echo "Installation échoué read fichier .enc" >> /home/root/logInstall
	exit 3
fi
sleep 1
umount /dev/mmcblk1p?
sleep 1
dd if=/usr/share/base-image-lafon-beaglebone-lafon.rootfs.wic of=/dev/mmcblk1 bs=512k
if [ $? ];then
    echo "recopie des bdd dans la nouvelle installation"
    mount /dev/mmcblk1p2 /mnt/saveOldInstall/
    mkdir /mnt/saveOldInstall/root/update
	cp /home/root/saveBDD/config* /mnt/saveOldInstall/root/update
	#rm /mnt/saveOldInstall/data/config.csv
    cp /home/root/saveBDD/*.db /mnt/saveOldInstall/root/update
    cp /home/root/logInstall /mnt/saveOldInstall/root/update
    cp /usr/bin/terminal.hex /mnt/saveOldInstall/root/update
    touch /mnt/saveOldInstall/root/update/update.log
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
	umount /dev/mmcblk0*
	#echo 1 > ${ONE}/brightness
	echo 'type=83' | sfdisk --force /dev/mmcblk0
	#(echo Y) | mkfs -t vfat /dev/mmcblk0p1
	reboot
	#echo 1 > ${THREE}/brightness
	sleep 1
	halt
else 
	kill $PIDLED
	/usr/bin/led-update-sequence failed
	echo "Installation échoué dd failed" >> /home/root/logInstall
	systemctl stop blinkled.service
    echo 1 > ${ONE}/brightness
	echo 1 > ${TWO}/brightness
fi

