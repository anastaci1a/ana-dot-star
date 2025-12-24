package ana.io.device.impl;


// main

public class InputButtonManager {
    // attr

    private InputButtonStatus status;
    private boolean toTap, toRelease;

    // constr

    public InputButtonManager() {
        status = new InputButtonStatus();

        toTap     = false;
        toRelease = false;
    }

    // get

    public InputButtonStatus getStatus() {
        return status;
    }

    // mut

    public void down() {
        if (status.up()) {
            toTap = true;
            update(false, true, false, false);
        }
    }

    public void up() {
        if (status.down()) {
            toRelease = true;
            update(false, false, true, true);
        }
    }

    public void resolveFrame() {
        if (toTap || toRelease) {
            update(toTap, status.down(), toRelease, status.up());
            toTap = false;
            toRelease = false;
        } else if (status.tapped() || status.released()) {
            update(false, status.down(), false, status.up());
        }
    }

    // priv util

    private void update(boolean tapped, boolean down, boolean released, boolean up) {
        status = new InputButtonStatus(tapped, down, released, up);
    }
}
