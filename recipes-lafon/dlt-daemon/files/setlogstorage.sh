#!/bin/bash

SD_LOGS_PATH="/media/sd/logs"
LOGS_PATH="/data/logs"

check_sd_logstorage() {
    if [[ -d "$SD_LOGS_PATH" ]]; then
        if [[ -r "$SD_LOGS_PATH" ]]; then
            echo "INFO: SD card is mounted and writable."
            return 0  # Success
        else
            echo "ERROR: SD card is mounted but not writable."
            return 1
        fi
    else
        echo "ERROR: SD card is not mounted."
        return 1
    fi
}

set_logstorage() {
    check_sd_logstorage
    if [[ $? -eq 0 ]]; then
        cp /data/logs/dlt_logstorage.conf "$SD_LOGS_PATH"
        mkdir -p "$SD_LOGS_PATH"
        dlt-logstorage-ctrl -c 1 -p "$SD_LOGS_PATH"
        dlt-logstorage-ctrl -c 0 -p "$LOGS_PATH"
        echo "INFO: Log storage set to SD card"
    else
        dlt-logstorage-ctrl -c 1 -p "$LOGS_PATH"
        dlt-logstorage-ctrl -c 0 -p "$SD_LOGS_PATH"
        echo "INFO: Log storage set to internal storage"
    fi
}