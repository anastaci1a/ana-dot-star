module ana {
    // req

    requires javafx.graphics;

    // exp

    exports ana.fx.app;
    exports ana.fx.canvas;
    exports ana.fx.canvas.draw;
    exports ana.fx.app.launcher;
    exports ana.fx.window;

    exports ana.io.cli;
    exports ana.io.device;
    exports ana.io.device.impl;
    exports ana.io.device.keyboard;
    exports ana.io.device.mouse;

    exports ana.lang;
    exports ana.lang.fx;

    exports ana.util.color;
    exports ana.util.math;
    exports ana.util.math.bounds;
    exports ana.util.math.point;
    exports ana.util.math.vector;
    exports ana.util.style;
}