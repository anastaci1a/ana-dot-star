package ana.io.device.keyboard;


// dep

import javafx.scene.input.KeyCode;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;


// main

public enum Key {
    A (KeyCode.A),
    B (KeyCode.B),
    C (KeyCode.C),
    D (KeyCode.D),
    E (KeyCode.E),
    F (KeyCode.F),
    G (KeyCode.G),
    H (KeyCode.H),
    I (KeyCode.I),
    J (KeyCode.J),
    K (KeyCode.K),
    L (KeyCode.L),
    M (KeyCode.M),
    N (KeyCode.N),
    O (KeyCode.O),
    P (KeyCode.P),
    Q (KeyCode.Q),
    R (KeyCode.R),
    S (KeyCode.S),
    T (KeyCode.T),
    U (KeyCode.U),
    V (KeyCode.V),
    W (KeyCode.W),
    X (KeyCode.X),
    Y (KeyCode.Y),
    Z (KeyCode.Z),

    UP      (KeyCode.UP     ),
    DOWN    (KeyCode.DOWN   ),
    LEFT    (KeyCode.LEFT   ),
    RIGHT   (KeyCode.RIGHT  ),
    SHIFT   (KeyCode.SHIFT  ),
    ENTER   (KeyCode.ENTER  ),
    CONTROL (KeyCode.CONTROL),
    ESCAPE  (KeyCode.ESCAPE ),

    FALLBACK (null);

    // attr, constr

    private final KeyCode fxInput;

    Key(KeyCode fxInput) {
        this.fxInput = fxInput;
    }

    // get

    public KeyCode fxCode() {
        return fxInput;
    }

    // static util

    public static Key from(KeyCode fxInput) {
        Key k = BY_FX_INPUTS.get(fxInput);
        return k == null ? FALLBACK : k;
    }

    // map config

    private static final Map<KeyCode, Key> BY_FX_INPUTS;
    static {
        var map = new EnumMap<KeyCode, Key>(KeyCode.class);
        for (Key k : values()) {
            if (k.fxCode() != null)
                map.put(k.fxInput, k);
        }
        BY_FX_INPUTS = Collections.unmodifiableMap(map);
    }
}