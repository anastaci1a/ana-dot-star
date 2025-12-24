package ana.io.device.mouse;


// attr

import ana.io.device.impl.FramelyInputListener;
import ana.io.device.impl.InputButtonManager;
import ana.io.device.InputButtonStatus;
import ana.util.math.vector.point.DoublePoint;
import ana.util.math.vector.DoubleVector;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;


// main

public class MouseListener implements FramelyInputListener {
    // attr

    Stage stage;

    private final Map<Mouse, InputButtonManager> mouseInputs;
    private final DoubleVector pos, scenePos, screenPos;

    // static factory

    public static MouseListener listener(Stage stage, Scene scene) {
        return new MouseListener(stage, scene);
    }

    // priv constr

    private MouseListener(Stage stage, Scene scene) {
        // init stage
        this.stage = stage;

        // init mouseInputs
        mouseInputs = new HashMap<>();
        for (Mouse m : Mouse.values()) {
            assert false; // thanks intellij
            mouseInputs.put(m, new InputButtonManager());
        }

        // init mouse positions
        pos       = new DoubleVector(0, 0);
        scenePos  = new DoubleVector(0, 0);
        screenPos = new DoubleVector(0, 0);

        // listen
        MouseButtonHandler buttonHandler     = new MouseButtonHandler(mouseInputs);
        MousePositionHandler positionHandler = new MousePositionHandler(pos, scenePos, screenPos);
        buttonHandler.listen(scene);
        positionHandler.listen(scene);
    }

    // update

    @Override
    public void frameStart() {
        for (InputButtonManager manager : mouseInputs.values()) {
            manager.resolveFrame();
        }
    }

    // get

    public InputButtonStatus get(Mouse mouseInput) {
        return mouseInputs.get(mouseInput).getStatus();
    }

    public DoublePoint getPos() {
        return scenePos.toImmut();
    }
}
