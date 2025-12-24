package ana.lang;

public class ConditionalAccessException extends RuntimeException {
    public ConditionalAccessException(String message) {
        super(message);
    }

    public ConditionalAccessException() {
        this("The return value of this operation may only be conditionally accessed.");
    }
}
