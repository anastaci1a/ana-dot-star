/**
 * Provides core utilities to extend {@code javafx} functionality, general io/cli, and that of other related tools.
 *
 * <p>The packages contained within {@code ana.*} ("ana dot star") aim to abstract away repeatable operations for:
 * graphical applications which use {@code javafx}, frame-by-frame user input, cli menus, color operations, and others.</p>
 *
 * <h2>Package Map</h2>
 * <ul>
 *   <li>{@link ana.fx} – Utilities and extended functionality for {@code javafx}</li>
 *   <li>{@link ana.io} – Utilities for modular cli, user input devices, and file operations</li>
 *   <li>{@link ana.lang} – Core exceptions for all local packages</li>
 *   <li>{@link ana.util} – Utilities for colors, math/vectors, and text formatting</li>
 * </ul>
 *
 * @since 1.0
 * @author anastaci1a
 */
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
    exports ana.util.math.vector.bounds;
    exports ana.util.math.vector.point;
    exports ana.util.math.vector;
    exports ana.util.style;
}