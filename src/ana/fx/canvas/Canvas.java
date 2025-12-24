package ana.fx.canvas;


// dep

import ana.fx.window.WindowConfig;
import ana.fx.canvas.draw.Draw;
import ana.lang.ConfigReassignmentException;
import ana.lang.fx.InvalidDrawInstanceException;
import ana.util.math.vector.IntVector;
import ana.util.math.point.IntPoint;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelBuffer;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

import java.nio.IntBuffer;
import java.util.function.Supplier;


// main

public class Canvas extends Pane {
    // pub attr

    public final WindowConfig cfg;
    public final IntPoint size;
    public final Draw draw;

    // priv attr

    private final ImageView imageView;
    private final WritableImage img;

    private final IntBuffer intBuffer;
    private final PixelBuffer<IntBuffer> pixelBuffer;
    private final Supplier<Draw.WriteState> getResetWriteState;

    private int frameCount;

    // constr

    public Canvas(WindowConfig cfg, Draw draw) {
        this.cfg = cfg;
        this.draw = draw;

        // pixel stuffs
        intBuffer = IntBuffer.allocate(cfg.size.x() * cfg.size.y());
        PixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbPreInstance();
        pixelBuffer = new PixelBuffer<>(cfg.size.x(), cfg.size.y(), intBuffer, pixelFormat);
        size = new IntVector(pixelBuffer.getWidth(), pixelBuffer.getHeight());

        // comm with Draw inst (which can be a subclass!)
        try {
            getResetWriteState = draw.setGetWriteState(intBuffer.array(), size, true);
        } catch(ConfigReassignmentException e) {
            // provided Draw instance must only be configured by Canvas
            throw new InvalidDrawInstanceException();
        }
        draw.resetMatrix();

        // javafx stuffs
        img = new WritableImage(pixelBuffer);
        imageView = new ImageView(img);
        getChildren().add(imageView);

        frameCount = 1;
    }

    // buffer

    public void drawFrame() {
        frameCount++;

        Draw.WriteState writeState = getResetWriteState.get();

        if (writeState.shouldRedraw()) {
            IntPoint modifMin = writeState.min();
            IntPoint modifSize = writeState.size();

            int minX  = modifMin.x(),  minY  = modifMin.y(),
                sizeX = modifSize.x(), sizeY = modifSize.y();

            pixelBuffer.updateBuffer(pb ->
                new Rectangle2D(minX, minY, sizeX, sizeY)
            );
            intBuffer.clear();
        }
    }

    // get

    public int getFrameCount() {
        return frameCount;
    }
}










