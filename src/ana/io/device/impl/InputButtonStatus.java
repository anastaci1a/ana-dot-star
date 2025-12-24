package ana.io.device.impl;


// main

public record InputButtonStatus(
    boolean tapped,   // FIRST frame of down
    boolean down,     //  ALL frames of down
    boolean released, // FIRST frame of up
    boolean up        //  ALL frames of up
) {
    // constr

    public InputButtonStatus() {
        this(false, false, false, true);
    }

    // static util

    public static InputButtonStatus or(InputButtonStatus status1, InputButtonStatus status2) {
        return new InputButtonStatus(
            status1.tapped || status2.tapped,
            status1.down || status2.down,
            status1.released || status2.released,
            status1.up || status2.up
        );
    }

    public static InputButtonStatus and(InputButtonStatus status1, InputButtonStatus status2) {
        return new InputButtonStatus(
            status1.tapped && status2.tapped,
            status1.down && status2.down,
            status1.released && status2.released,
            status1.up && status2.up
        );
    }
}
