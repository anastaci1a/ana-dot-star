package ana.fx.app;


// dep

import ana.fx.canvas.Canvas;
import ana.io.device.DeviceInput;
import ana.io.device.InputButtonStatus;
import ana.io.device.keyboard.Key;
import ana.io.device.mouse.Mouse;
import ana.util.math.point.DoublePoint;


// main

public class AppX extends App {
    // attr

    public final DeviceInput input;

    // constr

    public AppX(Init info) {
        super(info);

        this.input = info.input;
    }

    // prot utils

    protected InputButtonStatus key(Key key) {
        return input.get(key);
    }

    protected InputButtonStatus mouse(Mouse mouseButton) {
        return input.get(mouseButton);
    }

    protected DoublePoint mouse() {
        return input.getMouse();
    }

    // constr info (for LauncherX)

    public static class Init extends App.Init {
        public final DeviceInput input;

        public Init(Canvas canvas, DeviceInput input) {
            super(canvas);
            this.input = input;
        }
    }
}
