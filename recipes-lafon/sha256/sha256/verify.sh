#!/bin/bash

# List of applications
apps=(EB_Ihm EB_Ctrl EB_Jrnl)

for app in "${apps[@]}"; do
    if [[ -f "/root/easy/$app" ]]; then
        if openssl dgst -sha256 -verify /usr/appid/keys/key.pem -signature /usr/appid/sigs/$app /usr/appid/shas/yocto/$app; then
            # sha256sum /root/easy/$app" > "/usr/appid/shas/local/$app
            openssl dgst -sha256 -out /usr/appid/shas/local/$app /root/easy/$app
            awk -F'= ' '{print $2}' /usr/appid/shas/local/$app > /usr/appid/shas/local/tmp
            mv /usr/appid/shas/local/tmp /usr/appid/shas/local/$app

        else
            echo "Signature verification failed for $app."
        fi
    else
        echo "Warning: $app not found or is not a file."
    fi
done