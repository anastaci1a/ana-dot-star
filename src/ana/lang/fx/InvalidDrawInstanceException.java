package ana.lang.fx;

public class InvalidDrawInstanceException extends RuntimeException {
    public InvalidDrawInstanceException(String message) {
        super(message);
    }

    public InvalidDrawInstanceException() {
        this("The provided Draw instance may not have preconfigured write specifications.");
    }
}
