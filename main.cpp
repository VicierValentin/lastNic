#include <iostream>
#include <string>
#include <unistd.h>
#include <cstring>
#include "usb_keyboard.h"

void printUsage(const char* programName) {
    std::cout << "Usage: " << programName << " [options]\n"
              << "\nOptions:\n"
              << "  -t <text>     Type the specified text\n"
              << "  -f <file>     Type the contents of a file\n"
              << "  -d <ms>       Set delay between keystrokes (default: 10ms)\n"
              << "  -i            Interactive mode - type from stdin\n"
              << "  -h            Show this help message\n"
              << "\nExamples:\n"
              << "  " << programName << " -t \"Bonjour le monde!\"\n"
              << "  " << programName << " -t \"Azerty français\" -d 20\n"
              << "  " << programName << " -f script.txt\n"
              << "  echo \"Hello\" | " << programName << " -i\n"
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

int main(int argc, char* argv[]) {
    std::string text;
    std::string filename;
    unsigned int delay = 10; // Default delay in milliseconds
    bool interactive = false;
    
    // Parse command line arguments
    int opt;
    while ((opt = getopt(argc, argv, "t:f:d:ih")) != -1) {
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
                    std::cerr << "Error: Delay must be between 1 and 1000 ms" << std::endl;
                    return 1;
                }
                break;
            case 'i':
                interactive = true;
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
    if (text.empty() && filename.empty() && !interactive) {
        std::cerr << "Error: Please specify text to type (-t), a file (-f), or interactive mode (-i)\n" << std::endl;
        printUsage(argv[0]);
        return 1;
    }
    
    // Initialize keyboard
    USBKeyboard keyboard;
    
    std::cout << "Opening USB HID device..." << std::endl;
    if (!keyboard.open()) {
        std::cerr << "Error: Cannot open USB HID device at /dev/hidg0" << std::endl;
        std::cerr << "Make sure to run setup_usb_gadget.sh first (with sudo)" << std::endl;
        return 1;
    }
    
    std::cout << "USB Keyboard ready (French AZERTY layout)" << std::endl;
    std::cout << "Delay between keystrokes: " << delay << "ms" << std::endl;
    
    bool success = true;
    
    // Execute the requested operation
    if (!text.empty()) {
        std::cout << "Typing text: " << text << std::endl;
        success = keyboard.typeString(text, delay);
    }
    else if (!filename.empty()) {
        std::cout << "Typing from file: " << filename << std::endl;
        success = typeFile(keyboard, filename, delay);
    }
    else if (interactive) {
        success = interactiveMode(keyboard, delay);
    }
    
    keyboard.close();
    
    if (success) {
        std::cout << "Done!" << std::endl;
        return 0;
    } else {
        std::cerr << "Operation failed!" << std::endl;
        return 1;
    }
}
