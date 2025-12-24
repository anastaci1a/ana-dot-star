package ana.util.math.vector;


// dep

import ana.util.math.Axis;
import ana.util.math.Radians;
import ana.util.math.vector.point.DoublePoint;

import java.util.function.Function;


// main

public class DoubleVector implements DoublePoint {
    // attr

    protected double x, y, z;

    // constr

    public DoubleVector() {
        this(0, 0, 0);
    }

    public DoubleVector(double x, double y) {
        this(x, y, 0);
    }

    public DoubleVector(double x, double y, double z) {
        set(x, y, z);
    }

    public DoubleVector(DoublePoint v) {
        set(v.x(), v.y(), v.z());
    }

    // conversions

    @Override
    public DoubleVector clone() {
        return new DoubleVector(x, y, z);
    }

    @Override
    public IntVector toIntVector() {
        return new IntVector(
            (int) Math.round(x),
            (int) Math.round(y),
            (int) Math.round(z)
        );
    }

    @Override
    public String toString() {
        return String.format("(%.2f, %.2f, %.2f)", x, y, z);
    }

    // get

    @Override public double x() { return x; }
    @Override public double y() { return y; }
    @Override public double z() { return z; }

    // equals

    @Override
    public boolean equals(DoublePoint v) {
        return equals(v.x(), v.y(), v.z());
    }

    @Override
    public boolean equals(double x, double y) {
        return equals(x, y, 0);
    }

    @Override
    public boolean equals(double x, double y, double z) {
        return (this.x == x) && (this.y == y) && (this.z == z);
    }

    public static boolean equals(DoublePoint v1, DoublePoint v2) {
        return v1.equals(v2);
    }

    // set, add, sub

    public DoubleVector set(double x, double y) {
        return set(x, y, z);
    }

    public DoubleVector set(DoublePoint v) {
        return set(
            v.x(),
            v.y(),
            v.z()
        );
    }

    public DoubleVector set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public DoubleVector add(double x, double y, double z) {
        return set(
            this.x + x,
            this.y + y,
            this.z + z
        );
    }

    public DoubleVector add(DoublePoint v) {
        return add(v.x(), v.y(), v.z());
    }

    public DoubleVector add(double x, double y) {
        return add(x, y, 0);
    }

    public DoubleVector sub(double x, double y, double z) {
        return add(-x, -y, -z);
    }

    public static DoubleVector add(DoublePoint v1, DoublePoint ... vecs) {
        DoubleVector result = new DoubleVector(v1);
        for (DoublePoint v : vecs) result.add(v);
        return result;
    }

    public DoubleVector sub(double x, double y) {
        return sub(x, y, 0);
    }

    public DoubleVector sub(DoublePoint c) {
        return sub(c.x(), c.y(), c.z());
    }

    public static DoubleVector sub(DoublePoint v1, DoublePoint v2) {
        return new DoubleVector(v1).sub(v2);
    }

    // mult, div

    public DoubleVector mult(double n) {
        return mult(n, n, n);
    }

    public DoubleVector mult(double x, double y, double z) {
        return set(
            this.x * x,
            this.y * y,
            this.z * z
        );
    }

    public DoubleVector mult(double x, double y) {
        return mult(x, y, 1);
    }

    public DoubleVector mult(DoublePoint v) {
        return mult(v.x(), v.y(), v.z());
    }

    public static DoubleVector mult(DoublePoint v, double n) {
        return v.clone().mult(n);
    }

    public static DoubleVector mult(DoublePoint v1, DoublePoint v2, DoublePoint ... vecs) {
        DoubleVector result = v1.clone().mult(v2);
        for (DoublePoint v : vecs) result.mult(v.x(), v.y(), v.z());
        return result;
    }

    public DoubleVector div(double n) {
        return div(n, n, n);
    }

    public DoubleVector div(DoublePoint v) {
        return div(v.x(), v.y(), v.z());
    }

    public DoubleVector div(double x, double y, double z) {
        return set(
            x == 0 ? Double.MAX_VALUE : (this.x / x),
            y == 0 ? Double.MAX_VALUE : (this.y / y),
            z == 0 ? Double.MAX_VALUE : (this.z / z)
        );
    }

    public static DoubleVector div(DoublePoint v1, double n) {
        return new DoubleVector(v1).div(n);
    }

    public static DoubleVector div(DoublePoint v1, DoublePoint v2) {
        return new DoubleVector(v1).div(v2);
    }

    // normalize, mag, magSq, dot, cross

    public DoubleVector norm() {
        double m = mag();
        return set(
            x / m,
            y / m,
            z / m
        );
    }

    public DoubleVector setMag(double m) {
        norm();
        return set(
            x * m,
            y * m,
            z * m
        );
    }

    @Override
    public double mag() {
        return Math.hypot(x, y);
    }

    @Override
    public double magSq() {
        return dot(x, y, z);
    }

    @Override
    public double dot(DoublePoint p) {
        return p.dot(p.x(), p.y(), p.z());
    }

    @Override
    public double dot(double x, double y) {
        return dot(x, y, 0);
    }

    @Override
    public double dot(double x, double y, double z) {
        return dot(this.x, this.y, this.z, x, y, z);
    }

    public static double dot(DoublePoint p1, DoublePoint p2) {
        return p1.dot(p2);
    }

    public static double dot(double x1, double y1, double x2, double y2) {
        return dot(x1, y1, 0, x2, y2, 0);
    }

    public static double dot(double x1, double y1, double z1, double x2, double y2, double z2) {
        return (x1 * x2) + (y1 * y2) + (z1 * z2);
    }

    @Override
    public DoubleVector cross(DoublePoint v) {
        return cross(this, v);
    }

    public static DoubleVector cross(DoublePoint v1, DoublePoint v2) {
        return new DoubleVector(
            (v1.y() * v2.z()) - (v1.z() * v2.y()),
            (v1.z() * v2.x()) - (v1.x() * v2.z()),
            (v1.x() * v2.y()) - (v1.y() * v2.x())
        );
    }

    // rotate

    public DoubleVector rotate(Axis aor, double theta) {
        return rotate(
            aor,
            Math.cos(theta),
            Math.sin(theta)
        );
    }

    public DoubleVector rotate(Axis aor, double cosTheta, double sinTheta) {
        switch(aor) {
            case X:
                set(
                    x,
                    (y * cosTheta) - (z * sinTheta),
                    (y * sinTheta) + (z * cosTheta)
                );
                break;

            case Y:
                set(
                    ( x * cosTheta) + (z * sinTheta),
                    y,
                    (-x * sinTheta) + (z * cosTheta)
                );
                break;

            case Z:
                set(
                    (x * cosTheta) - (y * sinTheta),
                    (x * sinTheta) + (y * cosTheta),
                    z
                );
                break;

            default:
                throw new RuntimeException("Invalid axis of rotation.");
        }
        return this;
    }

    public DoubleVector rotate(Radians th) {
        return rotate(Axis.Z, th);
    }

    public DoubleVector rotate(Axis aor, Radians theta) {
        Function<Double, Double> mapA, mapB;

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
                throw new Error("ERROR: Invalid axis of rotation.");
        }

        return rotate(mapA, mapB, theta);
    }

    private DoubleVector rotate(
        Function<Double, Double> mapA,
        Function<Double, Double> mapB,
        Radians theta
    ) {
        double a = mapA.apply(0.0);
        double b = mapB.apply(0.0);

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
                throw new Error("ERROR: Invalid theta value.");
        }

        return this;
    }

    public static DoubleVector rotate(DoublePoint v, Axis aor, Radians theta) {
        return new DoubleVector(v).rotate(aor, theta);
    }

    // constrain

    public DoubleVector constrain2D(
        DoublePoint max
    ) {
        return constrain2D(new DoubleVector(), max);
    }

    public DoubleVector constrain2D(
        DoublePoint min,
        DoublePoint max
    ) {
        return constrain2D(min.x(), min.y(), max.x(), max.y());
    }

    public DoubleVector constrain3D(
        DoublePoint max
    ) {
        return constrain3D(new DoubleVector(), max);
    }

    public DoubleVector constrain3D(
        DoublePoint min,
        DoublePoint max
    ) {
        return constrain3D(min.x(), min.y(), min.z(), max.x(), max.y(), max.z());
    }

    public DoubleVector constrain2D(double x1, double y1, double x2, double y2) {
        return constrain3D(x1, y1, z, x2, y2, z);
    }

    public DoubleVector constrain3D(double x1, double y1, double z1, double x2, double y2, double z2) {
        return set(
            Math.clamp(x, x1, x2),
            Math.clamp(y, y1, y2),
            Math.clamp(z, z1, z2)
        );
    }

    public static DoubleVector constrain2D(
        DoublePoint v,
        DoublePoint min,
        DoublePoint max
    ) {
        return new DoubleVector(v).constrain2D(min, max);
    }

    public static DoubleVector constrain3D(
        DoublePoint v,
        DoublePoint min,
        DoublePoint max
    ) {
        return new DoubleVector(v).constrain3D(min, max);
    }

    public static DoubleVector constrain2D(
        DoublePoint v,
        double x1, double y1, double x2, double y2
    ) {
        return new DoubleVector(v).constrain2D(x1, y1, x2, y2);
    }

    public static DoubleVector constrain3D(
        DoublePoint v,
        double x1, double y1, double z1, double x2, double y2, double z2
    ) {
        return new DoubleVector(v).constrain3D(x1, y1, z1, x2, y2, z2);
    }
}
