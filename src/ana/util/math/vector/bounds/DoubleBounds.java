package ana.util.math.vector.bounds;


// dep

import ana.util.math.vector.point.DoublePoint;
import ana.util.math.vector.DoubleVector;


// main

public class DoubleBounds {
    // attr

    protected final DoubleVector tl, br, tr, bl, size, center;

    // prot constr

    protected DoubleBounds(DoublePoint tl, DoublePoint br, DoublePoint tr, DoublePoint bl, DoublePoint size, DoublePoint center) {
        this.tl = tl.clone();
        this.br = br.clone();
        this.tr = tr.clone();
        this.bl = bl.clone();
        this.size = size.clone();
        this.center = center.clone();
    }

    protected DoubleBounds(DoublePoint tl, DoublePoint br) {
        this(
            tl, br,
            new DoubleVector(br.x(), tl.y()),
            new DoubleVector(tl.x(), br.y()),
            DoubleVector.sub(br, tl),
            DoubleVector.add(tl, br).div(2)
        );
    }

    // get

    public DoublePoint tl() {
        return tl.clone();
    }

    public DoublePoint tr() {
        return tr.clone();
    }

    public DoublePoint bl() {
        return bl.clone();
    }

    public DoublePoint br() {
        return br.clone();
    }

    public DoublePoint size() {
        return size.clone();
    }

    public DoublePoint center() {
        return center.clone();
    }

    // to

    public IntBounds toIntBounds() {
        return IntBounds.fromCorners(
            tl.toIntVector(),
            br.toIntVector()
        );
    }

    // static factory

    public static DoubleBounds fromCenter(DoublePoint center, DoublePoint size) {
        DoublePoint halfSize = size.clone().div(2);
        return fromCorners(
            center.clone().sub(halfSize),
            center.clone().add(halfSize)
        );
    }

    public static DoubleBounds fromCorner(DoublePoint tl, DoublePoint size) {
        return fromCorners(
            tl,
            tl.clone().add(size)
        );
    }

    public static DoubleBounds fromCorners(DoublePoint c1, DoublePoint c2) {
        DoublePoint tl = new DoubleVector(
            Math.min(c1.x(), c2.x()),
            Math.min(c1.y(), c2.y())
        );

        DoublePoint br = new DoubleVector(
            Math.max(c1.x(), c2.x()),
            Math.max(c1.y(), c2.y())
        );

        return new DoubleBounds(tl, br);
    }
}
