package ana.io.cli;


// dep

import ana.io.file.FileExt;
import ana.util.color.Color;
import ana.util.style.Palette;
import ana.util.style.TextAttr;
import ana.util.style.TextStyle;

import java.io.*;
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

    // headings

    public static void h1(String txt) {
        h1(System.out, txt);
    }

    public static void h1(PrintStream osw, String txt) {
        osw.print(
            Heading.H1.format(txt)
        );
    }

    public static void h1(PrintWriter pw, String txt) {
        pw.print(
            Heading.H1.format(txt)
        );
    }

    public static void h2(String txt) {
        h2(System.out, txt);
    }

    public static void h2(PrintStream ps, String txt) {
        ps.print(
            Heading.H2.format(txt)
        );
    }

    public static void h2(PrintWriter pw, String txt) {
        pw.print(
            Heading.H2.format(txt)
        );
    }

    public static void h3(String txt) {
        h3(System.out, txt);
    }

    public static void h3(PrintStream ps, String txt) {
        ps.print(
            Heading.H3.format(txt)
        );
    }

    public static void h3(PrintWriter pw, String txt) {
        pw.print(
            Heading.H3.format(txt)
        );
    }

    public static void h4(String txt) {
        h4(System.out, txt);
    }

    public static void h4(PrintStream ps, String txt) {
        ps.print(
            Heading.H4.format(txt)
        );
    }

    public static void h4(PrintWriter pw, String txt) {
        pw.print(
            Heading.H4.format(txt)
        );
    }

    // heading

    public static final class Heading {
        // no inst

        private Heading() {}

        // const

        public static final String HEADING_CHARSET_DEFAULT = "─│╭╮╰╯•";
        public static final int    SPECIALS_AMOUNT_DEFAULT = 2;

        public static final Palette PALETTE_DEFAULT = new Palette(
            Color.AZURE,
            Color.AQUAMARINE
        );

        public static final TextStyle TEXT_STYLE_DEFAULT = new TextStyle(
            Color.WHITE, TextAttr.BOLD
        );


        // const ref

        public static final Style H1 = new Style(
            /* top  / bottom */ true, true,
            /* left / right  */ true, true
        );

        public static final Style H2 = new Style(
            /* top  / bottom */ false, true,
            /* left / right  */ true,  false
        );

        public static final Style H3 = new Style(
            /* top  / bottom */ false, false,
            /* left / right  */ true,  true
        );

        public static final Style H4 = new Style(
            /* top  / bottom */ false, true,
            /* left / right  */ false, false
        );

        public static final Style H5 = new Style(
            /* top  / bottom */ false, false,
            /* left / right  */ false, false
        );

        // inner

        public static class Style {
            public final String charset;

            public final boolean topLine, bottomLine, leftLine, rightLine;

            public final Palette   palette;
            public final TextStyle textStyle;
            public final Color     lineCol, cornerCol;

            // constr

            public Style (Palette palette) {
                this(palette, TEXT_STYLE_DEFAULT);
            }

            public Style (Palette palette, TextStyle textStyle) {
                this(
                    palette, textStyle,
                    HEADING_CHARSET_DEFAULT
                );
            }

            public Style (Palette palette, TextStyle textStyle, String charset) {
                this(
                    palette, textStyle, charset,
                    H1.topLine,  H1.bottomLine,
                    H1.leftLine, H1.rightLine
                );
            }

            public Style (
                boolean topLine,
                boolean bottomLine,
                boolean leftLine,
                boolean rightLine
            ) {
                this(
                    PALETTE_DEFAULT,
                    TEXT_STYLE_DEFAULT,
                    HEADING_CHARSET_DEFAULT,

                    topLine,
                    bottomLine,
                    rightLine,
                    leftLine
                );
            }

            public Style (
                Palette   palette,
                TextStyle textStyle,
                String    charset,

                boolean topLine,
                boolean bottomLine,
                boolean leftLine,
                boolean rightLine
            ) {
                this.palette   = palette;
                this.textStyle = textStyle;

                this.lineCol   = palette.primary();
                this.cornerCol = palette.secondary();

                this.charset = charset;

                this.topLine    = topLine;
                this.bottomLine = bottomLine;
                this.rightLine  = rightLine;
                this.leftLine   = leftLine;
            }

            // method

            public String format(String text) {
                // text init
                String[] textSplit = text.split("\n");

                // line calc
                int longest = maxLineLength(textSplit);
                int lineLength = getLineLength(longest);

                // format
                String textf = "";
                if (longest != lineLength) {
                    for (int i = 0; i < textSplit.length; i++) {
                        String str = Ansi.stripCodes(textSplit[i]);
                        textf += formatText(str, i, textSplit.length, lineLength) + "\n";
                    }
                } else {
                    textf = String.join("\n", textSplit) + "\n";
                    textf = Ansi.format(textf, textStyle);
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

            public Style withModif(
                Palette   palette,
                TextStyle textStyle,
                String    charset
            ) {
                Palette   p = Optional.ofNullable(palette  ).orElse(this.palette);
                TextStyle s = Optional.ofNullable(textStyle).orElse(this.textStyle);
                String    c = Optional.ofNullable(charset  ).orElse(this.charset);

                return new Style(
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
                text = Ansi.format(text, textStyle);

                String leftChar  = getSideChar(row, rowsTotal, leftLine,  chLineV());
                String rightChar = getSideChar(row, rowsTotal, rightLine, chLineV());

                String left = leftChar + " " + text;

                int paddingAmount = lineLength - Ansi.stripCodes(left).length() - 1;
                String padding = " ".repeat(paddingAmount);

                return left + padding + rightChar;
            }

            private String getSideChar(int row, int rowsTotal, boolean side, String sideStr) {
                if (side) return sideStr;

                String ch = includeSpecialOnVSide(row, rowsTotal, side)
                            ? chSpecial()
                            : (topLine || bottomLine ? " " : "");

                return ch;
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
                String line = ch.repeat(lineLength - 2);

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
    }
}
