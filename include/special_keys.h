#ifndef SPECIAL_KEYS_H
#define SPECIAL_KEYS_H

/**
 * special_keys.h — Named special key → HID keycode mapping
 *
 * Used to parse escape sequences of the form <KEYNAME> in input text files.
 * Key names are case-insensitive.
 *
 * Modifier combinations are expressed with '+':
 *   <CTRL+C>   <SHIFT+F1>   <ALT+TAB>   <CTRL+ALT+DEL>
 *
 * Examples in a text file:
 *   Hello<ENTER>
 *   <CTRL+A>
 *   <F5>
 *   <WIN+R>calc<ENTER>
 *   <SHIFT+TAB>
 */

#include <stdint.h>
#include <string.h>
#include "keymap.h"   /* for HID_MOD_* defines and hid_key_t */

/* -------------------------------------------------------------------------
 * Named key table
 * ---------------------------------------------------------------------- */
typedef struct {
    const char *name;     /* tag name, upper-case, without modifiers */
    uint8_t     keycode;  /* HID usage ID */
} named_key_t;

static const named_key_t named_keys[] = {
    /* --- Control / whitespace --- */
    { "ENTER",       0x28 },
    { "RETURN",      0x28 },
    { "ESC",         0x29 },
    { "ESCAPE",      0x29 },
    { "BACKSPACE",   0x2A },
    { "BS",          0x2A },
    { "TAB",         0x2B },
    { "SPACE",       0x2C },
    { "CAPSLOCK",    0x39 },
    { "CAPS",        0x39 },
    /* --- Function keys --- */
    { "F1",          0x3A },
    { "F2",          0x3B },
    { "F3",          0x3C },
    { "F4",          0x3D },
    { "F5",          0x3E },
    { "F6",          0x3F },
    { "F7",          0x40 },
    { "F8",          0x41 },
    { "F9",          0x42 },
    { "F10",         0x43 },
    { "F11",         0x44 },
    { "F12",         0x45 },
    /* --- Navigation --- */
    { "PRTSCR",      0x46 },
    { "PRINTSCREEN", 0x46 },
    { "SCROLLLOCK",  0x47 },
    { "PAUSE",       0x48 },
    { "INSERT",      0x49 },
    { "INS",         0x49 },
    { "HOME",        0x4A },
    { "PAGEUP",      0x4B },
    { "PGUP",        0x4B },
    { "DELETE",      0x4C },
    { "DEL",         0x4C },
    { "END",         0x4D },
    { "PAGEDOWN",    0x4E },
    { "PGDN",        0x4E },
    { "RIGHT",       0x4F },
    { "LEFT",        0x50 },
    { "DOWN",        0x51 },
    { "UP",          0x52 },
    { "NUMLOCK",     0x53 },
    /* --- Numpad --- */
    { "KP_DIVIDE",   0x54 },
    { "KP_MULTIPLY", 0x55 },
    { "KP_MINUS",    0x56 },
    { "KP_PLUS",     0x57 },
    { "KP_ENTER",    0x58 },
    { "KP_1",        0x59 },
    { "KP_2",        0x5A },
    { "KP_3",        0x5B },
    { "KP_4",        0x5C },
    { "KP_5",        0x5D },
    { "KP_6",        0x5E },
    { "KP_7",        0x5F },
    { "KP_8",        0x60 },
    { "KP_9",        0x61 },
    { "KP_0",        0x62 },
    { "KP_DOT",      0x63 },
    /* --- Application / GUI --- */
    { "APP",         0x65 },
    { "MENU",        0x65 },
    /* --- Modifier aliases (used standalone) --- */
    { "LCTRL",       0xE0 },
    { "LSHIFT",      0xE1 },
    { "LALT",        0xE2 },
    { "LWIN",        0xE3 },
    { "LGUI",        0xE3 },
    { "RCTRL",       0xE4 },
    { "RSHIFT",      0xE5 },
    { "RALT",        0xE6 },
    { "RWIN",        0xE7 },
    { "RGUI",        0xE7 },
    /* Sentinel */
    { NULL, 0x00 }
};

/* Modifier name → bitmask */
typedef struct {
    const char *name;
    uint8_t     bit;
} mod_alias_t;

static const mod_alias_t mod_aliases[] = {
    { "CTRL",  HID_MOD_LEFT_CTRL  },
    { "LCTRL", HID_MOD_LEFT_CTRL  },
    { "RCTRL", HID_MOD_RIGHT_CTRL },
    { "SHIFT", HID_MOD_LEFT_SHIFT },
    { "LSHIFT",HID_MOD_LEFT_SHIFT },
    { "RSHIFT",HID_MOD_RIGHT_SHIFT},
    { "ALT",   HID_MOD_LEFT_ALT   },
    { "LALT",  HID_MOD_LEFT_ALT   },
    { "RALT",  HID_MOD_RIGHT_ALT  },
    { "WIN",   HID_MOD_LEFT_GUI   },
    { "GUI",   HID_MOD_LEFT_GUI   },
    { "LWIN",  HID_MOD_LEFT_GUI   },
    { "LGUI",  HID_MOD_LEFT_GUI   },
    { "RWIN",  HID_MOD_RIGHT_GUI  },
    { "RGUI",  HID_MOD_RIGHT_GUI  },
    { NULL, 0 }
};

/* -------------------------------------------------------------------------
 * Helpers
 * ---------------------------------------------------------------------- */

/** Convert a string to upper-case in-place (ASCII only). */
static inline void str_toupper(char *s)
{
    for (; *s; s++)
        if (*s >= 'a' && *s <= 'z') *s = (char)(*s - 32);
}

/**
 * Look up a modifier token.
 * Returns the modifier bitmask if found, 0 otherwise.
 */
static inline uint8_t lookup_modifier(const char *token)
{
    for (int i = 0; mod_aliases[i].name != NULL; i++)
        if (strcmp(token, mod_aliases[i].name) == 0)
            return mod_aliases[i].bit;
    return 0;
}

/**
 * Look up a named key token.
 * Returns the HID keycode if found, 0 otherwise.
 * Also handles single printable ASCII characters (A-Z, 0-9, etc.)
 * after str_toupper() has been applied to the tag.
 */
static inline uint8_t lookup_named_key(const char *token, const hid_key_t *km)
{
    for (int i = 0; named_keys[i].name != NULL; i++)
        if (strcmp(token, named_keys[i].name) == 0)
            return named_keys[i].keycode;

    /* Single printable ASCII character — use the active keymap */
    if (token[1] == '\0') {
        unsigned char c = (unsigned char)token[0];
        /* str_toupper was applied, so A-Z → look up as lowercase */
        if (c >= 'A' && c <= 'Z')
            c = c - 'A' + 'a';
        if (c < 128 && km[c].keycode != 0x00)
            return km[c].keycode;
    }
    return 0;
}

/**
 * Parse a tag string (content between '<' and '>') into a hid_key_t.
 *
 * Supports:
 *   Single key:         "F5"      "ENTER"   "TAB"
 *   With modifiers:     "CTRL+C"  "CTRL+ALT+DEL"  "SHIFT+F1"
 *
 * @param tag     NUL-terminated tag content (will be modified in-place)
 * @param out     Output hid_key_t (modifier + keycode)
 * @return        0 on success, -1 if the tag is not recognised
 */
static int parse_special_tag(char *tag, hid_key_t *out, const hid_key_t *km)
{
    out->modifier = 0x00;
    out->keycode  = 0x00;

    str_toupper(tag);

    /* Tokenise on '+' */
    char *saveptr = NULL;
    char *token   = strtok_r(tag, "+", &saveptr);

    while (token != NULL) {
        char *next = strtok_r(NULL, "+", &saveptr);

        if (next == NULL) {
            /* Last token — try as a named key first */
            uint8_t kc = lookup_named_key(token, km);
            if (kc != 0x00) {
                out->keycode = kc;
            } else {
                /* Fall back: maybe it's a standalone modifier (e.g. <WIN>, <CTRL>) */
                uint8_t mod = lookup_modifier(token);
                if (mod == 0x00)
                    return -1;
                out->modifier |= mod;
                /* keycode stays 0x00 — sends modifier-only HID report */
            }
        } else {
            /* Intermediate token — must be a modifier */
            uint8_t mod = lookup_modifier(token);
            if (mod == 0x00) {
                /* Could be the key followed by more tokens — ambiguous, treat as error */
                return -1;
            }
            out->modifier |= mod;
        }
        token = next;
    }

    /* Valid if we have a keycode OR at least one modifier (modifier-only press) */
    return (out->keycode != 0x00 || out->modifier != 0x00) ? 0 : -1;
}

#endif /* SPECIAL_KEYS_H */
