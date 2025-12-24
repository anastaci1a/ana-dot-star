package ana.fx.app;


// dep

import ana.fx.canvas.Canvas;
import ana.fx.canvas.draw.Draw;
import ana.util.math.vector.point.IntPoint;


// main

public abstract class App {
    // attr

    public final Canvas canvas;
    public final IntPoint size;
    public final Draw draw;

    // constr

    public App(Init init) {
        this.canvas = init.canvas;
        this.size = canvas.size;
        this.draw = canvas.draw;
    }

    // main

    public void eventLoop() {}

    // prot utils

    protected int frameCount() {
        return canvas.getFrameCount();
    }

    // constr info (for Launcher)

    public static class Init {
        public final Canvas canvas;

        public Init(Canvas canvas) {
            this.canvas = canvas;
        }
    }
}
