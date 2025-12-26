#ifndef AZERTY_KEYMAP_H
#define AZERTY_KEYMAP_H

#include <map>
#include <string>

// USB HID Keyboard scan codes
namespace HID {
    // Modifier keys
    const uint8_t MOD_LEFT_CTRL = 0x01;
    const uint8_t MOD_LEFT_SHIFT = 0x02;
    const uint8_t MOD_LEFT_ALT = 0x04;
    const uint8_t MOD_LEFT_GUI = 0x08;
    const uint8_t MOD_RIGHT_CTRL = 0x10;
    const uint8_t MOD_RIGHT_SHIFT = 0x20;
    const uint8_t MOD_RIGHT_ALT = 0x40;
    const uint8_t MOD_RIGHT_GUI = 0x80;

    // Key scan codes
    const uint8_t KEY_NONE = 0x00;
    const uint8_t KEY_A = 0x04;
    const uint8_t KEY_B = 0x05;
    const uint8_t KEY_C = 0x06;
    const uint8_t KEY_D = 0x07;
    const uint8_t KEY_E = 0x08;
    const uint8_t KEY_F = 0x09;
    const uint8_t KEY_G = 0x0A;
    const uint8_t KEY_H = 0x0B;
    const uint8_t KEY_I = 0x0C;
    const uint8_t KEY_J = 0x0D;
    const uint8_t KEY_K = 0x0E;
    const uint8_t KEY_L = 0x0F;
    const uint8_t KEY_M = 0x10;
    const uint8_t KEY_N = 0x11;
    const uint8_t KEY_O = 0x12;
    const uint8_t KEY_P = 0x13;
    const uint8_t KEY_Q = 0x14;
    const uint8_t KEY_R = 0x15;
    const uint8_t KEY_S = 0x16;
    const uint8_t KEY_T = 0x17;
    const uint8_t KEY_U = 0x18;
    const uint8_t KEY_V = 0x19;
    const uint8_t KEY_W = 0x1A;
    const uint8_t KEY_X = 0x1B;
    const uint8_t KEY_Y = 0x1C;
    const uint8_t KEY_Z = 0x1D;
    
    const uint8_t KEY_1 = 0x1E;
    const uint8_t KEY_2 = 0x1F;
    const uint8_t KEY_3 = 0x20;
    const uint8_t KEY_4 = 0x21;
    const uint8_t KEY_5 = 0x22;
    const uint8_t KEY_6 = 0x23;
    const uint8_t KEY_7 = 0x24;
    const uint8_t KEY_8 = 0x25;
    const uint8_t KEY_9 = 0x26;
    const uint8_t KEY_0 = 0x27;
    
    const uint8_t KEY_ENTER = 0x28;
    const uint8_t KEY_ESC = 0x29;
    const uint8_t KEY_BACKSPACE = 0x2A;
    const uint8_t KEY_TAB = 0x2B;
    const uint8_t KEY_SPACE = 0x2C;
    const uint8_t KEY_MINUS = 0x2D;
    const uint8_t KEY_EQUAL = 0x2E;
    const uint8_t KEY_LEFT_BRACE = 0x2F;
    const uint8_t KEY_RIGHT_BRACE = 0x30;
    const uint8_t KEY_BACKSLASH = 0x31;
    const uint8_t KEY_SEMICOLON = 0x33;
    const uint8_t KEY_APOSTROPHE = 0x34;
    const uint8_t KEY_GRAVE = 0x35;
    const uint8_t KEY_COMMA = 0x36;
    const uint8_t KEY_DOT = 0x37;
    const uint8_t KEY_SLASH = 0x38;
    const uint8_t KEY_CAPS_LOCK = 0x39;
    
    const uint8_t KEY_F1 = 0x3A;
    const uint8_t KEY_F2 = 0x3B;
    const uint8_t KEY_F3 = 0x3C;
    const uint8_t KEY_F4 = 0x3D;
    const uint8_t KEY_F5 = 0x3E;
    const uint8_t KEY_F6 = 0x3F;
    const uint8_t KEY_F7 = 0x40;
    const uint8_t KEY_F8 = 0x41;
    const uint8_t KEY_F9 = 0x42;
    const uint8_t KEY_F10 = 0x43;
    const uint8_t KEY_F11 = 0x44;
    const uint8_t KEY_F12 = 0x45;
    
    const uint8_t KEY_INSERT = 0x49;
    const uint8_t KEY_HOME = 0x4A;
    const uint8_t KEY_PAGE_UP = 0x4B;
    const uint8_t KEY_DELETE = 0x4C;
    const uint8_t KEY_END = 0x4D;
    const uint8_t KEY_PAGE_DOWN = 0x4E;
    const uint8_t KEY_RIGHT = 0x4F;
    const uint8_t KEY_LEFT = 0x50;
    const uint8_t KEY_DOWN = 0x51;
    const uint8_t KEY_UP = 0x52;
    
    const uint8_t KEY_NON_US_BACKSLASH = 0x64; // <> key on French AZERTY
}

// Structure to hold key information
struct KeyInfo {
    uint8_t scancode;
    uint8_t modifier;
    
    KeyInfo(uint8_t sc = 0, uint8_t mod = 0) : scancode(sc), modifier(mod) {}
};

// French AZERTY keyboard mapping
// Maps characters to their corresponding scan codes and modifiers
class AzertyKeymap {
public:
    static const std::map<char, KeyInfo> charMap;
    
    static KeyInfo getKeyInfo(char c) {
        auto it = charMap.find(c);
        if (it != charMap.end()) {
            return it->second;
        }
        return KeyInfo(HID::KEY_NONE, 0);
    }
};

// French AZERTY layout mapping
const std::map<char, KeyInfo> AzertyKeymap::charMap = {
    // Lowercase letters - AZERTY layout
    {'a', KeyInfo(HID::KEY_Q, 0)},
    {'b', KeyInfo(HID::KEY_B, 0)},
    {'c', KeyInfo(HID::KEY_C, 0)},
    {'d', KeyInfo(HID::KEY_D, 0)},
    {'e', KeyInfo(HID::KEY_E, 0)},
    {'f', KeyInfo(HID::KEY_F, 0)},
    {'g', KeyInfo(HID::KEY_G, 0)},
    {'h', KeyInfo(HID::KEY_H, 0)},
    {'i', KeyInfo(HID::KEY_I, 0)},
    {'j', KeyInfo(HID::KEY_J, 0)},
    {'k', KeyInfo(HID::KEY_K, 0)},
    {'l', KeyInfo(HID::KEY_L, 0)},
    {'m', KeyInfo(HID::KEY_SEMICOLON, 0)},  // M is on semicolon key
    {'n', KeyInfo(HID::KEY_N, 0)},
    {'o', KeyInfo(HID::KEY_O, 0)},
    {'p', KeyInfo(HID::KEY_P, 0)},
    {'q', KeyInfo(HID::KEY_A, 0)},          // Q is on A key
    {'r', KeyInfo(HID::KEY_R, 0)},
    {'s', KeyInfo(HID::KEY_S, 0)},
    {'t', KeyInfo(HID::KEY_T, 0)},
    {'u', KeyInfo(HID::KEY_U, 0)},
    {'v', KeyInfo(HID::KEY_V, 0)},
    {'w', KeyInfo(HID::KEY_Z, 0)},          // W is on Z key
    {'x', KeyInfo(HID::KEY_X, 0)},
    {'y', KeyInfo(HID::KEY_Y, 0)},
    {'z', KeyInfo(HID::KEY_W, 0)},          // Z is on W key
    
    // Uppercase letters
    {'A', KeyInfo(HID::KEY_Q, HID::MOD_LEFT_SHIFT)},
    {'B', KeyInfo(HID::KEY_B, HID::MOD_LEFT_SHIFT)},
    {'C', KeyInfo(HID::KEY_C, HID::MOD_LEFT_SHIFT)},
    {'D', KeyInfo(HID::KEY_D, HID::MOD_LEFT_SHIFT)},
    {'E', KeyInfo(HID::KEY_E, HID::MOD_LEFT_SHIFT)},
    {'F', KeyInfo(HID::KEY_F, HID::MOD_LEFT_SHIFT)},
    {'G', KeyInfo(HID::KEY_G, HID::MOD_LEFT_SHIFT)},
    {'H', KeyInfo(HID::KEY_H, HID::MOD_LEFT_SHIFT)},
    {'I', KeyInfo(HID::KEY_I, HID::MOD_LEFT_SHIFT)},
    {'J', KeyInfo(HID::KEY_J, HID::MOD_LEFT_SHIFT)},
    {'K', KeyInfo(HID::KEY_K, HID::MOD_LEFT_SHIFT)},
    {'L', KeyInfo(HID::KEY_L, HID::MOD_LEFT_SHIFT)},
    {'M', KeyInfo(HID::KEY_SEMICOLON, HID::MOD_LEFT_SHIFT)},
    {'N', KeyInfo(HID::KEY_N, HID::MOD_LEFT_SHIFT)},
    {'O', KeyInfo(HID::KEY_O, HID::MOD_LEFT_SHIFT)},
    {'P', KeyInfo(HID::KEY_P, HID::MOD_LEFT_SHIFT)},
    {'Q', KeyInfo(HID::KEY_A, HID::MOD_LEFT_SHIFT)},
    {'R', KeyInfo(HID::KEY_R, HID::MOD_LEFT_SHIFT)},
    {'S', KeyInfo(HID::KEY_S, HID::MOD_LEFT_SHIFT)},
    {'T', KeyInfo(HID::KEY_T, HID::MOD_LEFT_SHIFT)},
    {'U', KeyInfo(HID::KEY_U, HID::MOD_LEFT_SHIFT)},
    {'V', KeyInfo(HID::KEY_V, HID::MOD_LEFT_SHIFT)},
    {'W', KeyInfo(HID::KEY_Z, HID::MOD_LEFT_SHIFT)},
    {'X', KeyInfo(HID::KEY_X, HID::MOD_LEFT_SHIFT)},
    {'Y', KeyInfo(HID::KEY_Y, HID::MOD_LEFT_SHIFT)},
    {'Z', KeyInfo(HID::KEY_W, HID::MOD_LEFT_SHIFT)},
    
    // Numbers (AZERTY: need shift for numbers)
    {'&', KeyInfo(HID::KEY_1, 0)},          // & without shift
    {'1', KeyInfo(HID::KEY_1, HID::MOD_LEFT_SHIFT)},
    {'é', KeyInfo(HID::KEY_2, 0)},          // é without shift
    {'2', KeyInfo(HID::KEY_2, HID::MOD_LEFT_SHIFT)},
    {'"', KeyInfo(HID::KEY_3, 0)},          // " without shift
    {'3', KeyInfo(HID::KEY_3, HID::MOD_LEFT_SHIFT)},
    {'\'', KeyInfo(HID::KEY_4, 0)},         // ' without shift
    {'4', KeyInfo(HID::KEY_4, HID::MOD_LEFT_SHIFT)},
    {'(', KeyInfo(HID::KEY_5, 0)},          // ( without shift
    {'5', KeyInfo(HID::KEY_5, HID::MOD_LEFT_SHIFT)},
    {'-', KeyInfo(HID::KEY_6, 0)},          // - without shift
    {'6', KeyInfo(HID::KEY_6, HID::MOD_LEFT_SHIFT)},
    {'è', KeyInfo(HID::KEY_7, 0)},          // è without shift
    {'7', KeyInfo(HID::KEY_7, HID::MOD_LEFT_SHIFT)},
    {'_', KeyInfo(HID::KEY_8, 0)},          // _ without shift
    {'8', KeyInfo(HID::KEY_8, HID::MOD_LEFT_SHIFT)},
    {'ç', KeyInfo(HID::KEY_9, 0)},          // ç without shift
    {'9', KeyInfo(HID::KEY_9, HID::MOD_LEFT_SHIFT)},
    {'à', KeyInfo(HID::KEY_0, 0)},          // à without shift
    {'0', KeyInfo(HID::KEY_0, HID::MOD_LEFT_SHIFT)},
    
    // Special characters
    {' ', KeyInfo(HID::KEY_SPACE, 0)},
    {'\n', KeyInfo(HID::KEY_ENTER, 0)},
    {'\t', KeyInfo(HID::KEY_TAB, 0)},
    
    {')', KeyInfo(HID::KEY_MINUS, 0)},      // ) key
    {'°', KeyInfo(HID::KEY_MINUS, HID::MOD_LEFT_SHIFT)},
    {'=', KeyInfo(HID::KEY_EQUAL, 0)},      // = key
    {'+', KeyInfo(HID::KEY_EQUAL, HID::MOD_LEFT_SHIFT)},
    
    {'^', KeyInfo(HID::KEY_LEFT_BRACE, 0)},  // ^ key (dead key)
    {'¨', KeyInfo(HID::KEY_LEFT_BRACE, HID::MOD_LEFT_SHIFT)},
    {'$', KeyInfo(HID::KEY_RIGHT_BRACE, 0)}, // $ key
    {'£', KeyInfo(HID::KEY_RIGHT_BRACE, HID::MOD_LEFT_SHIFT)},
    {'*', KeyInfo(HID::KEY_BACKSLASH, 0)},   // * key
    {'µ', KeyInfo(HID::KEY_BACKSLASH, HID::MOD_LEFT_SHIFT)},
    
    {'ù', KeyInfo(HID::KEY_APOSTROPHE, 0)},  // ù key
    {'%', KeyInfo(HID::KEY_APOSTROPHE, HID::MOD_LEFT_SHIFT)},
    
    {',', KeyInfo(HID::KEY_M, 0)},           // , is on M key
    {'?', KeyInfo(HID::KEY_M, HID::MOD_LEFT_SHIFT)},
    {';', KeyInfo(HID::KEY_COMMA, 0)},       // ; is on comma key
    {'.', KeyInfo(HID::KEY_COMMA, HID::MOD_LEFT_SHIFT)},
    {':', KeyInfo(HID::KEY_DOT, 0)},         // : is on dot key
    {'/', KeyInfo(HID::KEY_DOT, HID::MOD_LEFT_SHIFT)},
    {'!', KeyInfo(HID::KEY_SLASH, 0)},       // ! is on slash key
    {'§', KeyInfo(HID::KEY_SLASH, HID::MOD_LEFT_SHIFT)},
    
    {'<', KeyInfo(HID::KEY_NON_US_BACKSLASH, 0)},
    {'>', KeyInfo(HID::KEY_NON_US_BACKSLASH, HID::MOD_LEFT_SHIFT)},
};

#endif // AZERTY_KEYMAP_H
