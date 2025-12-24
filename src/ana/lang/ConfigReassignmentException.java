package ana.lang;

public class ConfigReassignmentException extends Exception {
    public ConfigReassignmentException(String message) {
        super(message);
    }

    public ConfigReassignmentException() {
        this("Configuration has already been set and may not be reassigned.");
    }
}
