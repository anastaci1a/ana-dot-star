package ana.fx.window;


// dep

import ana.util.color.Color;
import ana.util.math.point.DoublePoint;
import ana.util.math.point.IntPoint;
import ana.util.math.vector.DoubleVector;
import ana.util.math.vector.IntVector;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;


// main

public final class WindowConfig {
    // const

    public static final WindowConfig WINDOW_CFG_DEFAULT = new WindowConfig(
        "Untitled App",
        16.0 / 9.0,
        0.5,
        new DoubleVector(0, -0.1), // slightly upwards
        Color.WHITE
    );

    // attr

    public final String title;

    public final double sizeRatio;
    public final IntPoint size, pos;

    // constr

    public WindowConfig(
        String title,
        double windowRatio /* W:H */,
        double longerSidePercentage,
        DoublePoint posAdjust,
        Color bgColor
    ) {

        // title, color

        this.title = title;

        // size, pos

        DoublePoint screenSize = getScreenSize();
        DoublePoint sizeDouble = calcSize(screenSize, windowRatio, longerSidePercentage);

        this.size = new IntVector(
            (int) Math.round(sizeDouble.x()),
            (int) Math.round(sizeDouble.y())
        );
        this.sizeRatio = (double) size.x() / size.y();

        this.pos = calcPos(screenSize, sizeDouble, posAdjust);
    }

    // priv static util

    private static DoubleVector getScreenSize() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        return new DoubleVector(screenBounds.getWidth(), screenBounds.getHeight());
    }

    private static DoubleVector calcSize(DoublePoint screenSize, double windowRatio, double longerSidePercentage) {
        double screenRatio = screenSize.x() / screenSize.x();

        double width, height;
        if (screenRatio < windowRatio) {
            // width as limiter
            width = screenSize.x() * longerSidePercentage;
            height = width / windowRatio;
        } else {
            // height as limiter
            height = screenSize.y() * longerSidePercentage;
            width = height * windowRatio;
        }

        return new DoubleVector(width, height);
    }

    private static IntVector calcPos(DoublePoint screenSize, DoublePoint size, DoublePoint posAdjust) {
        DoubleVector center  = DoubleVector.div(screenSize, 2);
        DoubleVector topLeft = center.clone().sub(DoubleVector.div(size, 2));

        DoubleVector maxDelta  = DoubleVector.sub(screenSize, size).div(2);
        DoubleVector deltaMult = DoubleVector.constrain2D(posAdjust, -1, -1, 1, 1);
        DoubleVector delta = maxDelta.mult(deltaMult);

        DoubleVector posDouble = DoubleVector.add(topLeft, delta);

        return new IntVector(
            (int) Math.round(posDouble.x()),
            (int) Math.round(posDouble.y())
        );
    }
}
