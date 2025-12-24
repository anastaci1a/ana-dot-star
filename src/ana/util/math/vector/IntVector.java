package ana.util.math.vector;


// dep

import ana.util.math.Axis;
import ana.util.math.Radians;
import ana.util.math.point.IntPoint;

import java.util.function.Function;


// main

public class IntVector implements IntPoint {
    // attr

    protected int x, y, z;

    // constr, clone

    public IntVector() {
        this(0, 0, 0);
    }

    public IntVector(int x, int y) {
        this(x, y, 0);
    }

    public IntVector(int x, int y, int z) {
        set(x, y, z);
    }

    public IntVector(IntPoint v) {
        set(v.x(), v.y(), v.z());
    }

    // conversions

    @Override
    public IntVector clone() {
        return new IntVector(x, y, z);
    }

    @Override
    public DoubleVector toDoubleVector() {
        return new DoubleVector(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", x, y, z);
    }

    // get

    @Override public int x() { return x; }
    @Override public int y() { return y; }
    @Override public int z() { return z; }

    // equals

    @Override
    public boolean equals(IntPoint v) {
        return equals(v.x(), v.y(), v.z());
    }

    @Override
    public boolean equals(int x, int y) {
        return equals(x, y, 0);
    }

    @Override
    public boolean equals(int x, int y, int z) {
        return (this.x == x) && (this.y == y) && (this.z == z);
    }

    public static boolean equals(IntPoint v1, IntPoint v2) {
        return v1.equals(v2);
    }

    // set, add, sub

    public IntVector set(int x, int y) {
        return set(x, y, 0);
    }

    public IntVector set(IntPoint v) {
        return set(
            v.x(),
            v.y(),
            v.z()
        );
    }

    public IntVector set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public IntVector add(int x, int y, int z) {
        return set(
            this.x + x,
            this.y + y,
            this.z + z
        );
    }

    public IntVector add(IntPoint v) {
        return add(v.x(), v.y(), v.z());
    }

    public IntVector add(int x, int y) {
        return add(x, y, 0);
    }

    public IntVector sub(int x, int y, int z) {
        return add(-x, -y, -z);
    }

    public static IntVector add(IntPoint v1, IntPoint... vecs) {
        IntVector result = new IntVector(v1);
        for (IntPoint v : vecs) result.add(v);
        return result;
    }

    public IntVector sub(int x, int y) {
        return sub(x, y, 0);
    }

    public IntVector sub(IntPoint c) {
        return sub(c.x(), c.y(), c.z());
    }

    public static IntVector sub(IntPoint v1, IntPoint v2) {
        return new IntVector(v1).sub(v2);
    }

    // mult, div

    public IntVector mult(int n) {
        return mult(n, n, n);
    }

    public IntVector mult(int x, int y, int z) {
        return set(
            this.x * x,
            this.y * y,
            this.z * z
        );
    }

    public IntVector mult(int x, int y) {
        return mult(x, y, 1);
    }

    public IntVector mult(IntPoint v) {
        return mult(v.x(), v.y(), v.z());
    }

    public static IntVector mult(IntPoint v, int n) {
        return v.clone().mult(n);
    }

    public static IntVector mult(IntPoint v1, IntPoint v2, IntPoint... vecs) {
        IntVector result = v1.clone().mult(v2);
        for (IntPoint v : vecs) result.mult(v.x(), v.y(), v.z());
        return result;
    }

    public IntVector div(int n) {
        return div(n, n, n);
    }

    public IntVector div(IntPoint v) {
        return div(v.x(), v.y(), v.z());
    }

    public IntVector div(int x, int y, int z) {
        return set(
            x == 0 ? Integer.MAX_VALUE : (this.x / x),
            y == 0 ? Integer.MAX_VALUE : (this.y / y),
            z == 0 ? Integer.MAX_VALUE : (this.z / z)
        );
    }

    public static IntVector div(IntPoint v1, int n) {
        return new IntVector(v1).div(n);
    }

    public static IntVector div(IntPoint v1, IntPoint v2) {
        return new IntVector(v1).div(v2);
    }

    // mag, magSq, dot, cross

    @Override
    public double mag() {
        return Math.hypot(x, y);
    }

    @Override
    public int magSq() {
        return dot(x, y, z);
    }

    @Override
    public int dot(IntPoint p) {
        return p.dot(p.x(), p.y(), p.z());
    }

    @Override
    public int dot(int x, int y) {
        return dot(x, y, 0);
    }

    @Override
    public int dot(int x, int y, int z) {
        return dot(this.x, this.y, this.z, x, y, z);
    }

    public static int dot(IntPoint p1, IntPoint p2) {
        return p1.dot(p2);
    }

    public static int dot(int x1, int y1, int x2, int y2) {
        return dot(x1, y1, 0, x2, y2, 0);
    }

    public static int dot(int x1, int y1, int z1, int x2, int y2, int z2) {
        return (x1 * x2) + (y1 * y2) + (z1 * z2);
    }

    @Override
    public IntVector cross(IntPoint v) {
        return cross(this, v);
    }

    public static IntVector cross(IntPoint v1, IntPoint v2) {
        return new IntVector(
            (v1.y() * v2.z()) - (v1.z() * v2.y()),
            (v1.z() * v2.x()) - (v1.x() * v2.z()),
            (v1.x() * v2.y()) - (v1.y() * v2.x())
        );
    }

    // rotate

    public IntVector rotate(Radians theta) {
        return rotate(Axis.Z, theta);
    }

    public IntVector rotate(Axis aor, Radians theta) {
        Function<Integer, Integer> mapA, mapB;

        switch(aor) {
            case X:
                mapA = (y) -> this.y = y;
                mapB = (z) -> this.z = z;
                break;

            case Y:
                mapA = (x) -> this.x = x;
                mapB = (z) -> this.z = z;
                break;

            case Z:
                mapA = (x) -> this.x = x;
                mapB = (y) -> this.y = y;
                break;

            default:
                throw new RuntimeException("Invalid axis of rotation.");
        }

        return rotate(mapA, mapB, theta);
    }

    private IntVector rotate(
        Function<Integer, Integer> mapA,
        Function<Integer, Integer> mapB,
        Radians theta
    ) {
        int a = mapA.apply(0);
        int b = mapB.apply(0);

        switch(theta) {
            // (a, b) -> (b, -a)
            case HALF_PI:
                mapA.apply(b);
                mapB.apply(-a);

                break;

            // (a, b) -> (-a, -b)
            case PI:
                mapA.apply(-a);
                mapB.apply(-b);

                break;

            // (a, b) -> (-b, a)
            case NEG_HALF_PI:
                mapA.apply(-b);
                mapB.apply(a);

                break;

            default:
                throw new RuntimeException("Invalid theta value.");
        }

        return this;
    }

    public static IntVector rotate(IntPoint v, Axis aor, Radians theta) {
        return new IntVector(v).rotate(aor, theta);
    }

    // constrain

    public IntVector constrain2D(
        IntPoint max
    ) {
        return constrain2D(new IntVector(), max);
    }

    public IntVector constrain2D(
        IntPoint min,
        IntPoint max
    ) {
        return constrain2D(min.x(), min.y(), max.x(), max.y());
    }

    public IntVector constrain3D(
        IntPoint max
    ) {
        return constrain3D(new IntVector(), max);
    }

    public IntVector constrain3D(
        IntPoint min,
        IntPoint max
    ) {
        return constrain3D(min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
    }

    public IntVector constrain2D(int x1, int y1, int x2, int y2) {
        return constrain3D(x1, y1, z, x2, y2, z);
    }

    public IntVector constrain3D(int x1, int y1, int z1, int x2, int y2, int z2) {
        return set(
            Math.clamp(x, x1, x2),
            Math.clamp(y, y1, y2),
            Math.clamp(z, z1, z2)
        );
    }

    public static IntVector constrain2D(
        IntPoint v,
        IntPoint min,
        IntPoint max
    ) {
        return new IntVector(v).constrain2D(min, max);
    }

    public static IntVector constrain3D(
        IntPoint v,
        IntPoint min,
        IntPoint max
    ) {
        return new IntVector(v).constrain3D(min, max);
    }

    public static IntVector constrain2D(
        IntPoint v,
        int x1, int y1, int x2, int y2
    ) {
        return new IntVector(v).constrain2D(x1, y1, x2, y2);
    }

    public static IntVector constrain3D(
        IntPoint v,
        int x1, int y1, int z1, int x2, int y2, int z2
    ) {
        return new IntVector(v).constrain3D(x1, y1, z1, x2, y2, z2);
    }
}
