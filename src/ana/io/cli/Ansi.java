package ana.io.cli;


// dep

import ana.util.color.Color;
import ana.util.text.*;

import java.util.*;


// main

public final class Ansi {
    // no inst

    private Ansi() {}

    // ansi specif

    private static final String PREFIX = "\033[";
    private static final String SUFFIX = "m";
    private static final String SEP    = ";";

    // stripCodes

    public static String stripCodes(String str) {
        return str.replaceAll(
            "\033\\[\\d+(;\\d+)*[mM]", ""
        );
    }

    // get codes

    public static String from(TextGroupStyle style) {
        List<String> parts = new ArrayList<>(style.attrs.size() + 1);

        // attrs
        for (TextAttr attr : style.attrs) {
            String bare = Attr.from(attr).bare();
            if (!bare.isEmpty())
                parts.add(bare);
        }

        // color
        if (style.col != null) {
            String bare = fromColorGetBare(style.col);
            parts.add(bare);
        }

        // build
        if (parts.isEmpty()) return "";
        return PREFIX + String.join(SEP, parts) + SUFFIX;
    }

    public static String from(Color col) {
        return PREFIX + fromColorGetBare(col) + SUFFIX;
    }

    private static String fromColorGetBare(Color col) {
        int[] codes = { 38, 2, col.red, col.green, col.blue };

        return String.join(
            SEP, intsToStrings(codes)
        );
    }

    // format

    public static String format(List<TextGroup> groups) {
        String str = "";
        for (TextGroup group : groups) {
            str += format(group);
        }

        return str;
    }

    public static String format(TextGroup group) {
        return format(group.str, group.style);
    }

    public static String format(String str, TextAttr... attrs) {
        TextGroupStyle style = new TextGroupStyle(attrs);
        return format(str, style);
    }

    public static String format(String str, TextGroupStyle style) {
        return formatFromCodeStr(str, from(style));
    }

    public static String format(String str, Color col) {
        return formatFromCodeStr(str, from(col));
    }

    private static String formatFromCodeStr(String str, String code) {
        if (code == null || code.isEmpty()) return str;
        str = str.replaceAll("\033\\[0m", "\033\\[0m" + code);
        return code + str + Attr.RESET;
    }


    // --


    // text attr

    private enum Attr {
        RESET         (TextAttr.NONE,           0),
        BOLD          (TextAttr.BOLD,           1),
        ITALIC        (TextAttr.ITALIC,         3),
        UNDERLINE     (TextAttr.UNDERLINE,      4),
        SLOW_BLINK    (TextAttr.SLOW_BLINK,     5),
        STRIKETHROUGH (TextAttr.STRIKETHROUGH,  9),
        OVERLINE      (TextAttr.OVERLINE,      53),

        FALLBACK (null);

        // attr

        private final TextAttr textAttr;
        private final String[] codes;

        // constr

        Attr(TextAttr textAttr, int... codes) {
            this.textAttr = textAttr;
            this.codes = intsToStrings(codes);
        }

        // get

        @Override
        public String toString() {
            if (codes.length == 0) return "";
            return PREFIX + bare() + SUFFIX;
        }

        public String bare() {
            return String.join(SEP, codes);
        }

        public TextAttr textAttr() {
            return textAttr;
        }

        // static util

        public static Attr from(TextAttr attr) {
            Attr t = BY_TEXT_ATTR.get(attr);
            return t == null ? FALLBACK : t;
        }

        // map config

        private static final Map<TextAttr, Attr> BY_TEXT_ATTR;
        static {
            var map = new EnumMap<TextAttr, Attr>(TextAttr.class);
            for (Attr a : values()) {
                if (a.textAttr() != null)
                    map.put(a.textAttr, a);
            }
            BY_TEXT_ATTR = Collections.unmodifiableMap(map);
        }
    }

    // priv util

    private static String[] intsToStrings(int[] ints) {
        String[] strs = new String[ints.length];
        for (int i = 0; i < ints.length; i++)
            strs[i] = Integer.toString(ints[i]);
        return strs;
    }
}
