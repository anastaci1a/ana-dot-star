package ana.io.cli;


// dep

import ana.io.file.FileExt;
import ana.util.color.Color;
import ana.util.text.*;
import ana.util.theme.Palette;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;


// main

public final class Cli {
    // no inst

    private Cli() {}

    // const

    public static final String INVALID_INPUT_TEXT = "(Invalid input)";
    public static final String PROMPT_PREFIX      = "> ";

    public static final String CHOOSE_FILE_PROMPT_DEFAULT = "Please select a file.";

    public static final String           CHOOSE_PROMPT_DEFAULT = "Please select an option.";
    public static final String           CHOOSE_EXIT_DEFAULT = "Exit";
    public static final Consumer<String> CHOOSE_PRINT_DEFAULT = Cli::h2;

    public static final Predicate<String> VERIFY_INPUT_DEFAULT = Cli::stringIsNotEmpty;
    public static final Consumer<String>  INPUT_PRINT_DEFAULT  = Cli::h4;

    // priv util

    private static String[] optionsToStrs(Option[] options) {
        String[] optionsStrs = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            optionsStrs[i] = options[i].title();
        }

        return optionsStrs;
    }

    // verifiers

    public static boolean stringIsNotEmpty(String input) {
        return !input.isEmpty();
    }

    public static boolean stringIsYN(String input) {
        String lower = input.toLowerCase();
        return lower.equals("y") || lower.equals("n");
    }

    // menu/choose specific

    public static String confirm(Scanner s, String prompt) {
        return input(s, prompt, Cli::stringIsYN).toLowerCase();
    }

    public static String[] chooseFileInDir(Scanner s, String dirStr) throws IOException {
        return chooseFileInDir(s, dirStr, CHOOSE_FILE_PROMPT_DEFAULT);
    }

    public static String[] chooseFileInDir(Scanner s, String dirStr, String prompt) throws IOException {
        dirStr = FileExt.dirStrip(dirStr);
        String[] filenames = FileExt.listDir(dirStr);

        int i = choose(s, prompt, filenames);
        String filePath = dirStr + "/" + filenames[i];

        return FileExt.readLines(filePath);
    }

    // choose, execute, choose, ... exit

    public static void menu(Scanner s, Option... options) {
        menu(s, CHOOSE_PROMPT_DEFAULT, options);
    }

    public static void menu(Scanner s, String prompt, Option... options) {
        menu(s, prompt, true, options);
    }

    public static void menu(Scanner s, String prompt, boolean exitOption, Option... options) {
        Looper looper = new Looper();

        Option[] optionsPlus;
        if (exitOption) {
            Option exit = new Option(CHOOSE_EXIT_DEFAULT, looper::stop);

            optionsPlus = new Option[options.length + 1];
            System.arraycopy(options, 0, optionsPlus, 0, options.length); // intellij told me to do this idk
            optionsPlus[optionsPlus.length - 1] = exit;
        } else optionsPlus = options;

        while (looper.test()) {
            choose(s, prompt, optionsPlus);
        }
    }

    // choose, execute

    public static void choose(Scanner s, Option... options) {
        choose(s, CHOOSE_PROMPT_DEFAULT, options);
    }

    public static void choose(Scanner s, String prompt, Option... options) {
        String[] optionsStrs = optionsToStrs(options);

        int choice = choose(s, prompt, optionsStrs);
        options[choice].toRun().run();
    }

    public static int choose(Scanner s, String prompt, String... options) {
        for (int i = 0; i < options.length; i++) {
            String str = options[i];
            prompt += String.format(
                "\n%d: %s", i + 1, str
            );
        }
        int optionsLength = options.length;

        String choiceStr = input(
            s, prompt,
            (input) -> {
                try {
                    int i = Integer.parseInt(input);
                    return 1 <= i && i <= optionsLength;
                } catch (NumberFormatException e) {
                    return false;
                }
            }, CHOOSE_PRINT_DEFAULT
        );

        return Integer.parseInt(choiceStr) - 1; // should never (NEVER) give an exception
    }

    // menu & choose util

    public record Option(String title, Runnable toRun) {}

    private static class Looper {
        private boolean looping;

        public Looper() {
            looping = true;
        }

        public void stop() {
            looping = false;
        }

        public boolean test() {
            return looping;
        }
    }

    // input

    public static String input(Scanner s, String prompt) {
        return input(s, prompt, VERIFY_INPUT_DEFAULT);
    }

    public static String input(Scanner s, String prompt, Predicate<String> verifyInput) {
        return input(s, prompt, verifyInput, INPUT_PRINT_DEFAULT);
    }

    public static String input(Scanner s, String prompt, Predicate<String> verifyInput, Consumer<String> printPrompt) {
        printPrompt.accept(prompt);
        System.out.print(PROMPT_PREFIX);

        String inputStr = "";

        boolean invalidInput = true;
        while (invalidInput) {
            inputStr = s.nextLine();

            if (verifyInput.test(inputStr)) {
                invalidInput = false;
            } else {
                System.out.print(INVALID_INPUT_TEXT + "\n" + PROMPT_PREFIX);
            }
        }

        return inputStr;
    }

    // default text types shortcuts

    public static void h1(String txt) {
        TextType.H1.printf(txt, System.out);
    }
    public static void h1(PrintStream ps, String txt) {
        TextType.H1.printf(txt, ps);
    }
    public static void h1(PrintWriter pw, String txt) {
        TextType.H1.printf(txt, pw);
    }

    public static void h2(String txt) {
        TextType.H2.printf(txt, System.out);
    }
    public static void h2(PrintStream ps, String txt) {
        TextType.H2.printf(txt, ps);
    }
    public static void h2(PrintWriter pw, String txt) {
        TextType.H2.printf(txt, pw);
    }

    public static void h3(String txt) {
        TextType.H3.printf(txt, System.out);
    }
    public static void h3(PrintStream ps, String txt) {
        TextType.H3.printf(txt, ps);
    }
    public static void h3(PrintWriter pw, String txt) {
        TextType.H3.printf(txt, pw);
    }

    public static void h4(String txt) {
        TextType.H4.printf(txt, System.out);
    }
    public static void h4(PrintStream ps, String txt) {
        TextType.H4.printf(txt, ps);
    }
    public static void h4(PrintWriter pw, String txt) {
        TextType.H4.printf(txt, pw);
    }

    // default text family shortcut(s)

    public static void mdPrintf(String format, Object... args) {
        TextFamily.DEFAULT.mdPrintf(format, args);
    }

    // text type

    public static class TextType {
        // -- static --

        // const

        public static final String CHARSET_DEFAULT = "─│╭╮╰╯•";
        public static final int    SPECIALS_AMOUNT_DEFAULT = 2;

        public static final Palette PALETTE_DEFAULT = new Palette(
            Color.AZURE,
            Color.AQUAMARINE
        );

        public static final TextGroupStyle STYLE_DEFAULT = new TextGroupStyle(
            Color.WHITE
        );

        // inst

        public static final TextType H1 = new TextType(
            /* top  / bottom */ true, true,
            /* left / right  */ true, true
        );

        public static final TextType H2 = new TextType(
            /* top  / BOTTOM */ false, true,
            /* LEFT / right  */ true,  false
        );

        public static final TextType H3 = new TextType(
            /* top  / bottom */ false, false,
            /* LEFT / RIGHT  */ true,  true
        );

        public static final TextType H4 = new TextType(
            /* top  / BOTTOM */ false, true,
            /* left / right  */ false, false
        );

        public static final TextType QUOTE = new TextType(
            /* top  / bottom */ false, false,
            /* LEFT / right  */ true,  false
        );

        public static final TextType P = new TextType(
            /* top  / bottom */ false, false,
            /* left / right  */ false, false
        );

        // -- inst --

        // const

        public final String charset;

        public final boolean topLine, bottomLine, leftLine, rightLine;

        public final Palette        palette;
        public final TextGroupStyle style;
        public final Color          lineCol, cornerCol;

        // constr

        public TextType(Palette palette) {
            this(palette, STYLE_DEFAULT);
        }

        public TextType(Palette palette, TextGroupStyle style) {
            this(
                palette, style, CHARSET_DEFAULT
            );
        }

        public TextType(Palette palette, TextGroupStyle style, String charset) {
            this(
                palette, style, charset,
                H1.topLine,  H1.bottomLine,
                H1.leftLine, H1.rightLine
            );
        }

        public TextType(
            boolean topLine,
            boolean bottomLine,
            boolean leftLine,
            boolean rightLine
        ) {
            this(
                PALETTE_DEFAULT, STYLE_DEFAULT, CHARSET_DEFAULT,

                topLine,
                bottomLine,
                leftLine,
                rightLine
            );
        }

        public TextType(
            Palette        palette,
            TextGroupStyle style,
            String         charset,

            boolean topLine,
            boolean bottomLine,
            boolean leftLine,
            boolean rightLine
        ) {
            this.palette   = palette;
            this.style = style;

            this.lineCol   = palette.primary();
            this.cornerCol = palette.secondary();

            this.charset = charset;

            this.topLine    = topLine;
            this.bottomLine = bottomLine;
            this.leftLine   = leftLine;
            this.rightLine  = rightLine;
        }

        // print

        public void printf(String format, Object... args) {
            printf(format, System.out, args);
        }

        public void printf(String format, PrintStream ps, Object... args) {
            ps.println(
                format(format, args)
            );
        }

        public void printf(String format, PrintWriter pw, Object... args) {
            pw.println(
                format(format, args)
            );
        }

        // format

        public String format(String format, Object... args) {
            String text = String.format(format, args);

            // strip
            String text_stripped = Ansi.stripCodes(text);

            // text init
            String[] textSplit = text.split("\n");
            String[] textSplit_stripped = text_stripped.split("\n");

            // line calc
            int longest = maxLineLength(textSplit_stripped);
            int lineLength = getLineLength(longest);

            // format
            String textf = "";
            if (longest != lineLength) {
                for (int i = 0; i < textSplit_stripped.length; i++) {
                    String str = textSplit[i];
                    textf += formatText(str, i, textSplit_stripped.length, lineLength) + "\n";
                }
            } else {
                textf = Ansi.format(text, style);
            }

            // build
            return String.format(
                "\n%s%s%s",
                topLineStr(lineLength),
                textf,
                bottomLineStr(lineLength)
            );
        }

        // get clone

        public TextType withModif(
            Palette        palette,
            TextGroupStyle textStyle,
            String         charset
        ) {
            Palette        p = Optional.ofNullable(palette  ).orElse(this.palette);
            TextGroupStyle s = Optional.ofNullable(textStyle).orElse(this.style);
            String         c = Optional.ofNullable(charset  ).orElse(this.charset);

            return new TextType(
                p, s, c,
                topLine,  bottomLine,
                leftLine, rightLine
            );
        }

        // priv get

        public String chLineH()   { return fCharAt(0,   lineCol); }
        public String chLineV()   { return fCharAt(1,   lineCol); }
        public String chCnrTL()   { return fCharAt(2, cornerCol); }
        public String chCnrTR()   { return fCharAt(3, cornerCol); }
        public String chCnrBL()   { return fCharAt(4, cornerCol); }
        public String chCnrBR()   { return fCharAt(5, cornerCol); }
        public String chSpecial() { return fCharAt(6, cornerCol); }

        private String fCharAt(int i, Color col) {
            return Ansi.format(Character.toString(charset.charAt(i)), col);
        }

        // priv format

        private String formatText(String text, int row, int rowsTotal, int lineLength) {
            // md parse
            text = Ansi.format(text, style); // style (additive)

            // chars l/r to text
            String leftChar  = getSideChar(row, rowsTotal, leftLine,  chLineV());
            String rightChar = getSideChar(row, rowsTotal, rightLine, chLineV());

            String left = leftChar + " " + text;

            // padding
            int paddingAmount = lineLength - Ansi.stripCodes(left).length() - 1;
            String padding = " ".repeat(paddingAmount);

            return left + padding + rightChar;
        }

        private String getSideChar(int row, int rowsTotal, boolean side, String sideStr) {
            if (side) return sideStr;

            return includeSpecialOnVSide(row, rowsTotal, side)
                   ? chSpecial()
                   : (topLine || bottomLine ? " " : "");
        }

        // priv str building

        private String topLineStr(int lineLength) {
            return horiLineStr(topLine, lineLength, chCnrTL(), chCnrTR());
        }

        private String bottomLineStr(int lineLength) {
            return horiLineStr(bottomLine, lineLength, chCnrBL(), chCnrBR());
        }

        private String horiLineStr(boolean side, int lineLength, String leftCorner, String rightCorner) {
            String ch = side ? Ansi.stripCodes(chLineH()) : " ";
            String line = ch.repeat(Math.max(lineLength - 2, 0));

            if (!side) {
                String specials = getSpecialsOnHSides();

                String left  = Ansi.stripCodes(leftCorner + " " + specials);
                String right = Ansi.stripCodes(specials + " " + rightCorner);
                if (!leftLine)  left = "";
                if (!rightLine) right = "";

                int padding = Math.max(0, lineLength - left.length() - right.length());

                line = left + " ".repeat(padding) + right;
                line = line.isBlank() ? "" : Ansi.format(line, cornerCol) + "\n";
            } else {
                line = Ansi.format(line, lineCol);
                line = leftCorner + line + rightCorner + "\n";
            }

            return line;
        }

        private String getSpecialsOnHSides() {
            String ch = (Ansi.stripCodes(chSpecial()) + " ");
            return ch.repeat(SPECIALS_AMOUNT_DEFAULT).strip();
        }

        private int getLineLength(int maxTextLength) {
            int padding = 0;

            if      (topLine  || bottomLine) padding = 4;
            else if (leftLine && rightLine)  padding = 4;
            else if (leftLine || rightLine)  padding = 3;

            return maxTextLength + padding;
        }

        private boolean includeSpecialOnVSide(int row, int rowsTotal, boolean side) {
            boolean topInRange    = row < SPECIALS_AMOUNT_DEFAULT;
            boolean bottomInRange = row >= rowsTotal - SPECIALS_AMOUNT_DEFAULT;

            boolean top    = topLine    && !side && topInRange;
            boolean bottom = bottomLine && !side && bottomInRange;

            return top || bottom;
        }

        // priv util

        private static int maxLineLength(String[] lines) {
            int longest = 0;
            for (String l : lines)
                if (l.length() > longest) longest = l.length();

            return longest;
        }
    }

    // text family

    public static class TextFamily extends HashMap<TextLineType, TextType> {
        // -- static --

        public static TextFamily DEFAULT = new TextFamily(Map.of(
            TextLineType.H1, TextType.H1,
            TextLineType.H2, TextType.H2,
            TextLineType.H3, TextType.H3,
            TextLineType.H4, TextType.H4,

            TextLineType.QUOTE, TextType.QUOTE,

            TextLineType.P, TextType.P
        ));

        // -- inst --

        // constr

        public TextFamily(Map<? extends TextLineType, ? extends TextType> m) {
            super(m);

            // add default
            if (!containsKey(TextLineType.P)) put(
                TextLineType.P, TextType.P
            );
        }

        // get

        public TextType get(TextLineType type) {
            if (!containsKey(type)) return get(TextType.P); // will always exist

            return super.get(type);
        }

        // print

        public void mdPrintf(String format, Object... args) {
            mdPrintf(format, System.out, args);
        }

        public void mdPrintf(String format, PrintStream ps, Object... args) {
            ps.println(
                mdFormat(format, args)
            );
        }

        public void mdPrintf(String format, PrintWriter pw, Object... args) {
            pw.println(
                mdFormat(format, args)
            );
        }

        // format

        public String mdFormat(String format, Object... args) {
            Text text = Text.mdParse(format, args);

            return mdFormat(text);
        }

        public String mdFormat(Text text) {
            String str = "";

            for (TextLine line : text.lines) {
                str += mdFormat(line).strip() + "\n\n";
            }

            return str.strip();
        }

        public String mdFormat(TextLine line) {
            String str = "";

            for (TextGroup group : line.groups) {
                str += Ansi.format(group);
            }

            str = get(line.type).format(str);

            return str;
        }

        // get clone

        public TextFamily withModif(
            Palette        palette,
            TextGroupStyle textStyle,
            String         charset
        ) {
            Map<TextLineType, TextType> newFamilyMap = new HashMap<>();

            for (TextLineType key : this.keySet()) {
                TextType type = get(key).withModif(palette, textStyle, charset);

                newFamilyMap.put(key, type);
            }

            return new TextFamily(newFamilyMap);
        }
    }
}
