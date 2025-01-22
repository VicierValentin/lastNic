#!/bin/sh

systemctl stop psplash-systemd.service
echo 0 > /sys/class/graphics/fbcon/cursor_blink
