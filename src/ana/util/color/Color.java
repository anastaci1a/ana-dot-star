package ana.util.color;


// dep

import ana.lang.InvalidHexException;
import ana.util.math.MathExt;


// main

public final class Color {
    // attr

    public final int    red, green, blue, alpha;
    public final double hue, saturation, value;

    // sys constr

    private Color(int[] rgb, double[] hsv, int alpha) {
        int[] rgb_constr = MathExt.clampAll(rgb, 0, 255);

        this.red   = rgb_constr[0];
        this.green = rgb_constr[1];
        this.blue  = rgb_constr[2];
        
        this.hue        = hsv[0] % 360.0;
        this.saturation = Math.clamp(hsv[1], 0, 100);
        this.value      = Math.clamp(hsv[2], 0, 100);

        this.alpha = Math.clamp(alpha, 0, 255);
    }

    // from (static factory)

    public static Color hex(String hex) {
        int[] rgba = hexToRgba(hex);
        return rgba(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public static Color rgb(int red, int green, int blue) {
        return rgba(red, green, blue, 255);
    }

    public static Color rgba(int red, int green, int blue, int alpha) {
        return new Color(
            new int[] { red, green, blue },
            rgbToHsv(red, green, blue),
            alpha
        );
    }

    public static Color rgba(int[] rgb, int alpha) {
        return new Color(rgb, rgbToHsv(rgb), alpha);
    }

    public static Color hsv(double hue, double saturation, double value) {
        return hsva(hue, saturation, value, 255);
    }

    public static Color hsva(double hue, double saturation, double value, int alpha) {
        return hsva(
            new double[] { hue, saturation, value },
            alpha
        );
    }

    public static Color hsva(double[] hsv, int alpha) {
        return new Color(hsvToRgb(hsv), hsv, alpha);
    }

    // to (type conversions)

    @Override
    public String toString() {
        return toHex();
    }

    public String toHex() {
        return String.format("#%02x%02x%02x%02x", this.red, this.green, this.blue, this.alpha);
    }

    public int toArgbInt() {  // non-multiplied alpha
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public int toArgbIntPre() { // premultiplied alpha
        float alphaNorm = alpha / 255.0f;

        int multR = Math.round(red   * alphaNorm);
        int multG = Math.round(green * alphaNorm);
        int multB = Math.round(blue  * alphaNorm);

        multR = Math.clamp(multR, 0, 255);
        multG = Math.clamp(multG, 0, 255);
        multB = Math.clamp(multB, 0, 255);

        return (alpha << 24) | (multR << 16) | (multG << 8) | multB;
    }

    public double getAlphaNorm() {
        return alpha / 255.0;
    }

    // with (mut clone)

    public Color withAlpha(int alpha) {
        return Color.rgba(this.red, this.green, this.blue, alpha);
    }

    public Color withRed(int red) {
        return Color.rgba(red, this.green, this.blue, this.alpha);
    }

    public Color withGreen(int green) {
        return Color.rgba(this.red, green, this.blue, this.alpha);
    }

    public Color withBlue(int blue) {
        return Color.rgba(this.red, this.green, blue, this.alpha);
    }

    public Color withHue(double hue) {
        return Color.hsva(hue, this.saturation, this.value, this.alpha);
    }

    public Color withSaturation(double saturation) {
        return Color.hsva(this.hue, saturation, this.value, this.alpha);
    }

    public Color withValue(double value) {
        return Color.hsva(this.hue, this.saturation, value, this.alpha);
    }

    // add (mut clone)

    public Color addAlpha(int deltaAlpha) {
        return addRgba(0, 0, 0, deltaAlpha);
    }

    public Color addRed(int deltaRed) {
        return addRgb(deltaRed, 0, 0);
    }

    public Color addGreen(int deltaGreen) {
        return addRgb(0, deltaGreen, 0);
    }

    public Color addBlue(int deltaBlue) {
        return addRgb(0, 0, deltaBlue);
    }

    public Color addRgb(int deltaRed, int deltaGreen, int deltaBlue) {
        return addRgba(deltaRed, deltaGreen, deltaBlue, 0);
    }

    public Color addRgba(int deltaRed, int deltaGreen, int deltaBlue, int deltaAlpha) {
        return Color.rgba(
            this.red + deltaRed,
            this.green + deltaGreen,
            this.blue + deltaBlue,
            this.alpha + deltaAlpha
        );
    }

    public Color addHue(double deltaHue) {
        return addHsv(deltaHue, 0, 0);
    }

    public Color addSaturation(double deltaSaturation) {
        return addHsv(0, deltaSaturation, 0);
    }

    public Color addValue(double deltaValue) {
        return addHsv(0, 0, deltaValue);
    }

    public Color addHsv(double deltaHue, double deltaSaturation, double deltaValue) {
        return addHsva(deltaHue, deltaSaturation, deltaValue, 0);
    }

    public Color addHsva(double deltaHue, double deltaSaturation, double deltaValue, int deltaAlpha) {
        return Color.hsva(
            this.hue + deltaHue,
            this.saturation + deltaSaturation,
            this.value + deltaValue,
            this.alpha + deltaAlpha
        );
    }

    // mult (mut clone)

    public Color multAlpha(double multAlpha) {
        return multRgb(1, 1, multAlpha);
    }

    public Color multRed(double multRed) {
        return multRgb(multRed, 1, 1);
    }

    public Color multGreen(double multGreen) {
        return multRgb(1, multGreen, 1);
    }

    public Color multBlue(double multBlue) {
        return multRgb(1, 1, multBlue);
    }

    public Color multRgb(double multRed, double multGreen, double multBlue) {
        return multRgba(multRed, multGreen, multBlue, 1);
    }

    public Color multRgba(double multRed, double multGreen, double multBlue, double multAlpha) {
        return Color.rgba(
            (int) Math.round(this.red * multRed),
            (int) Math.round(this.green * multGreen),
            (int) Math.round(this.blue * multBlue),
            (int) Math.round(this.alpha * multAlpha)
        );
    }

    public Color multHue(double multHue) {
        return multHsv(multHue, 1, 1);
    }

    public Color multSaturation(double multSaturation) {
        return multHsv(1, multSaturation, 1);
    }

    public Color multValue(double multValue) {
        return multHsv(1, 1, multValue);
    }

    public Color multHsv(double multHue, double multSaturation, double multValue) {
        return multHsva(multHue, multSaturation, multValue, 1);
    }

    public Color multHsva(double multHue, double multSaturation, double multValue, double multAlpha) {
        return Color.hsva(
            this.hue * multHue,
            this.saturation * multSaturation,
            this.value * multValue,
            (int) Math.round(this.alpha * multAlpha)
        );
    }

    // lerp

    public static Color lerp(double n, Color c1, Color c2) {
        n = Math.clamp(n, 0, 1);

        return Color.rgba(
            (int) Math.round(MathExt.lerp(n, c1.red,   c2.red  )),
            (int) Math.round(MathExt.lerp(n, c1.green, c2.green)),
            (int) Math.round(MathExt.lerp(n, c1.blue,  c2.blue )),
            (int) Math.round(MathExt.lerp(n, c1.alpha, c2.alpha))
        );
    }


    // ---


    // priv rgb

    private static double[] rgbToHsv(int[] rgb) {
        return rgbToHsv(rgb[0], rgb[1], rgb[2]);
    }

    private static double[] rgbToHsv(int red, int green, int blue) {
        int Cmax = MathExt.max(red, green, blue);
        int Cmin = MathExt.min(red, green, blue);

        double H, S, V;
        double delta = Cmax - Cmin;

        if (delta == 0) H = 0;
        else if (Cmax == red   ) H = ((green - blue ) / delta) % 6.0;
        else if (Cmax == green ) H = ((blue  - red  ) / delta) + 2.0;
        else    /*    == blue */ H = ((red   - green) / delta) + 4.0;

        H *= 60.0;
        if (H < 0) H += 360.0;

        if (Cmax == 0) S = 0;
        else S = 100.0 * (delta / Cmax);

        V = 100.0 * (Cmax / 255.0);

        return new double[] { H, S, V };
    }

    // priv hsv

    private static int[] hsvToRgb(double[] hsv) {
        return hsvToRgb(hsv[0], hsv[1], hsv[2]);
    }

    private static int[] hsvToRgb(double hue, double saturation, double value) {
        double H = (hue + 360.0) % 360.0;
        double S = Math.clamp(saturation, 0, 100);
        double V = Math.clamp(value, 0, 100);

        double C = (V / 100.0) * (S / 100.0);
        double X = C * (1 - Math.abs(((H / 60.0) % 2.0) - 1));
        double m = (V / 100.0) - C;

        double[] _rgb = new double[3];
        /**/ if(  0 <= H && H <  60) _rgb = new double[] { C, X, 0 };
        else if( 60 <= H && H < 120) _rgb = new double[] { X, C, 0 };
        else if(120 <= H && H < 180) _rgb = new double[] { 0, C, X };
        else if(180 <= H && H < 240) _rgb = new double[] { 0, X, C };
        else if(240 <= H && H < 300) _rgb = new double[] { X, 0, C };
        else if(300 <= H && H < 360) _rgb = new double[] { C, 0, X };

        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            double exact = (_rgb[i] + m) * 255.0;
            rgb[i] = (int) Math.round(exact);
        }

        return rgb;
    }

    // priv hex

    private static int[] hexToRgba(String hex) {
        String pureHex = hex.replace("#", "").toLowerCase();

        boolean valid = pureHex.matches("^([0-9a-f]{6})|([0-9a-f]{8})$");
        if (!valid) throw new InvalidHexException(hex);

        int[] rgb = new int[4];
        rgb[3] = 255;
        for (int i = 0; i < pureHex.length(); i += 2) {
            String substr = pureHex.substring(i, i + 2);
            rgb[i/2] = Integer.parseInt(substr, 16);
        }

        return rgb;
    }


    // --


    // predefined colors
    // (https://en.wikipedia.org/wiki/List_of_colors:_A%E2%80%93F#:~:text=Color%20classifications)

    // pink
    public static final Color PINK = hex("#FFC0CB");
    public static final Color LIGHT_PINK = hex("#FFB6C1");
    public static final Color PANTONE_PINK = hex("#D74894");
    public static final Color HOT_PINK = hex("#FF69B4");
    public static final Color BRIGHT_PINK = hex("#FF007F");
    public static final Color BRINK_PINK = hex("#FB607F");
    public static final Color DEEP_PINK = hex("#FF1493");
    public static final Color LIGHT_DEEP_PINK = hex("#FF5CCD");
    public static final Color PASTEL_PINK = hex("#FFD1DC");
    public static final Color PERSIAN_PINK = hex("#F77FBE");
    public static final Color PINK_LACE = hex("#FFDDF4");
    public static final Color PALE_PINK = hex("#F9CCCA");
    public static final Color COTTON_CANDY = hex("#FFB7D5");
    public static final Color BABY_PINK = hex("#F4C2C2");
    public static final Color CHERRY_BLOSSOM_PINK = hex("#FFB7C5");
    public static final Color AMARANTH_PINK = hex("#F19CBB");
    public static final Color SALMON_PINK = hex("#FF91A4");
    public static final Color CORAL_PINK = hex("#F88379");
    public static final Color PUCE = hex("#CC8899");
    public static final Color ZARQA = hex("#FF4500");
    public static final Color PINK_LAVENDER = hex("#DBB2D1");
    public static final Color LAVENDER_PINK = hex("#FBAED2");
    public static final Color CERISE = hex("#DE3163");
    public static final Color ROSE = hex("#FF0080");
    public static final Color ROSE_POMPADOUR = hex("#ED7A9B");
    public static final Color LIGHT_CORAL = hex("#F08080");
    public static final Color TANGO_PINK = hex("#E4717A");
    public static final Color CONGO_PINK = hex("#F88379");
    public static final Color APRICOT = hex("#FBCEB1");
    public static final Color BUBBLEGUM_PINK = hex("#F58092");
    public static final Color MELON = hex("#FDBCB4");
    public static final Color FOLLY = hex("#FF004F");
    public static final Color SILVER_PINK = hex("#C4AEAD");
    public static final Color QUEEN_PINK = hex("#E8CCD7");
    public static final Color ORCHID_PINK = hex("#F2BDCD");
    public static final Color MONA_LISA = hex("#FF948E");

    // red
    public static final Color RED = hex("#FF0000");
    public static final Color LIGHT_RED = hex("FF8080");
    public static final Color DARK_RED = hex("8B0000");
    public static final Color CMYK_RED = hex("#ED1B24");
    public static final Color MUNSELL_RED = hex("#FF0042");
    public static final Color PANTONE_RED = hex("#ED2839");
    public static final Color CRAYOLA_RED = hex("#ED0A3F");
    public static final Color BLOOD_RED = hex("#660000");
    public static final Color FIRE_ENGINE_RED = hex("#CE2029");
    public static final Color BARN_RED = hex("#7C0902");
    public static final Color CHILI_RED = hex("#E03C31");
    public static final Color VENETIAN_RED = hex("#C80815");
    public static final Color PERSIAN_RED = hex("#CC3333");
    public static final Color POPPY_RED = hex("#DC343B");
    public static final Color CARDINAL = hex("#C51E3A");
    public static final Color MADDER = hex("#A50021");
    public static final Color TOMATO = hex("#FF6347");
    public static final Color CRIMSON = hex("#DC143C");
    public static final Color CINNABAR = hex("#E44D2E");
    public static final Color AMARANTH = hex("#E52B50");
    public static final Color CARMINE = hex("#960018");
    public static final Color VERMILON = hex("#E34234");
    public static final Color ROSE_VALE = hex("#AB4E52");
    public static final Color OLD_ROSE = hex("#C08081");
    public static final Color SCARLET = hex("#FF2400");

    // orange
    public static final Color ORANGE = hex("#FF8000");
    public static final Color LIGHT_ORANGE = hex("#FED8B1");
    public static final Color PANTONE_ORANGE = hex("#FF5800");
    public static final Color CRAYOLA_ORANGE = hex("#FF5800");
    public static final Color SAFETY_ORANGE = hex("#FF7900");
    public static final Color ENGINEERING_ORANGE = hex("#BA160C");
    public static final Color INTERNATIONAL_ORANGE = hex("#F04A00");
    public static final Color PORTLAND_ORANGE = hex("#FF5A36");
    public static final Color PERSIAN_ORANGE = hex("#D99058");
    public static final Color CARROT_ORANGE = hex("#ED9121");
    public static final Color ORANGE_PEEL = hex("#FF9F00");
    public static final Color TANGERINE = hex("#F28500");
    public static final Color PERSIMMON = hex("#EC5800");
    public static final Color CORAL = hex("#FF7F50");
    public static final Color PUMPKIN = hex("#FF7518");
    public static final Color JASPER = hex("#D05340");
    public static final Color BUTTERSCOTCH = hex("#E09540");
    public static final Color SALMON = hex("#FA8072");

    // yellow
    public static final Color YELLOW = hex("#FFFF00");
    public static final Color LIGHT_YELLOW = hex("#FFFFE0");
    public static final Color MUNSELL_YELLOW = hex("#FFDB00");
    public static final Color PANTONE_YELLOW = hex("#FEDF00");
    public static final Color CRAYOLA_LEMON = hex("#FDFF00");
    public static final Color ROYAL_YELLOW = hex("#FADA5E");
    public static final Color SAFETY_YELLOW = hex("#EED202");
    public static final Color CHARTREUSE_YELLOW = hex("#DFFF00");
    public static final Color GOLD = hex("#FFD700");
    public static final Color ORPIMENT = hex("#FDCC01");
    public static final Color MUSTARD = hex("#FFDB58");
    public static final Color SAFFRON = hex("#F4C430");
    public static final Color GOLDENROD = hex("#DAA520");
    public static final Color XANTHOUS = hex("#F1B42F");
    public static final Color XANTHIC = hex("#EEED09");
    public static final Color STRAW = hex("#dfd87f");
    public static final Color MANGO = hex("#FFC800");
    public static final Color CITRON = hex("#DDD06A");
    public static final Color AMBER = hex("#FFBF00");
    public static final Color CREAM = hex("#FFFDD0");
    public static final Color MOCCASIN = hex("#FFE4B5");
    public static final Color SUNSET = hex("#FAD6A5");
    public static final Color MINDARO = hex("#E3F988");

    // chartreuse
    public static final Color CHARTREUSE = hex("#80FF00");
    public static final Color CHARTREUSE_TRADITIONAL = hex("#DFFF00");
    public static final Color LAWN_GREEN = hex("#7CFC00");
    public static final Color BRIGHT_GREEN = hex("#66FF00");
    public static final Color YELLOW_GREEN = hex("#9ACD32");
    public static final Color GREEN_YELLOW = hex("#ADFF2F");
    public static final Color LIME = hex("#BFFF00");
    public static final Color PEAR = hex("#D1E231");
    public static final Color PISTACHIO = hex("#93C572");
    public static final Color AVOCADO = hex("#568203");
    public static final Color ASPARAGUS = hex("#87A96B");
    public static final Color ARTICHOKE = hex("#8F9779");
    public static final Color GREEN_EARTH = hex("#DADD98");

    // green
    public static final Color GREEN = hex("#00FF00");
    public static final Color LIGHT_GREEN = hex("#90EE90");
    public static final Color DARK_GREEN = hex("#2f6119");
    public static final Color CMYK_GREEN = hex("#00A550");
    public static final Color MUNSELL_GREEN = hex("#00FFB5");
    public static final Color PANTONE_GREEN = hex("#00AD83");
    public static final Color CRAYOLA_GREEN = hex("#1CAC78");
    public static final Color LIME_GREEN = hex("#32CD32");
    public static final Color PALE_GREEN = hex("#98FB98");
    public static final Color ERIN = hex("#00FF40");
    public static final Color HARLEQUIN = hex("#3FFF00");
    public static final Color ACID_GREEN = hex("#B0BF1A");
    public static final Color APPLE_GREEN = hex("#8AB800");
    public static final Color PANTONE_ARTICHOKE_GREEN = hex("#4B6F44");
    public static final Color EVERGREEN = hex("#05472A");
    public static final Color FERN_GREEN = hex("#4F7942");
    public static final Color FERN = hex("#63B76C");
    public static final Color FOREST_GREEN = hex("#228B22");
    public static final Color JUNGLE_GREEN = hex("#29AB87");
    public static final Color LAUREL_GREEN = hex("#A9BA9D");
    public static final Color MANTIS = hex("#74C365");
    public static final Color MINT_GREEN = hex("#98FB98");
    public static final Color OLIVE = hex("#808000");
    public static final Color PINE_GREEN = hex("#01796F");
    public static final Color RESEDA_GREEN = hex("#6C7C59");
    public static final Color KHAKI = hex("#728639");

    // spring green
    public static final Color MEDIUM_SPRING_GREEN = hex("#00FA9A");
    public static final Color DARK_SPRING_GREEN = hex("#177245");
    public static final Color SEA_GREEN = hex("#2E8B57");
    public static final Color AQUAMARINE = hex("#00FFC0");
    public static final Color EMERALD = hex("#50C878");
    public static final Color VIRIDIAN = hex("#40826D");
    public static final Color CARIBBEAN_GREEN = hex("#00CC99");
    public static final Color MAGIC_MINT = hex("#AAF0D1");
    public static final Color MINT = hex("#3EB489");
    public static final Color MOUNTAIN_MEADOW = hex("#30BA8F");
    public static final Color PERSIAN_GREEN = hex("#00A693");
    public static final Color SEAFOAM_GREEN = hex("#9FE2BF");
    public static final Color JADE = hex("#00A86B");
    public static final Color MALACHITE = hex("#0BDA51");

    // cyan
    public static final Color CYAN = hex("#00FFFF");
    public static final Color LIGHT_CYAN = hex("#E0FFFF");
    public static final Color DARK_CYAN = hex("#008B8B");
    public static final Color CMYK_CYAN = hex("#00B7EB");
    public static final Color CELESTE = hex("#B2FFFF");
    public static final Color TURQUOISE = hex("#40E0D0");
    public static final Color ELECTRIC_BLUE = hex("#7DF9FF");
    public static final Color ROBIN_EGG_BLUE = hex("#00CCCC");
    public static final Color OLO = hex("#00FFCC");
    public static final Color TEAL = hex("#008080");
    public static final Color MOONSTONE = hex("#3AA8C1");
    public static final Color BLUE_GREEN = hex("#0D98BA");
    public static final Color PEACOCK_BLUE = hex("#004958");
    public static final Color LIGHT_SEA_GREEN = hex("#20B2AA");
    public static final Color KEPPEL = hex("#3AB09E");

    // azure
    public static final Color AZURE = hex("#0080FF");
    public static final Color PALE_AZURE = hex("#87D3F8");
    public static final Color COLUMBIA_BLUE = hex("#B9D9EB");
    public static final Color CLOUDY_BLUE = hex("#9BBCD8");
    public static final Color LIGHT_SKY_BLUE = hex("#87CEFA");
    public static final Color SKY_BLUE = hex("#87CEEB");
    public static final Color DEEP_SKY_BLUE = hex("#00BFFF");
    public static final Color CERULEAN = hex("#007BA7");
    public static final Color CORNFLOWER_BLUE = hex("#6495ED");
    public static final Color MANGANESE_BLUE = hex("#1099D6");
    public static final Color TRUE_BLUE = hex("#2D68C4");
    public static final Color ROYAL_BLUE = hex("#4169E1");
    public static final Color ROYAL_BLUE_TRADITIONAL = hex("#002366");
    public static final Color LAPIS_LAZULI = hex("#26619C");

    // blue
    public static final Color BLUE = hex("#0000FF");
    public static final Color LIGHT_BLUE = hex("#ADD8E6");
    public static final Color DARK_BLUE = hex("#00008B");
    public static final Color CMYK_BLUE = hex("#333399");
    public static final Color PANTONE_BLUE = hex("#0018A8");
    public static final Color MUNSELL_BLUE = hex("#00DEFF");
    public static final Color PERIWINKLE = hex("#CCCCFF");
    public static final Color BLUEBONNET = hex("#1C1CF0");
    public static final Color TWIN_BLUE = hex("#BEDBED");
    public static final Color SMALT = hex("#003399");
    public static final Color SAVOY_BLUE = hex("#003399");
    public static final Color MEDIUM_BLUE = hex("#0000CD");
    public static final Color EGYPTIAN_BLUE = hex("#1034A6");
    public static final Color ULTRAMARINE = hex("#1034A6");
    public static final Color NAVY_BLUE = hex("#000080");
    public static final Color COOL_BLACK = hex("#002E63");
    public static final Color BABY_BLUE = hex("#89CFF0");
    public static final Color INDIGO = hex("#4000FF");

    // violet
    public static final Color VIOLET = hex("#8000FF");
    public static final Color DARK_VIOLET = hex("#9400D3");
    public static final Color PANTONE_ULTRA_VIOLET = hex("#645394");
    public static final Color AFRICAN_VIOLET = hex("#B284BE");
    public static final Color CHINESE_VIOLET = hex("#856088");
    public static final Color ENGLISH_VIOLET = hex("#563C5C");
    public static final Color FRENCH_VIOLET = hex("#8806CE");
    public static final Color JAPANESE_VIOLET = hex("#5B3256");
    public static final Color SPANISH_VIOLET = hex("#4C2882");
    public static final Color RUSSIAN_VIOLET = hex("#32174D");
    public static final Color GRAPE = hex("#6F2DA8");
    public static final Color LAVENDER = hex("#B57EDC");
    public static final Color PALE_LAVENDER = hex("#DCD0FF");
    public static final Color MAUVE = hex("#E0B0FF");
    public static final Color WISTERIA = hex("#C9A0DC");
    public static final Color PETUNIA = hex("#470659");
    public static final Color LILAC = hex("#C8A2C8");

    // magenta
    public static final Color MAGENTA = hex("#FF00FF");
    public static final Color DARK_MAGENTA = hex("#8B008B");
    public static final Color PANTONE_MAGENTA = hex("#D0417E");
    public static final Color CRAYOLA_MAGENTA = hex("#F653A6");
    public static final Color CMYK_MAGENTA = hex("#FF0090");
    public static final Color MAGENTA_DYE = hex("#CA1F7B");
    public static final Color HOT_MAGENTA = hex("#FF1DCE");
    public static final Color MAGENTA_HAZE = hex("#9F4576");
    public static final Color SKY_MAGENTA = hex("#CF71AF");
    public static final Color TELEMAGENTA = hex("#CF3476");
    public static final Color ORCHID = hex("#DA70D6");
    public static final Color WINE = hex("#800032");
    public static final Color CRAYOLA_PLUM = hex("#843179");
    public static final Color ROSE_QUARTZ = hex("#AA98A9");
    public static final Color AMARANTH_PURPLE = hex("#AB274F");
    public static final Color STEEL_PINK = hex("#CC33CC");
    public static final Color HALAYA_UBE = hex("#663854");

    // dark
    public static final Color BROWN = hex("#964B00");
    public static final Color GARNET = hex("#733635");
    public static final Color ROSE_EBONY = hex("#674846");
    public static final Color MAHOGANY = hex("#C04000");
    public static final Color BURNT_ORANGE = hex("#BF5700");
    public static final Color BURGUNDY = hex("#800020");
    public static final Color MAROON = hex("#800000");
    public static final Color REDWOOD = hex("#A45A52");
    public static final Color ROSEWOOD = hex("#65000B");
    public static final Color BRONZE = hex("#CD7F32");
    public static final Color LION = hex("#C19A6B");
    public static final Color RUST = hex("#B7410E");
    public static final Color KOMBU_GREEN = hex("#354230");
    public static final Color SAP_GREEN = hex("#123524");

    // black / off-black
    public static final Color BLACK = hex("#000000");
    public static final Color JET_BLACK = hex("#0E0E10");
    public static final Color LICORICE = hex("#1A1110");
    public static final Color RAISIN_BLACK = hex("#242124");
    public static final Color ONYX = hex("#353839");
    public static final Color OUTER_SPACE = hex("#2D383A");
    public static final Color BLACK_OLIVE = hex("#3B3C36");

    // mid-gray
    public static final Color GRAY = hex("#808080");
    public static final Color GAINSBORO = hex("#DCDCDC");
    public static final Color SILVER = hex("#C0C0C0");
    public static final Color MEDIUM_GRAY = hex("#BEBEBE");
    public static final Color SPANISH_GRAY = hex("#989898");
    public static final Color DAVYS_GRAY = hex("#555555");
    public static final Color TUNDORA = hex("#404040");
    public static final Color EBONY = hex("#555D50");

    // off-gray
    public static final Color CRAYOLA_GRAY = hex("#8B8680");
    public static final Color BATTLESHIP_GRAY = hex("#848482");
    public static final Color PLATINUM = hex("#E5E4E2");
    public static final Color ASH_GRAY = hex("#B2BEB5");
    public static final Color GUNMETAL = hex("#2A3439");
    public static final Color CHARCOAL = hex("#36454F");
    public static final Color SLATE_GRAY = hex("#708090");
    public static final Color GRAY_GREEN = hex("#5E716A");
    public static final Color TAUPE = hex("#483C32");
    public static final Color GREIGE = hex("#CCC2BA");

    // white / off-white
    public static final Color WHITE = hex("#FFFFFF");
    public static final Color NAVAJO_WHITE = hex("#f6dfb3");
    public static final Color PEACH = hex("#FFE5B4");
    public static final Color APRICOT_PEACH = hex("#F8C8B0");
    public static final Color CHAMPAGNE = hex("#F7E7CE");
    public static final Color PAPAYA_WHIP = hex("#FFEFD5");
    public static final Color VANILLA = hex("#F3E5AB");
    public static final Color HONEYDEW = hex("#F0FFF0");
    public static final Color MINT_CREAM = hex("#F5FFFA");
    public static final Color MISTY_ROSE = hex("#FFE4E1");
    public static final Color BEIGE = hex("#F5F5DC");
    public static final Color PALE_PURPLE = hex("#FAE6FA");
    public static final Color IVORY = hex("#FFFFF0");
    public static final Color SEASHELL = hex("#FFF5EE");
    public static final Color CORNSILK = hex("#FFF8DC");
    public static final Color OLD_LACE = hex("#FDF5E6");
    public static final Color WHITE_CHOCOLATE = hex("#EDE6D6");
    public static final Color LINEN = hex("#FAF0E6");
}