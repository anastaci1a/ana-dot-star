package ana.io.device.keyboard;


// dep

import ana.io.device.impl.InputButtonManager;
import ana.io.device.impl.InputListener;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Map;


// main

public class KeyHandler implements InputListener {
    // attr

    private final Map<Key, InputButtonManager> keys;
    private final EventHandler<KeyEvent> pressedHandler, releasedHandler;

    // constr

    public KeyHandler(Map<Key, InputButtonManager> keys) {
        this.keys = keys;

        this.pressedHandler  = new PressedHandler();
        this.releasedHandler = new ReleasedHandler();
    }

    // impl

    @Override
    public void listen(Scene scene) {
        scene.setOnKeyPressed(pressedHandler);
        scene.setOnKeyReleased(releasedHandler);
    }

    // inner

    private class PressedHandler implements EventHandler<KeyEvent> {
        // impl
        @Override
        public void handle(KeyEvent event) {
            KeyCode fxInput = event.getCode();
            Key input = Key.from(fxInput);

            if (input != Key.FALLBACK) update(input);
        }

        // update keys
        protected void update(Key input) {
            keys.get(input).down();
        }
    }

    private class ReleasedHandler extends PressedHandler {
        // update keys
        @Override
        protected void update(Key input) {
            keys.get(input).up();
        }
    }
}
