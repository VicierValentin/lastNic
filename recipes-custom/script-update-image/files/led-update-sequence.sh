#!/bin/sh

TIME=0.1
LED="/sys/class/gpio/gpio49"
echo 49 > /sys/class/gpio/export 2>>/dev/null
echo out > ${LED}/direction
update_sequence() {
    echo 0 > ${LED}/value 
    sleep ${TIME}
echo 1 > ${LED}/value
    sleep ${TIME}
echo 0 > ${LED}/value 
    sleep ${TIME}
}

while [ 1 -gt 0 ];do 
    update_sequence
done
