package ana.io.device;


// dep

import ana.io.device.impl.FramelyInputListener;
import ana.io.device.impl.InputButtonStatus;
import ana.io.device.keyboard.Key;
import ana.io.device.keyboard.KeyboardListener;
import ana.io.device.mouse.Mouse;
import ana.io.device.mouse.MouseListener;
import ana.util.math.vector.point.DoublePoint;

import javafx.scene.Scene;
import javafx.stage.Stage;


// main

public class DeviceInput implements FramelyInputListener {
    // attr

    private final MouseListener mouse;
    private final KeyboardListener keyboard;

    // static factory

    public static DeviceInput listener(Stage stage, Scene scene) {
        return new DeviceInput(stage, scene);
    }

    // priv constr

    private DeviceInput(Stage stage, Scene scene) {
        keyboard = KeyboardListener.listener(scene);
        mouse = MouseListener.listener(stage, scene);
    }

    // update

    @Override
    public void frameStart() {
        mouse.frameStart();
        keyboard.frameStart();
    }

    // get

    public InputButtonStatus get(Key keyInput) {
        return keyboard.get(keyInput);
    }

    public InputButtonStatus get(Mouse mouseInput) {
        return mouse.get(mouseInput);
    }

    public DoublePoint getMouse() {
        return mouse.getPos();
    }

    // getx

    public InputButtonStatus getOr(Key... keyInputs) {
        InputButtonStatus combined = new InputButtonStatus(
            false,
            false,
            false,
            false
        );

        for (Key k : keyInputs) {
            combined = InputButtonStatus.or(combined, get(k));
        }

        return combined;
    }

    public InputButtonStatus getAnd(Key... keyInputs) {
        InputButtonStatus combined = new InputButtonStatus(
            false,
            false,
            false,
            false
        );

        for (Key k : keyInputs) {
            combined = InputButtonStatus.and(combined, get(k));
        }

        return combined;
    }
}
