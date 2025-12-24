package ana.util.style;


// dep

import ana.util.color.Color;
import ana.util.math.MathExt;


// main

public record Palette(Color primary, Color secondary, Color accent) {
    // constr

    public Palette(Color primary, Color secondary) {
        this(primary, secondary, secondary);
    }

    // factory (bad code)

    public static Palette getRandom() {
        double hue1 = MathExt.randDouble(0, 360);
        Color primary = Color.hsv(hue1, MathExt.randDouble(50, 75), MathExt.randDouble(50, 75));

        double dHue2 = MathExt.randDouble(45, 90) * MathExt.randSgn();
        double hue2 = (hue1 + dHue2) % 360;
        Color secondary = Color.hsv(hue2, MathExt.randDouble(75, 100), MathExt.randDouble(25, 50));

        Color accent = Color.hsv((hue1 + hue2) / 2, MathExt.randDouble(75, 100), MathExt.randDouble(90, 100));

        return new Palette(primary, accent, secondary);
    }
}
