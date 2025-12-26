#include <iostream>
#include <string>
#include <unistd.h>
#include <cstring>
#include "usb_keyboard.h"
#include <fcntl.h>
#include <linux/input.h>
#include <log.h>

void printUsage(const char* programName) {
    std::cout << "Usage: " << programName << " [options]\n"
              << "\nOptions:\n"
              << "  -t <text>     Type the specified text\n"
              << "  -f <file>     Type the contents of a file\n"
              << "  -d <ms>       Set delay between keystrokes (default: 10ms)\n"
              << "  -i            Interactive mode - type from stdin\n"
              << "  -u <device>   Forward USB keyboard input (e.g., /dev/input/event0)\n"
              << "  -h            Show this help message\n"
              << "\nExamples:\n"
              << "  " << programName << " -t \"Bonjour le monde!\"\n"
              << "  " << programName << " -t \"Azerty français\" -d 20\n"
              << "  " << programName << " -f script.txt\n"
              << "  echo \"Hello\" | " << programName << " -i\n"
              << "  " << programName << " -u /dev/input/event0\n"
              << "\nSpecial sequences:\n"
              << "  \\n - Enter key\n"
              << "  \\t - Tab key\n"
              << std::endl;
}

bool typeFile(USBKeyboard& keyboard, const std::string& filename, unsigned int delay) {
    std::ifstream file(filename);
    if (!file.is_open()) {
        std::cerr << "Error: Cannot open file: " << filename << std::endl;
        return false;
    }
    
    std::string line;
    while (std::getline(file, line)) {
        keyboard.typeString(line + "\n", delay);
        usleep(50000); // 50ms delay between lines
    }
    
    file.close();
    return true;
}

bool interactiveMode(USBKeyboard& keyboard, unsigned int delay) {
    std::cout << "Interactive mode - type text (Ctrl+D to exit):" << std::endl;
    
    std::string line;
    while (std::getline(std::cin, line)) {
        keyboard.typeString(line + "\n", delay);
    }
    
    return true;
}

bool forwardFromUSBKeyboard(USBKeyboard& keyboard, const std::string& inputDevice) {
    std::cout << "Opening input device: " << inputDevice << std::endl;
    
    int fd = open(inputDevice.c_str(), O_RDONLY);
    if (fd < 0) {
        std::cerr << "Error: Cannot open input device: " << inputDevice << std::endl;
        std::cerr << "Make sure the device exists and you have read permissions" << std::endl;
        std::cerr << "Hint: You may need to run with sudo or add yourself to the 'input' group" << std::endl;
        return false;
    }
    
    // Get device name
    char name[256] = "Unknown";
    if (ioctl(fd, EVIOCGNAME(sizeof(name)), name) < 0) {
        std::cerr << "Warning: Cannot get device name" << std::endl;
    }
    std::cout << "Connected to: " << name << std::endl;
    std::cout << "Forwarding keyboard input to /dev/hidg0... (Press Ctrl+C to stop)" << std::endl;
    
    // Use non-blocking mode initially to set it up, but we'll use blocking reads
    struct input_event ev;
    uint8_t currentModifiers = 0;
    
    while (true) {
        ssize_t n = read(fd, &ev, sizeof(ev));
        if (n < 0) {
            std::cerr << "Error reading from input device" << std::endl;
            close(fd);
            return false;
        }
        
        if (n != sizeof(ev)) {
            continue;
        }
        
        // We only care about key events
        if (ev.type != EV_KEY) {
            continue;
        }
        
        // ev.value: 0 = release, 1 = press, 2 = repeat
        bool isPress = (ev.value == 1);
        bool isRelease = (ev.value == 0);
        
        if (!isPress && !isRelease) {
            continue; // Ignore repeat events
        }
        
        // Map Linux keycodes to HID scancodes
        // This is a basic mapping - modifier keys need special handling
        uint8_t scancode = ev.code; // Direct mapping works for many keys
        
        // Handle modifier keys
        if (ev.code == KEY_LEFTSHIFT || ev.code == KEY_RIGHTSHIFT) {
            if (isPress) currentModifiers |= 0x02;
            else currentModifiers &= ~0x02;
        }
        else if (ev.code == KEY_LEFTCTRL || ev.code == KEY_RIGHTCTRL) {
            if (isPress) currentModifiers |= 0x01;
            else currentModifiers &= ~0x01;
        }
        else if (ev.code == KEY_LEFTALT || ev.code == KEY_RIGHTALT) {
            if (isPress) currentModifiers |= 0x04;
            else currentModifiers &= ~0x04;
        }
        else if (ev.code == KEY_LEFTMETA || ev.code == KEY_RIGHTMETA) {
            if (isPress) currentModifiers |= 0x08;
            else currentModifiers &= ~0x08;
        }
        
        // For regular keys, forward them
        if (isPress && scancode != 0) {
            keyboard.pressKey(scancode, currentModifiers, 0);
        }
    }
    
    close(fd);
    return true;
}

int main(int argc, char* argv[]) {

    init_dlt("KB", LOG_INFO);
    std::string text;
    std::string filename;
    std::string usbInputDevice;
    unsigned int delay = 10; // Default delay in milliseconds
    bool interactive = false;
    
    // Parse command line arguments
    int opt;
    while ((opt = getopt(argc, argv, "t:f:d:u:ih")) != -1) {
        switch (opt) {
            case 't':
                text = optarg;
                break;
            case 'f':
                filename = optarg;
                break;
            case 'd':
                delay = std::atoi(optarg);
                if (delay < 1 || delay > 1000) {
                    log_error("Delay must be between 1 and 1000 ms");
                    return 1;
                }
                break;
            case 'i':
                interactive = true;
                break;
            case 'u':
                usbInputDevice = optarg;
                break;
            case 'h':
                printUsage(argv[0]);
                return 0;
            default:
                printUsage(argv[0]);
                return 1;
        }
    }
    
    // Check if at least one mode is specified
    if (text.empty() && filename.empty() && !interactive && usbInputDevice.empty()) {
        log_error("Error: Please specify text to type (-t), a file (-f), interactive mode (-i), or USB forwarding (-u)");
        printUsage(argv[0]);
        return 1;
    }
    
    // Initialize keyboard
    USBKeyboard keyboard;
    
    log_info("Opening USB HID device...");
    if (!keyboard.open()) {
        log_error("Cannot open USB HID device at /dev/hidg0");
        log_error("Make sure to run setup_usb_gadget.sh first (with sudo)");
        return 1;
    }
    
    log_info("USB Keyboard ready (French AZERTY layout)");
    log_info("Delay between keystrokes: %s", std::to_string(delay).c_str());
    
    bool success = true;
    
    // Execute the requested operation
    if (!text.empty()) {
        log_info("Typing text: %s", text.c_str());
        success = keyboard.typeString(text, delay);
    }
    else if (!filename.empty()) {
        log_info("Typing from file: %s", filename.c_str());
        success = typeFile(keyboard, filename, delay);
    }
    else if (interactive) {
        success = interactiveMode(keyboard, delay);
    }
    else if (!usbInputDevice.empty()) {
        success = forwardFromUSBKeyboard(keyboard, usbInputDevice);
    }
    
    keyboard.close();
    
    if (success) {
        log_info("Done!");
        return 0;
    } else {
        log_error("Operation failed!");
        return 1;
    }
}
