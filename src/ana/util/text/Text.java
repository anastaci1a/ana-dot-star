package ana.util.text;


// dep

import java.util.ArrayList;
import java.util.List;


// main

public class Text {
    // const

    private static final TextLineType LINE_TYPE_DEFAULT = TextLineType.P;

    // attr

    public final List<TextLine> lines;

    // priv constr

    private Text() {
        this.lines = null;
    }

    private Text(List<TextLine> lines) {
        this.lines = lines;
    }

    // sys

    public String toString() {
        String className = getClass().getSimpleName();

        String tab = "  ";
        String linesStr = lines.toString().replaceAll("\n", "\n" + tab);

        return String.format(
            "\n%s:{\n%s%s\n}",
            className, tab, linesStr
        );
    }

    public TextLine toLine() {
        return toLine(TextLineType.P);
    }

    public TextLine toLine(TextLineType lineType) {
        return toLine(lineType, "\n", "");
    }

    public TextLine toLine(TextLineType lineType, String prefixSep, String suffixSep) {
        List<TextGroup> allGroups = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            List<TextGroup> groups = lines.get(i).groups;
            for (int j = 0; j < groups.size(); j++) {
                TextGroup group = groups.get(i);
                if (i != 0 && j == 0) {
                    allGroups.add(new TextGroup(
                        prefixSep + group.str, // if first group of non-0th line, add prefix sep
                        group.style
                    ));
                } else if (i != lines.size() - 1 && j == groups.size() - 1) {
                    allGroups.add(new TextGroup(
                        group.str + suffixSep, // if last group of non-last line, add suffix sep
                        group.style
                    ));
                } else {
                    allGroups.add(group);
                }
            }
        }

        return new TextLine(
            lineType,
            allGroups
        );
    }

    // parsing (markdown)

    public static Text mdParse(String format, Object... args) {
        return mdParse(format, null, args);
    }

    public static Text mdParse(String format, TextLineType lineTypeOverride, Object... args) {
        String str = String.format(format, args);

        String[] linesUnparsed = str.split("\n");
        List<TextLine> lines = new ArrayList<>();

        for (String lineStr : linesUnparsed) {
            List<TextGroup> groups = new ArrayList<>(List.of(new TextGroup(lineStr)));
            TextLine line;

            // parse groups

            for (MDTextAttr mdAttr : MDTextAttr.values()) {
                groups = mdParseAttr(mdAttr, groups);
            }

            // parse type

            if (lineTypeOverride != null) { // override type
                line = new TextLine(lineTypeOverride, groups);
            }

            else { // parse type
                line = new TextLine(LINE_TYPE_DEFAULT, groups);

                for (MDTextType mdType : MDTextType.values()) {
                    TextLine lineNullable = mdParseType(mdType, groups);
                    if (lineNullable != null) {
                        line = lineNullable;
                        break;
                    }
                }
            }

            lines.add(line);
        }

        return new Text(lines);
    }

    public static TextLine mdParseType(MDTextType mdType, List<TextGroup> groups) {
        if (groups.isEmpty()) return null; // no groups
        TextGroup firstGroup = groups.getFirst(); // first group
                                                  // (types only match beginning of lines)

        for (String match : mdType.matches) {
            String temp = "AFTERAFTERAFTERAFTER";
            String[] parsedStrSplit = (firstGroup.str + temp).split(match);

            if (parsedStrSplit.length != 2) break; // no match
            String parsedStr = parsedStrSplit[1].replaceAll(temp, "");

            // replace first group's str with parsed if non-empty
            groups.removeFirst();
            if (!parsedStr.isEmpty()) {
                groups.addFirst(new TextGroup(
                    parsedStr, firstGroup.style
                ));
            }

            // create line & return
            return new TextLine(
                mdType.lineType, groups
            );
        }

        return null; // no matches
    }

    private static List<TextGroup> mdParseAttr(MDTextAttr mdAttr, List<TextGroup> groups) {
        List<TextGroup> newGroups = new ArrayList<>();

        int carry = 0; // whether to carry the style of prev group (when =1)

        for (TextGroup group : groups) {
            TextGroupStyle existingStyle = group.style;
            List<TextGroup> parsedGroups = new ArrayList<>(); // subgroups (split) of current group

            for (String match : mdAttr.matches) {
                String[] split = group.str.split(match, -1);

                if (split.length > 1 || carry == 1) { // if matched or carried
                    // make new groups
                    for (int i = 0; i < split.length; i++) {
                        String strPart = split[i];

                        TextGroupStyle newStyle;
                        if ((i + carry) % 2 == 1) { // odd (>1), or carried
                            // combine styles
                            List<TextAttr> newAttrs = new ArrayList<>(
                                existingStyle.attrs
                            );
                            if (mdAttr.textAttr != null) newAttrs.add(mdAttr.textAttr);

                            newStyle = new TextGroupStyle(
                                existingStyle.col,
                                newAttrs.toArray(TextAttr[]::new)
                            );
                        } else {
                            newStyle = existingStyle;
                        }

                        // subgroup finished
                        TextGroup parsedGroup = new TextGroup(strPart, newStyle);
                        if (!parsedGroup.str.isEmpty()) parsedGroups.add(parsedGroup);
                    }

                    carry = (split.length + carry + 1) % 2; // if even, flip the carry

                    break; // group finished (broken up, parsed)
                }
            }

            // nothing was parsed, add unchanged group
            if (parsedGroups.isEmpty()) parsedGroups.add(group);

            // add all non-empty parsed groups
            for (TextGroup g : parsedGroups) {
                if (!g.isEmpty()) newGroups.add(g);
            }
        }

        return newGroups;
    }

    // priv md util

    public enum MDTextType {
        // text types (ordered by priority)

        H4 (TextLineType.H4, "^#### "),
        H3 (TextLineType.H3, "^### " ),
        H2 (TextLineType.H2, "^## "  ),
        H1 (TextLineType.H1, "^# "   ),

        QUOTE (TextLineType.QUOTE, "^> "),

        P (TextLineType.P, "");

        // attr

        public final String[] matches;
        public final TextLineType lineType;

        // constr

        MDTextType(TextLineType lineType, String... matches) {
            this.lineType = lineType;
            this.matches  = matches;
        }
    }

    public enum MDTextAttr {
        // text attrs (ordered by priority)

        BOLD          (TextAttr.BOLD,          "\\*\\*"    ),
        UNDERLINE     (TextAttr.UNDERLINE,     "\\_\\_"    ),
        ITALIC        (TextAttr.ITALIC,        "\\*", "\\_"),
        STRIKETHROUGH (TextAttr.STRIKETHROUGH, "\\~\\~"    );

        // attr

        public final String[] matches;
        public final TextAttr textAttr;

        // constr

        MDTextAttr(TextAttr textAttr, String... matches) {
            this.textAttr = textAttr;
            this.matches  = matches; // escapeStrs(matches);
        }
    }
}
