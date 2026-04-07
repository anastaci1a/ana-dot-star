package ana.util.text;


// main

public final class TextGroup {
    // attr

    public final String str;
    public final TextGroupStyle style;

    // constr

    public TextGroup(String str, TextGroupStyle style) {
        this.str = str;
        this.style = style;
    }

    public TextGroup(String str) {
        this.str = str;
        this.style = new TextGroupStyle();
    }

    // sys

    public String toString() {
        String className = getClass().getSimpleName();

        String tab = "  ";

        return String.format(
            "%s:{\n%s\"%s\",\n%s%s\n}",
            className, tab, str, tab, style.toString()
        );
    }

    public boolean isEmpty() {
        return str.isEmpty();
    }
}