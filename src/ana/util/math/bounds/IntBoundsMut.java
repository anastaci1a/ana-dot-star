package ana.util.math.bounds;


// dep

import ana.util.math.point.IntPoint;
import ana.util.math.vector.IntVector;


// main

public class IntBoundsMut extends IntBounds {
    // attr

    protected IntVector tl, br, tr, bl, size, center;
    private boolean isNone = false;

    // prot constr

    protected IntBoundsMut(IntPoint tl, IntPoint br) {
        super(tl, br);
    }

    // get

    public boolean isNone() {
        return isNone;
    }

    // set

    public IntBoundsMut setNone() {
        isNone = true;
        return setCorner(
            new IntVector(0, 0),
            new IntVector(0, 0)
        );
    }

    public IntBoundsMut setSizeCornerScale(IntPoint size) {
        return setCorner(tl, size);
    }

    public IntBoundsMut setSizeCenterScale(IntPoint size) {
        return setCenter(tl, size);
    }

    public IntBoundsMut setCorner(IntPoint tl) {
        return setCorner(tl, size);
    }

    public IntBoundsMut setCorner(IntPoint tl, IntPoint size) {
        return set(fromCorner(tl, size));
    }

    public IntBoundsMut setCorners(IntPoint tl, IntPoint br) {
        return set(fromCorners(tl, br));
    }

    public IntBoundsMut setCenter(IntPoint center) {
        return setCenter(center, size);
    }

    public IntBoundsMut setCenter(IntPoint center, IntPoint size) {
        return set(fromCenter(center, size));
    }

    // prot util

    protected IntBoundsMut set(IntBounds b) { // unchecked, no clone
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
