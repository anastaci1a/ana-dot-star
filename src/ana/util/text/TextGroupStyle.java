package ana.util.text;


// dep

import ana.util.color.Color;

import java.util.*;


// main

public final class TextGroupStyle {
    // const

    public static final Color COLOR_DEFAULT = Color.hsv(0, 0, 100);

    // attr

    public final Color col;
    public final List<TextAttr> attrs;

    // constr

    public TextGroupStyle(TextAttr... attrs) {
        this(COLOR_DEFAULT, attrs);
    }

    public TextGroupStyle(Color col, TextAttr... attrs) {
        this.col = col;

        Set<TextAttr> attrsSet = new HashSet<>(Arrays.asList(attrs));
        this.attrs = List.copyOf(attrsSet);
    }

    // sys

    public String toString() {
        String className = getClass().getSimpleName();

        return String.format(
            "%s:{%s, %s}",
            className, col, attrs
        );
    }
}