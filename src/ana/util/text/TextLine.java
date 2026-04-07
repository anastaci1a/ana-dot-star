package ana.util.text;


// dep

import java.util.Collections;
import java.util.List;


// main

public final class TextLine {
    // attr

    public final TextLineType type;
    public final List<TextGroup> groups;

    // constr

    public TextLine(List<TextGroup> groups) {
        this(TextLineType.P, groups);
    }

    public TextLine(TextLineType type, List<TextGroup> groups) {
        this.type = type;
        this.groups = Collections.unmodifiableList(
            groups
        );
    }

    // sys

    public String toString() {
        String className = getClass().getSimpleName();

        String tab = "  ";
        String groupsStr = groups.toString().replaceAll("\n", "\n" + tab);

        return String.format(
            "%s:{\n%s%s,\n%s%s\n}",
            className, tab, type.name(), tab, groupsStr
        );
    }

    public boolean isEmpty() {
        return groups.isEmpty();
    }
}