PACKAGECONFIG:append = "networkd resolved"
PACKAGECONFIG:remove = "logind"
PACKAGECONFIG:remove = "timesyncd"

do_install:append() {
   # Disable getty@tty1 from starting at boot time.
    sed -i -e "s/enable getty@.service/disable getty@.service/g" ${D}${systemd_unitdir}/system-preset/90-systemd.preset
   
   #disabling network stuff
   #sed -i -e "s/enable systemd-networkd.service/disable systemd-networkd.service/g" ${D}${systemd_unitdir}/system-preset/90-systemd.preset
   #sed -i -e "s/enable systemd-resolved.service/disable systemd-resolved.service/g" ${D}${systemd_unitdir}/system-preset/90-systemd.preset
   
   #disabling unwanted services
    echo "disable busybox-klogd.service" >> ${D}${systemd_unitdir}/system-preset/80-systemd.preset
    echo "disable busybox-syslog.service" >> ${D}${systemd_unitdir}/system-preset/80-systemd.preset
    echo "disable avahi-daemon.service" >> ${D}${systemd_unitdir}/system-preset/80-systemd.preset
    echo "disable ofono.service" >> ${D}${systemd_unitdir}/system-preset/80-systemd.preset

}
