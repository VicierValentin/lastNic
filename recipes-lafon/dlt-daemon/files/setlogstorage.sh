#!/bin/bash

SD_LOGS_PATH="/media/sd/logs"
LOGS_PATH="/data/logs"

check_sd_logstorage() {
    # Check if SD card is mounted at /media/sd
    if ls /media/sd; then
        # SD card is mounted
        return 0  # Success
    else
        echo "ERROR: SD card is not mounted."
        return 1
    fi
}

set_logstorage() {
    check_sd_logstorage
    if [[ $? -eq 0 ]]; then
        mkdir -p "$SD_LOGS_PATH"
        cp /data/logs/dlt_logstorage.conf "$SD_LOGS_PATH"
        dlt-logstorage-ctrl -c 0 -p "$LOGS_PATH"
        dlt-logstorage-ctrl -c 1 -p "$SD_LOGS_PATH"
        echo "INFO: Log storage set to SD card"
    else
        dlt-logstorage-ctrl -c 0 -p "$SD_LOGS_PATH"
        dlt-logstorage-ctrl -c 1 -p "$LOGS_PATH"
        echo "INFO: Log storage set to internal storage"
    fi
}