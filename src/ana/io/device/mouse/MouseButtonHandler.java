package ana.io.device.mouse;


// dep

import ana.io.device.impl.InputButtonManager;
import ana.io.device.impl.InputListener;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Map;


// main

public class MouseButtonHandler implements InputListener {
    // attr

    private final Map<Mouse, InputButtonManager> mouseInputs;
    private final EventHandler<MouseEvent> pressedHandler, releasedHandler;

    // constr

    public MouseButtonHandler(Map<Mouse, InputButtonManager> mouseInputs) {
        this.mouseInputs = mouseInputs;

        this.pressedHandler  = new PressedHandler();
        this.releasedHandler = new ReleasedHandler();
    }

    // impl

    @Override
    public void listen(Scene scene) {
        scene.setOnMousePressed(pressedHandler);
        scene.setOnMouseReleased(releasedHandler);
    }

    // inner

    private class PressedHandler  implements EventHandler<MouseEvent> {
        // impl
        @Override
        public void handle(MouseEvent event) {
            MouseButton fxInput = event.getButton();
            Mouse input = Mouse.from(fxInput);

            if (input != Mouse.FALLBACK) update(input);
        }

        // update keys
        protected void update(Mouse input) {
            mouseInputs.get(input).down();
        }
    }

    private class ReleasedHandler extends PressedHandler {
        // update keys
        @Override
        protected void update(Mouse input) {
            mouseInputs.get(input).up();
        }
    }
}
