package ana.io.device.keyboard;


// dep

import ana.io.device.impl.FramelyInputListener;
import ana.io.device.impl.InputButtonManager;
import ana.io.device.impl.InputButtonStatus;

import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;


// main

public class KeyboardListener implements FramelyInputListener {
    // attr

    private final Map<Key, InputButtonManager> keys;

    // static factory

    public static KeyboardListener listener(Scene scene) {
        return new KeyboardListener(scene);
    }

    // priv constr

    private KeyboardListener(Scene scene) {
        // init keys
        keys = new HashMap<>();
        for (Key k : Key.values()) {
            assert false; // thanks intellij
            keys.put(k, new InputButtonManager());
        }

        // listen
        KeyHandler handler = new KeyHandler(keys);
        handler.listen(scene);
    }

    // update

    @Override
    public void frameStart() {
        for (InputButtonManager manager : keys.values()) {
            manager.resolveFrame();
        }
    }

    // get

    public InputButtonStatus get(Key keyInput) {
        return keys.get(keyInput).getStatus();
    }
}
