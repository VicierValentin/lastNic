/**
 * main.c — lastnic: BBB USB HID keyboard emulator
 *
 * Reads a text file and sends its content as HID keyboard reports to the
 * connected PC via /dev/hidg0.
 *
 * Plain ASCII characters are typed directly.  Special keys and modifier
 * combinations are expressed as tags enclosed in angle brackets:
 *
 *   <ENTER>          <ESC>           <TAB>
 *   <F1> ... <F12>   <UP> <DOWN> ...  <HOME> <END> <PGUP> <PGDN>
 *   <INSERT> <DELETE>
 *   <CTRL+C>         <CTRL+ALT+DEL>  <SHIFT+F1>  <WIN+R>
 *
 * A literal '<' can be produced by writing '<<'.
 *
 * Usage:
 *   lastnic [options] <textfile>
 *
 * Options:
 *   -d <ms>    Delay between keystrokes in milliseconds (default: 10)
 *   -D <path>  HID device path (default: /dev/hidg0)
 *   -v         Verbose: log every keystroke
 *   -h         Show this help
 *
 * The USB HID gadget must be configured first:
 *   sudo ./scripts/setup_gadget.sh
 */

#define _POSIX_C_SOURCE 199309L

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <getopt.h>
#include <time.h>

#include "../include/keymap.h"
#include "../include/special_keys.h"

/* -------------------------------------------------------------------------
 * Constants
 * ---------------------------------------------------------------------- */
#define DEFAULT_HID_DEV       "/dev/hidg0"
#define DEFAULT_DELAY_MS      10
#define HID_REPORT_SIZE       8
#define TAG_BUF_MAX           64
/* Timeout waiting for the USB host to poll the HID IN endpoint.
 * Once g_multi is gone and the gadget is enumerated, the host polls
 * every 8 ms. We retry for up to 10 s before giving up. */
#define HID_WRITE_TIMEOUT_MS  10000
#define HID_RETRY_INTERVAL_MS 50   /* 20 attempts/s max — don't starve the CPU */

/* Forward declaration */
static void sleep_ms(unsigned int ms);

/* -------------------------------------------------------------------------
 * HID report helpers — O_NONBLOCK + EAGAIN retry loop
 *
 * The f_hid gadget driver is opened O_NONBLOCK. write() returns EAGAIN
 * when the USB IN endpoint is busy (host hasn't issued an IN token yet).
 * We spin-wait up to HID_WRITE_TIMEOUT_MS. On a properly enumerated
 * gadget the host polls every 8 ms so EAGAIN resolves almost immediately.
 * ---------------------------------------------------------------------- */
static int hid_write_buf(int fd, const uint8_t *buf)
{
    int elapsed = 0;
    while (elapsed < HID_WRITE_TIMEOUT_MS) {
        ssize_t n = write(fd, buf, HID_REPORT_SIZE);
        if (n == HID_REPORT_SIZE)
            return 0;
        if (n < 0 && (errno == EAGAIN || errno == EWOULDBLOCK)) {
            sleep_ms(HID_RETRY_INTERVAL_MS);
            elapsed += HID_RETRY_INTERVAL_MS;
            continue;
        }
        fprintf(stderr, "Error: HID write failed: %s\n", strerror(errno));
        return -1;
    }
    fprintf(stderr,
            "Error: USB host not polling HID endpoint after %d ms.\n"
            "  Check: gadget enumerated? Run 'sudo ~/setup_gadget.sh' again.\n"
            "  Debug: lsusb | grep 1d6b:0106  (should show BBB HID Keyboard)\n",
            HID_WRITE_TIMEOUT_MS);
    return -1;
}

/* -------------------------------------------------------------------------
 * HID report helpers
 * ---------------------------------------------------------------------- */

static int hid_write_report(int fd, uint8_t modifier, uint8_t keycode)
{
    uint8_t report[HID_REPORT_SIZE] = {
        modifier, 0x00,
        keycode,  0x00, 0x00, 0x00, 0x00, 0x00,
    };
    return hid_write_buf(fd, report);
}

static int hid_release_keys(int fd)
{
    static const uint8_t zeros[HID_REPORT_SIZE] = {0};
    return hid_write_buf(fd, zeros);
}

static void sleep_ms(unsigned int ms)
{
    if (ms == 0) return;
    struct timespec ts;
    ts.tv_sec  = ms / 1000;
    ts.tv_nsec = (long)(ms % 1000) * 1000000L;
    nanosleep(&ts, NULL);
}

/* -------------------------------------------------------------------------
 * Send a single hid_key_t as a press+release pair
 * ---------------------------------------------------------------------- */
static int send_key(int fd, const hid_key_t *k,
                    unsigned int delay_ms, int verbose,
                    const char *label)
{
    if (verbose)
        printf("  [lastnic] %-20s mod=0x%02X kc=0x%02X\n",
               label ? label : "?", k->modifier, k->keycode);

    if (hid_write_report(fd, k->modifier, k->keycode) < 0)
        return -1;
    sleep_ms(delay_ms);
    if (hid_release_keys(fd) < 0)
        return -1;
    sleep_ms(delay_ms);
    return 0;
}

/* -------------------------------------------------------------------------
 * Send one ASCII character
 * ---------------------------------------------------------------------- */
static int send_ascii(int fd, int ch, unsigned int delay_ms, int verbose)
{
    if (ch < 0 || ch > 127) {
        if (verbose)
            fprintf(stderr, "  [lastnic] SKIP  0x%02X (out of ASCII range)\n", ch);
        return 0;
    }

    const hid_key_t *k = &keymap[(uint8_t)ch];
    if (k->keycode == 0x00) {
        if (verbose)
            fprintf(stderr, "  [lastnic] SKIP  0x%02X '%c' (no HID mapping)\n",
                    ch, (ch >= 0x20 && ch < 0x7F) ? ch : '?');
        return 0;
    }

    char label[8];
    if (ch >= 0x20 && ch < 0x7F)
        snprintf(label, sizeof(label), "'%c'", ch);
    else
        snprintf(label, sizeof(label), "0x%02X", ch);

    return send_key(fd, k, delay_ms, verbose, label);
}

/* -------------------------------------------------------------------------
 * Parse and send a <TAG> token
 * ---------------------------------------------------------------------- */
static int send_tag(int fd, const char *tag_content,
                    unsigned int delay_ms, int verbose)
{
    char buf[TAG_BUF_MAX];
    strncpy(buf, tag_content, TAG_BUF_MAX - 1);
    buf[TAG_BUF_MAX - 1] = '\0';

    hid_key_t k;
    if (parse_special_tag(buf, &k) < 0) {
        fprintf(stderr, "Warning: unknown tag <%s>, skipped\n", tag_content);
        return 0;
    }

    char label[TAG_BUF_MAX + 2];
    snprintf(label, sizeof(label), "<%s>", tag_content);
    return send_key(fd, &k, delay_ms, verbose, label);
}

/* -------------------------------------------------------------------------
 * Process the input file
 *
 * State machine:
 *   NORMAL  - reading plain text characters
 *   IN_TAG  - inside <...>, collecting tag body
 *
 * Special cases:
 *   '<<'  -> literal '<'
 *   '<>'  -> silently ignored (empty tag)
 * ---------------------------------------------------------------------- */
typedef enum { ST_NORMAL, ST_IN_TAG } parse_state_t;

static int process_file(FILE *fp, int hid_fd,
                        unsigned int delay_ms, int verbose,
                        long *out_sent, long *out_skipped)
{
    parse_state_t state   = ST_NORMAL;
    char          tag_buf[TAG_BUF_MAX];
    int           tag_len = 0;
    int           ch;
    long          sent    = 0;
    long          skipped = 0;

    while ((ch = fgetc(fp)) != EOF) {

        if (state == ST_NORMAL) {

            if (ch == '<') {
                int next = fgetc(fp);
                if (next == '<') {
                    /* '<<' -> literal '<' : Shift+, on US layout */
                    hid_key_t lt = { HID_MOD_LEFT_SHIFT, 0x36 };
                    if (send_key(hid_fd, &lt, delay_ms, verbose, "'<'") < 0)
                        return -1;
                    sent++;
                } else {
                    if (next != EOF) ungetc(next, fp);
                    state   = ST_IN_TAG;
                    tag_len = 0;
                    tag_buf[0] = '\0';
                }
            } else {
                /* Normalise CR+LF -> single LF */
                if (ch == '\r') {
                    int next = fgetc(fp);
                    if (next != '\n' && next != EOF)
                        ungetc(next, fp);
                    ch = '\n';
                }
                int rc = send_ascii(hid_fd, ch, delay_ms, verbose);
                if (rc < 0) return -1;
                if (ch < 128 && keymap[(uint8_t)ch].keycode != 0x00)
                    sent++;
                else
                    skipped++;
            }

        } else { /* ST_IN_TAG */

            if (ch == '>') {
                tag_buf[tag_len] = '\0';
                state = ST_NORMAL;
                if (tag_len > 0) {
                    int rc = send_tag(hid_fd, tag_buf, delay_ms, verbose);
                    if (rc < 0) return -1;
                    sent++;
                }
                tag_len = 0;

            } else if (ch == '\n') {
                /* Unclosed tag at end of line - flush as literals */
                fprintf(stderr,
                        "Warning: unclosed tag '<%s', treating as literal text\n",
                        tag_buf);
                hid_key_t lt = { HID_MOD_LEFT_SHIFT, 0x36 };
                if (send_key(hid_fd, &lt, delay_ms, verbose, "'<'") < 0)
                    return -1;
                for (int i = 0; i < tag_len; i++) {
                    if (send_ascii(hid_fd, (unsigned char)tag_buf[i],
                                   delay_ms, verbose) < 0)
                        return -1;
                }
                /* Send the newline that terminated the line */
                if (send_ascii(hid_fd, '\n', delay_ms, verbose) < 0)
                    return -1;
                state   = ST_NORMAL;
                tag_len = 0;

            } else {
                if (tag_len < TAG_BUF_MAX - 1)
                    tag_buf[tag_len++] = (char)ch;
                else {
                    fprintf(stderr,
                            "Warning: tag too long (max %d), truncating\n",
                            TAG_BUF_MAX - 1);
                    tag_buf[tag_len] = '\0';
                }
            }
        }
    }

    /* Unclosed tag at EOF */
    if (state == ST_IN_TAG && tag_len > 0)
        fprintf(stderr, "Warning: unclosed tag '<%s' at EOF, ignored\n", tag_buf);

    *out_sent    = sent;
    *out_skipped = skipped;
    return 0;
}

/* -------------------------------------------------------------------------
 * Main
 * ---------------------------------------------------------------------- */
static void print_usage(const char *prog)
{
    fprintf(stderr,
        "Usage: %s [options] <textfile>\n"
        "\n"
        "  <textfile>   Path to the text file to type out\n"
        "\n"
        "Options:\n"
        "  -d <ms>      Delay between keystrokes in ms (default: %d)\n"
        "  -D <path>    HID device path (default: %s)\n"
        "  -v           Verbose: log every keystroke sent\n"
        "  -h           Show this help\n"
        "\n"
        "Special key tags (case-insensitive):\n"
        "  <ENTER>  <ESC>  <TAB>  <BACKSPACE>  <SPACE>  <DELETE>  <INSERT>\n"
        "  <HOME>   <END>  <PGUP> <PGDN>\n"
        "  <UP>  <DOWN>  <LEFT>  <RIGHT>\n"
        "  <F1> ... <F12>\n"
        "  <CAPSLOCK>  <NUMLOCK>  <SCROLLLOCK>  <PAUSE>  <PRINTSCREEN>\n"
        "  <KP_0>...<KP_9>  <KP_ENTER>  <KP_PLUS>  <KP_MINUS>  etc.\n"
        "\n"
        "Modifier combos (CTRL/SHIFT/ALT/WIN + key):\n"
        "  <CTRL+C>  <CTRL+ALT+DEL>  <SHIFT+F1>  <WIN+R>  <ALT+TAB>\n"
        "\n"
        "Literal '<': use '<<' in the text file.\n"
        "\n"
        "Prerequisite: sudo ./scripts/setup_gadget.sh\n",
        prog, DEFAULT_DELAY_MS, DEFAULT_HID_DEV);
}

int main(int argc, char *argv[])
{
    const char  *hid_dev  = DEFAULT_HID_DEV;
    unsigned int delay_ms = DEFAULT_DELAY_MS;
    int          verbose  = 0;
    int          opt;

    while ((opt = getopt(argc, argv, "d:D:vh")) != -1) {
        switch (opt) {
        case 'd':
            delay_ms = (unsigned int)atoi(optarg);
            break;
        case 'D':
            hid_dev = optarg;
            break;
        case 'v':
            verbose = 1;
            break;
        case 'h':
            print_usage(argv[0]);
            return 0;
        default:
            print_usage(argv[0]);
            return 1;
        }
    }

    if (optind >= argc) {
        fprintf(stderr, "Error: no input file specified.\n\n");
        print_usage(argv[0]);
        return 1;
    }

    const char *textfile = argv[optind];

    int hid_fd = open(hid_dev, O_RDWR | O_NONBLOCK);
    if (hid_fd < 0) {
        fprintf(stderr,
                "Error: cannot open HID device '%s': %s\n"
                "  Hint: run 'sudo ./setup_gadget.sh' first.\n",
                hid_dev, strerror(errno));
        return 1;
    }

    FILE *fp = fopen(textfile, "r");
    if (!fp) {
        fprintf(stderr, "Error: cannot open '%s': %s\n",
                textfile, strerror(errno));
        close(hid_fd);
        return 1;
    }

    if (verbose)
        printf("[lastnic] '%s' -> %s  delay=%u ms\n",
               textfile, hid_dev, delay_ms);

    long sent = 0, skipped = 0;
    int rc = process_file(fp, hid_fd, delay_ms, verbose, &sent, &skipped);

    fclose(fp);
    close(hid_fd);

    if (rc < 0) {
        fprintf(stderr, "[lastnic] Aborted due to HID write error.\n");
        return 1;
    }

    if (verbose)
        printf("[lastnic] Done. %ld sent, %ld skipped.\n", sent, skipped);

    return 0;
}
