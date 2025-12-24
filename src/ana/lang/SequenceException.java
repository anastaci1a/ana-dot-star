package ana.lang;

public class SequenceException extends RuntimeException {
    public SequenceException(String message) {
        super(message);
    }

    public SequenceException(String className, String sequenceName) {
        super(String.format(
            "An uncaught exception was thrown during %s %s's %s sequence.", (
                String.valueOf(className.charAt(0)).matches("[aeiouAEIOU]")
                ? "an" : "a"
            ), className, sequenceName
        ));
    }
}
