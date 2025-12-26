# Makefile for BBB USB Keyboard
CXX = g++
CXXFLAGS = -std=c++11 -Wall -Wextra -O2
TARGET = usb_keyboard
SOURCES = main.cpp
HEADERS = usb_keyboard.h azerty_keymap.h

.PHONY: all clean install

all: $(TARGET)

$(TARGET): $(SOURCES) $(HEADERS)
	$(CXX) $(CXXFLAGS) $(SOURCES) -o $(TARGET)

clean:
	rm -f $(TARGET)

install: $(TARGET)
	install -m 755 $(TARGET) /usr/local/bin/
	install -m 755 setup_usb_gadget.sh /usr/local/bin/

uninstall:
	rm -f /usr/local/bin/$(TARGET)
	rm -f /usr/local/bin/setup_usb_gadget.sh
