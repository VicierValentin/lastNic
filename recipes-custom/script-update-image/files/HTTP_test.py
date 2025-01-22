import requests
from time import sleep
import sys

user = 'lafon'
password = 'lafon'
payloadTelnet = {'submit_button':'Services','action':'ApplyTake','telnetd_enable':'1'}
payloadVersion = {'submit_button':'ForwardSpec','action':'ApplyTake','forward_spec':'13','name0':'version 8', 'pro0': 'both' , 'src0' : "", 'from0' : '0', 'ip0' : '0.0.0.0', 'to0' : '0'}
nb_retry = 0

def configToV8():
    global nb_retry
    try:
        rT = requests.post('http://192.168.1.1:8080/applyuser.cgi', auth=(user, password), data = payloadTelnet)
        if rT.status_code != 200:
            print("err = " + rT.status_code)

        rV = requests.post('http://192.168.1.1:8080/applyuser.cgi', auth=(user, password), data = payloadVersion)
        if rV.status_code != 200:
            print("err = " + rV.status_code)
    except OSError:
        print("Unable to reach router")
        return False

    if rV.status_code == 200 and rT.status_code == 200:
        print("Router config done to V8")
        return True
    else:
        print("Router config failed with codes : " + str(rT.status_code) + " for Telnet and " + str(rV.status_code) + "for Version")
        return False

def routerVersion():
    global nb_retry
    try:
        r = requests.get('http://192.168.1.1:8080/ForwardSpec.asp', auth=(user, password))

        for line in r.text.split(">"):
            if line.__contains__("name0"):
                for word in line.split("\""):
                    if word.__contains__("version"):
                        return str(word)
    except OSError:
        print("Unable to reach router")
        sleep(10)
        nb_retry = nb_retry+1
        if nb_retry > 5:
            sys.exit("Connection failed too many times, script stopped running")
        string = routerVersion()
        return string

text,val = routerVersion().split()
if 'version' in text and int(val) >= 8:
    print("Router already in V8 or higher")
else:
    while configToV8() is False:
        sleep(5)
        nb_retry += 1
        if nb_retry > 5:
            sys.exit("Connection failed too many times, script stopped running")
