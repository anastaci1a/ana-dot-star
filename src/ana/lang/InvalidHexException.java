package ana.lang;

public class InvalidHexException extends RuntimeException {
    public InvalidHexException(String format, Object... args) {
        super(String.format(format, args));
    }

    public InvalidHexException(String hex) {
        this(
            "Invalid hex format for %s, either \"#RRGGBB\" or \"#RRGGBBAA\".",
            hex
        );
    }
}