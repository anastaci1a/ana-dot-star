package ana.io.device.mouse;


// dep

import ana.io.device.impl.InputListener;
import ana.util.math.vector.DoubleVector;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;


// main

public class MousePositionHandler implements InputListener {
    // attr

    private final DoubleVector pos, scenePos, screenPos;
    EventHandler<MouseEvent> movedHandler;

    // constr

    public MousePositionHandler(DoubleVector pos, DoubleVector scenePos, DoubleVector screenPos) {
        this.pos       = pos;
        this.scenePos  = scenePos;
        this.screenPos = screenPos;

        this.movedHandler = new MovedHandler();
    }

    // impl

    @Override
    public void listen(Scene scene) {
        scene.setOnMouseMoved(movedHandler);
        scene.setOnMouseDragged(movedHandler);
    }

    // inner

    private class MovedHandler implements EventHandler<MouseEvent> {
        // impl
        @Override
        public void handle(MouseEvent event) {
            pos      .set(event.getX(),       event.getY()      );
            scenePos .set(event.getSceneX(),  event.getSceneY() );
            screenPos.set(event.getScreenX(), event.getScreenY());
        }
    }
}
