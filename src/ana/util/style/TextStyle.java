package ana.util.style;


// dep

import ana.util.color.Color;

import java.util.*;


// main

public final class TextStyle {
    // const

    public static final Color COLOR_DEFAULT = Color.hsv(0, 0, 100);

    // attr

    public final Color col;
    public final List<TextAttr> attrs;

    // constr

    public TextStyle(TextAttr... attrs) {
        this(null, attrs);
    }

    public TextStyle(Color col, TextAttr... attrs) {
        this.col = col;

        Set<TextAttr> attrsSet = new HashSet<>(Arrays.asList(attrs));
        this.attrs = List.copyOf(attrsSet);
    }
}