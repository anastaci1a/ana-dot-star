package ana.io.device.mouse;


// dep

import javafx.scene.input.MouseButton;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;


// main

public enum Mouse {
    LEFT   (MouseButton.PRIMARY  ),
    RIGHT  (MouseButton.SECONDARY),
    MIDDLE (MouseButton.MIDDLE   ),

    FALLBACK (null);

    // attr, constr

    private final MouseButton fxInput;

    Mouse(MouseButton fxInput) {
        this.fxInput = fxInput;
    }

    // get

    public MouseButton fxInput() {
        return fxInput;
    }

    // static util

    public static Mouse from(MouseButton fxInput) {
        Mouse m = BY_FX_INPUTS.get(fxInput);
        return m == null ? FALLBACK : m;
    }

    // map config

    private static final Map<MouseButton, Mouse> BY_FX_INPUTS;
    static {
        var map = new EnumMap<MouseButton, Mouse>(MouseButton.class);
        for (Mouse m : values()) {
            if (m.fxInput() != null)
                map.put(m.fxInput, m);
        }
        BY_FX_INPUTS = Collections.unmodifiableMap(map);
    }
}