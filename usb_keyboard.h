#ifndef USB_KEYBOARD_H
#define USB_KEYBOARD_H

#include <string>
#include <cstdint>
#include <fstream>
#include <cstring>
#include <unistd.h>
#include "azerty_keymap.h"

/**
 * USB HID Keyboard controller for BeagleBone Black
 * Emulates a French AZERTY keyboard through USB gadget interface
 */
class USBKeyboard {
private:
    std::string devicePath;
    std::ofstream device;
    bool isOpen;
    
    // HID report structure (8 bytes)
    struct HIDReport {
        uint8_t modifiers;      // Byte 0: Modifier keys
        uint8_t reserved;       // Byte 1: Reserved (always 0)
        uint8_t keys[6];        // Bytes 2-7: Up to 6 simultaneous keys
        
        HIDReport() {
            memset(this, 0, sizeof(HIDReport));
        }
    };
    
    /**
     * Send a raw HID report to the USB device
     */
    bool sendReport(const HIDReport& report) {
        if (!isOpen) {
            return false;
        }
        
        device.write(reinterpret_cast<const char*>(&report), sizeof(HIDReport));
        device.flush();
        
        return device.good();
    }
    
    /**
     * Send an empty report (all keys released)
     */
    bool releaseAll() {
        HIDReport report;
        return sendReport(report);
    }

public:
    /**
     * Constructor
     * @param path Path to the HID device (default: /dev/hidg0)
     */
    USBKeyboard(const std::string& path = "/dev/hidg0") 
        : devicePath(path), isOpen(false) {}
    
    /**
     * Destructor - ensures device is properly closed
     */
    ~USBKeyboard() {
        close();
    }
    
    /**
     * Open the USB HID device
     * @return true if successful, false otherwise
     */
    bool open() {
        if (isOpen) {
            return true;
        }
        
        device.open(devicePath, std::ios::binary);
        isOpen = device.is_open();
        
        if (!isOpen) {
            return false;
        }
        
        // Send initial empty report
        releaseAll();
        return true;
    }
    
    /**
     * Close the USB HID device
     */
    void close() {
        if (isOpen) {
            releaseAll();
            device.close();
            isOpen = false;
        }
    }
    
    /**
     * Check if device is open
     */
    bool isDeviceOpen() const {
        return isOpen;
    }
    
    /**
     * Press and release a single key
     * @param scancode The HID scancode of the key
     * @param modifier Modifier keys (shift, ctrl, alt, etc.)
     * @param delay_ms Delay between press and release in milliseconds
     */
    bool pressKey(uint8_t scancode, uint8_t modifier = 0, unsigned int delay_ms = 10) {
        if (!isOpen || scancode == HID::KEY_NONE) {
            return false;
        }
        
        // Press key
        HIDReport report;
        report.modifiers = modifier;
        report.keys[0] = scancode;
        
        if (!sendReport(report)) {
            return false;
        }
        
        // Small delay
        usleep(delay_ms * 1000);
        
        // Release key
        return releaseAll();
    }
    
    /**
     * Type a single character using French AZERTY layout
     * @param c The character to type
     * @param delay_ms Delay between press and release
     */
    bool typeChar(char c, unsigned int delay_ms = 10) {
        KeyInfo keyInfo = AzertyKeymap::getKeyInfo(c);
        
        if (keyInfo.scancode == HID::KEY_NONE) {
            // Unknown character - skip it
            return false;
        }
        
        return pressKey(keyInfo.scancode, keyInfo.modifier, delay_ms);
    }
    
    /**
     * Type a string using French AZERTY layout
     * @param text The text to type
     * @param delay_ms Delay between each character
     */
    bool typeString(const std::string& text, unsigned int delay_ms = 10) {
        if (!isOpen) {
            return false;
        }
        
        for (char c : text) {
            if (!typeChar(c, delay_ms)) {
                // Continue even if a character fails (might be unsupported)
            }
            // Additional delay between characters
            usleep(delay_ms * 1000);
        }
        
        return true;
    }
    
    /**
     * Press a special key (F1-F12, arrows, etc.)
     * @param scancode The HID scancode
     * @param delay_ms Delay between press and release
     */
    bool pressSpecialKey(uint8_t scancode, unsigned int delay_ms = 10) {
        return pressKey(scancode, 0, delay_ms);
    }
    
    /**
     * Send a keyboard shortcut (e.g., Ctrl+C)
     * @param scancode The key scancode
     * @param modifiers The modifier keys
     * @param delay_ms Delay between press and release
     */
    bool sendShortcut(uint8_t scancode, uint8_t modifiers, unsigned int delay_ms = 50) {
        return pressKey(scancode, modifiers, delay_ms);
    }
    
    /**
     * Type text with line ending
     * @param text The text to type
     * @param delay_ms Delay between characters
     */
    bool typeLine(const std::string& text, unsigned int delay_ms = 10) {
        return typeString(text + "\n", delay_ms);
    }
};

#endif // USB_KEYBOARD_H
