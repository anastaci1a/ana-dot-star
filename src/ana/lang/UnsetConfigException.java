package ana.lang;

public class UnsetConfigException extends RuntimeException {
    public UnsetConfigException(String message) {
        super(message);
    }

    public UnsetConfigException() {
        this("Necessary configuration was not set before performing this operation.");
    }
}
