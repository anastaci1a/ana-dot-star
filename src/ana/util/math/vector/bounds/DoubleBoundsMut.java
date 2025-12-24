package ana.util.math.vector.bounds;


// dep

import ana.util.math.vector.point.DoublePoint;
import ana.util.math.vector.DoubleVector;


// main

public class DoubleBoundsMut extends DoubleBounds {
    // attr

    protected DoubleVector tl, br, tr, bl, size, center;
    private boolean isNone = false;

    // prot constr

    protected DoubleBoundsMut(DoublePoint tl, DoublePoint br) {
        super(tl, br);
    }

    // set

    public DoubleBoundsMut setNone() {
        isNone = true;
        return setCorner(
            new DoubleVector(0, 0),
            new DoubleVector(0, 0)
        );
    }

    public DoubleBoundsMut setSizeCornerScale(DoublePoint size) {
        return setCorner(tl, size);
    }

    public DoubleBoundsMut setSizeCenterScale(DoublePoint size) {
        return setCenter(tl, size);
    }

    public DoubleBoundsMut setCorner(DoublePoint tl) {
        return setCorner(tl, size);
    }

    public DoubleBoundsMut setCorner(DoublePoint tl, DoublePoint size) {
        return set(DoubleBounds.fromCorner(tl, size));
    }

    public DoubleBoundsMut setCorners(DoublePoint tl, DoublePoint br) {
        return set(DoubleBounds.fromCorners(tl, br));
    }

    public DoubleBoundsMut setCenter(DoublePoint center) {
        return setCenter(center, size);
    }

    public DoubleBoundsMut setCenter(DoublePoint center, DoublePoint size) {
        return set(DoubleBounds.fromCenter(center, size));
    }

    // prot util

    protected DoubleBoundsMut set(DoubleBounds b) { // unchecked, no clone
        this.tl = b.tl;
        this.tr = b.br;
        this.bl = b.bl;
        this.br = b.br;
        this.size = b.size;
        this.center = b.center;

        isNone = false;
        return this;
    }
}
