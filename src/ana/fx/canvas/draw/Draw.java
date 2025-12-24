package ana.fx.canvas.draw;


// dep

import ana.lang.ConfigReassignmentException;
import ana.lang.MatrixStackEmptyException;
import ana.util.color.Color;
import ana.util.math.Axis;
import ana.util.math.MathExt;
import ana.util.math.vector.bounds.DoubleBounds;
import ana.util.math.vector.point.DoublePoint;
import ana.util.math.vector.point.IntPoint;
import ana.util.math.vector.DoubleVector;
import ana.util.math.vector.IntVector;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;


// main

public class Draw {
    // const

    public final Color COL_FILL_DEFAULT   = Color.GRAY;
    public final Color COL_STROKE_DEFAULT = Color.ONYX;

    public final double STROKE_WEIGHT_DEFAULT = 20;

    public final DrawMode.Rect MODE_RECT_DEFAULT = DrawMode.Rect.CORNERS;

    // attr

    protected IntPoint size;
    protected int[] intArray;

    protected IntVector modifMin, modifMax, modifSize;
    protected boolean shouldRedraw;

    private boolean preMultColAlpha;

    protected final Stack<Function<DoubleVector, DoubleVector>> matrices;

    protected Color fill, stroke;
    protected int fillInt, strokeInt;
    protected double strokeWeight;
    protected boolean yesFill, yesStroke;

    protected DrawMode.Rect rectMode;

    // constr

    public Draw() {
        intArray  = null;
        size      = null;
        modifMin  = null;
        modifMax  = null;
        modifSize = null;

        preMultColAlpha = false;

        shouldRedraw = false;

        matrices = new Stack<>();

        setFill(COL_FILL_DEFAULT);
        setStroke(COL_STROKE_DEFAULT);
        strokeWeight = STROKE_WEIGHT_DEFAULT;
        rectMode = MODE_RECT_DEFAULT;
    }

    // setup

    public Supplier<WriteState> setGetWriteState(int[] intArray, IntPoint size, boolean preMultColAlpha) throws ConfigReassignmentException {
        if (this.intArray != null) throw new ConfigReassignmentException();

        this.intArray = intArray;
        this.size = size;

        this.preMultColAlpha = preMultColAlpha;

        modifMin  = new IntVector(0, 0);
        modifMax  = new IntVector(0, 0);
        modifSize = new IntVector(0, 0);

        shouldRedraw = false;

        return this::getResetWriteState;
    }

    // buffer

    protected boolean shouldRedraw() {
        return shouldRedraw;
    }

    protected WriteState getResetWriteState() {
        modifSize.set(
            // adding one for inclusivity
            // ("pixels from 2 to 3: (3 - 2) + 1 = 2")
            IntVector.sub(modifMax, modifMin).add(1, 1)
        );

        WriteState state = new WriteState(
            modifMin.clone(), modifMax.clone(),
            modifSize.clone(),
            shouldRedraw
        );

        shouldRedraw = false;

        // these values are never used when noModif is false
        modifMin.set(0, 0);
        modifMax.set(0, 0);
        modifSize.set(0, 0);

        resetMatrix();

        return state;
    }

    // settings

    public void setFill(Color fill) {
        this.fill    = fill;
        this.fillInt = colorToInt(fill);
        this.yesFill = true;
    }

    public Color getFill() {
        return fill;
    }

    public void noFill() {
        this.yesFill = false;
    }

    public void setStroke(Color stroke) {
        this.stroke    = stroke;
        this.strokeInt = colorToInt(stroke);
        this.yesStroke = true;
    }

    public Color getStroke() {
        return stroke;
    }

    public void noStroke() {
        this.yesStroke = false;
    }

    public void setStrokeWeight(double strokeWeight) {
        this.strokeWeight = (int) Math.round(strokeWeight);
    }

    public double getStrokeWeight() {
        return strokeWeight;
    }

    public void setMode(DrawMode.Rect rectMode) {
        this.rectMode = rectMode;
    }

    // matrix

    public void pushMatrix() {
        matrices.push(newMatrixOp());
    }

    public void popMatrix() {
        if (matrices.empty()) throw new MatrixStackEmptyException();
        else matrices.pop();
    }

    public void resetMatrix() {
        while (!matrices.isEmpty()) matrices.pop();
        matrices.push(newMatrixOp());
    }

    public void translate(DoublePoint delta) {
        updateMatrix(p -> p.add(delta));
    }

    public void translate(double x, double y) {
        translate(
            new DoubleVector(x, y)
        );
    }

    public void scale(double x, double y) {
        updateMatrix(p -> p.mult(x, y));
    }

    public void scale(DoublePoint amt) {
        updateMatrix(p -> p.mult(amt));
    }

    public void rotate(Axis aor, double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        updateMatrix(p -> p.rotate(aor, cosTheta, sinTheta));
    }

    // plot

    public void plot(double x, double y) {
        plot(new DoubleVector(x, y), fillInt);
    }

    public void plot(DoublePoint p) {
        basePixel(applyMatrix(p), fillInt);
    }

    protected void plot(DoublePoint p, int argb) {
        IntPoint ip = applyMatrix(p);
        basePixel(ip.x(), ip.y(), argb);
    }

    // line

    public void line(double x1, double y1, double x2, double y2) {
        line(
            new DoubleVector(x1, y1),
            new DoubleVector(x2, y2)
        );
    }

    public void line(DoublePoint p1, DoublePoint p2) {
        line(p1, p2, strokeWeight, strokeInt);
    }

    protected void line(DoublePoint p1, DoublePoint p2, double weight, int argb) {

        IntPoint ip1 = applyMatrix(p1);
        IntPoint ip2 = applyMatrix(p2);

        // NEVERMIND IT'S WEIRD
        // // easy solution, should use a polygon (not implemented)
        // IntPoint weightVec = applyMatrix(new DoubleVector(MathExt.SQRT2, 0));
        // int weightAdj = (int) Math.round(weightVec.mag());

        baseWeightedLine(ip1, ip2, (int) Math.round(strokeWeight), argb);
    }

    // rect

    public void rect(double x1, double y1, double x2, double y2) {
        rect(
            new DoubleVector(x1, y1),
            new DoubleVector(x2, y2)
        );
    }

    public void rect(DoublePoint p1, DoublePoint p2) {
        if (yesFill || yesStroke) {
            DoubleBounds b = getRectBounds(p1, p2);
            if (yesFill) rect(b, fillInt);
            if (yesStroke) rectOutline(b, strokeWeight, strokeInt);
        }
    }

    public void background(Color c) {
        quad(
            new DoubleVector(0, 0),
            new DoubleVector(size.x(), 0),
            new DoubleVector(0, size.y()),
            size.toDoubleVector(),
            colorToInt(c)
        );
    }

    protected void rect(DoubleBounds b, int argb) {
        quad(b.tl(), b.tr(), b.bl(), b.br(), argb);
    }

    protected void quad(DoublePoint tl, DoublePoint tr, DoublePoint bl, DoublePoint br, int argb) {
        IntPoint ip1 = applyMatrix(tl);
        IntPoint ip2 = applyMatrix(tr);
        IntPoint ip3 = applyMatrix(bl);
        IntPoint ip4 = applyMatrix(br);

        baseQuadConvex(ip1, ip2, ip3, ip4, argb);
    }

    protected void rectOutline(DoubleBounds b, double weight, int argbStroke) {
        line(b.tl(), b.bl(), weight, argbStroke);
        line(b.tl(), b.tr(), weight, argbStroke);
        line(b.bl(), b.br(), weight, argbStroke);
        line(b.tr(), b.br(), weight, argbStroke);
    }

    protected DoubleBounds getRectBounds(DoublePoint p1, DoublePoint p2) {
        DoubleBounds b;
        switch (rectMode) {
            case CENTER: {
                b = DoubleBounds.fromCenter(p1, p2);
                break;
            }
            case CORNERS: {
                b = DoubleBounds.fromCorners(p1, p2);
                break;
            }
            default: { // CORNERS
                b = DoubleBounds.fromCorner(p1, p2);
            }
        }

        return b;
    }


    // --


    // sys pixel

    private int idx(int x, int y) {
        return (size.x() * y) + x;
    }

    private void basePixel(IntPoint p, int argb) {
        basePixel(p.x(), p.y(), argb);
    }

    private void basePixel(int x, int y, int argb) {
        boolean inBounds = MathExt.inBounds(
            x, y, 0, 0,
            size.x() - 1, size.y() - 1
        );
        if (inBounds) {
            updateModifBounds(x, y);

            int idx = idx(x, y);
            intArray[idx] = argb;
        }
    }

    // sys line
    // (https://stackoverflow.com/questions/49047229/draw-a-triangle-to-pixel-array)

    private void baseLine(IntPoint p1, IntPoint p2, int argb) {
        baseLine(p1.x(), p1.y(), p2.x(), p2.y(), argb);
    }

    private void baseLine(int x1, int y1, int x2, int y2, int argb) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int err = dx - dy;

        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        for (int i = 0; x1 != x2 || y1 != y2; i++) {
            basePixel(x1, y1, argb); // endpoint

            double e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
        basePixel(x2, y2, argb); // endpoint
    }

    // sys triangle
    // (https://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html)

    private void baseTriangle(IntPoint p1, IntPoint p2, IntPoint p3, int argb) {
        IntPoint[] p = new IntPoint[] { p1, p2, p3 };
        Arrays.sort(p, Comparator.comparingInt(IntPoint::y));
        p1 = p[0]; p2 = p[1]; p3 = p[2];

        // bottom-flat triangle
        if (p2.y() == p3.y()) baseTriangleBottom(p1, p2, p3, argb);

        // top-flat triangle
        else if (p1.y() == p2.y()) baseTriangleTop(p1, p2, p3, argb);

        // dual triangle (split top-flat + bottom-flat)
        else {
            IntPoint p4 = new IntVector(
                // intercept theorem (dx4/dx3 = dy4/dy3)
                (int) (p1.x() + ((float) (p2.y() - p1.y()) / (float) (p3.y() - p1.y())) * (p3.x() - p1.x())),
                p2.y()
            );

            baseTriangleBottom(p[0], p[1], p4, argb);
            baseTriangleTop(p[1], p4, p[2], argb);
        }
    }

    private void baseTriangleBottom(IntPoint p1, IntPoint p2, IntPoint p3, int argb) {
        double slope1 = (double) (p2.x() - p1.x()) / (p2.y() - p1.y());
        double slope2 = (double) (p3.x() - p1.x()) / (p3.y() - p1.y());

        double curx1 = p1.x();
        double curx2 = p1.x();

        for (int scanlineY = p1.y(); scanlineY <= p2.y(); scanlineY++) {
            baseLine((int) curx1, scanlineY, (int) curx2, scanlineY, argb);

            curx1 += slope1;
            curx2 += slope2;
        }
    }

    private void baseTriangleTop(IntPoint p1, IntPoint p2, IntPoint p3, int argb) {
        double slope1 = (double) (p3.x() - p1.x()) / (p3.y() - p1.y());
        double slope2 = (double) (p3.x() - p2.x()) / (p3.y() - p2.y());

        double curx1 = p3.x();
        double curx2 = p3.x();

        for (int scanlineY = p3.y(); scanlineY >= p1.y(); scanlineY--) {
            baseLine((int) curx1, scanlineY, (int) curx2, scanlineY, argb);

            curx1 -= slope1;
            curx2 -= slope2;
        }
    }

    // sys quad

    private void baseQuadConvex(IntPoint p1, IntPoint p2, IntPoint p3, IntPoint p4, int argb) {
        IntPoint[] pts = { p1, p2, p3, p4 };

        Arrays.sort(pts, Comparator.comparingInt(IntPoint::y));
        Arrays.sort(pts, Comparator.comparingInt(IntPoint::x));

        IntPoint tl = pts[0], bl = pts[1], tr = pts[2], br = pts[3];

        baseTriangle(tl, tr, br, argb);
        baseTriangle(tl, bl, br, argb);
    }

    // sys weighted line

    private void baseWeightedLine(IntPoint p1, IntPoint p2, int weight, int argb) {
        if (weight <= 1) {
            baseLine(p1, p2, argb);
            return;
        }

        IntVector heading = IntVector.sub(p2, p1);
        double len = IntVector.sub(p2, p1).toDoubleVector().mag();
        if (len == 0) {
            // basePixel(p1.x(), p1.y(), argb); // a single pixel if line length is 0 (not necessary)
            return;
        }

        double od = weight / (2.0 * len);
        IntVector off = new DoubleVector(
            -heading.y(),
            heading.x()
        ).mult(od).toIntVector();

        IntPoint tl = p1.clone().add(off);
        IntPoint tr = p2.clone().add(off);
        IntPoint bl = p1.clone().sub(off);
        IntPoint br = p2.clone().sub(off);

        // baseTriangle(tl, tr, bl, argb);
        // baseTriangle(bl, br, tr, argb);

        // to fix the line thing, I don't want to implement it the efficient way
        // (nevermind it's too slow, and I wanna do something else eventually)
        // baseTriangle(tl, tr, br, argb);
        // baseTriangle(bl, br, tl, argb);

        // quadcoded frfr

        baseQuadConvex(tl, tr, bl, br, argb);
    }

    private void baseRect(IntPoint tl, IntPoint br, int argb) {
        IntPoint size = IntVector.sub(br, tl);

        IntPoint tr = tl.clone().add(size.x() / 2, 0);
        IntPoint bl = tl.clone().add(0, size.y() / 2);

        baseQuadConvex(tl, tr, bl, br, argb);
    }


    // --


    // inner

    public record WriteState(
        IntPoint min, IntPoint max, IntPoint size,
        boolean shouldRedraw
    ) {}

    private void updateModifBounds(int x, int y) {
        // init both
        if (!shouldRedraw) {
            modifMin.set(x, y);
            modifMax.set(x, y);
            shouldRedraw = true;

            return;
        }

        int minX = modifMin.x(), minY = modifMin.y();
        int maxX = modifMax.x(), maxY = modifMax.y();

        if (x < minX) minX = x;
        if (y < minY) minY = y;
        if (x > maxX) maxX = x;
        if (y > maxY) maxY = y;

        modifMin.set(minX, minY);
        modifMax.set(maxX, maxY);
    }


    // --


    // sys matrix

    protected static Function<DoubleVector, DoubleVector> newMatrixOp() {
        return d -> d;
    }

    protected void updateMatrix(Function<DoubleVector, DoubleVector> matrixOp) {
        var current = matrices.pop();
        matrices.push(p -> {
            return current.apply(matrixOp.apply(p)); // compose the functions
        });
    }

    protected IntPoint applyMatrix(DoublePoint p) {
        if (matrices.isEmpty()) return p.toIntVector();

        DoubleVector pModif = p.clone();
        for (int i = matrices.size() - 1; i >= 0; i--) {
            Function<DoubleVector, DoubleVector> m = matrices.get(i);
            m.apply(pModif);
        }
        return pModif.toIntVector();
    }

    // util

    protected int colorToInt(Color col) {
        if (preMultColAlpha) return col.toArgbIntPre();
        return col.toArgbInt();
    }
}
