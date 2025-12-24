package ana.lang.fx;

import ana.lang.SequenceException;

public class LauncherSequenceException extends SequenceException {
    public LauncherSequenceException(String sequenceName) {
        super("Launcher", sequenceName);
    }

    public LauncherSequenceException() {
        this("launch");
    }
}
