package ana.util.math.vector.bounds;


// dep

import ana.util.math.vector.point.IntPoint;
import ana.util.math.vector.IntVector;


// main

public class IntBounds {
    // attr

    protected final IntVector tl, br, tr, bl, size, center;

    // prot constr

    protected IntBounds(IntPoint tl, IntPoint br, IntPoint tr, IntPoint bl, IntPoint size, IntPoint center) {
        this.tl = tl.clone();
        this.br = br.clone();
        this.tr = tr.clone();
        this.bl = bl.clone();
        this.size = size.clone();
        this.center = center.clone();
    }

    protected IntBounds(IntPoint tl, IntPoint br) {
        this(
            tl, br,
            new IntVector(br.x(), tl.y()),
            new IntVector(tl.x(), br.y()),
            IntVector.sub(br, tl),
            IntVector.add(tl, br).div(2)
        );
    }

    // get

    public IntPoint tl() {
        return tl.clone();
    }

    public IntPoint tr() {
        return tr.clone();
    }

    public IntPoint bl() {
        return bl.clone();
    }

    public IntPoint br() {
        return br.clone();
    }

    public IntPoint size() {
        return size.clone();
    }

    public IntPoint center() {
        return center.clone();
    }

    // to

    public DoubleBounds toDoubleBounds() {
        return DoubleBounds.fromCorners(
            tl.toDoubleVector(),
            br.toDoubleVector()
        );
    }

    // static factory

    public static IntBounds fromCenter(IntPoint center, IntPoint size) {
        IntPoint halfSize = size.clone().div(2);
        return fromCorners(
            center.clone().sub(halfSize),
            center.clone().add(halfSize)
        );
    }

    public static IntBounds fromCorner(IntPoint tl, IntPoint size) {
        return fromCorners(
            tl,
            tl.clone().add(size)
        );
    }

    public static IntBounds fromCorners(IntPoint c1, IntPoint c2) {
        IntPoint tl = new IntVector(
            Math.min(c1.x(), c2.x()),
            Math.min(c1.y(), c2.y())
        );

        IntPoint br = new IntVector(
            Math.max(c1.x(), c2.x()),
            Math.max(c1.y(), c2.y())
        );

        return new IntBounds(tl, br);
    }
}
