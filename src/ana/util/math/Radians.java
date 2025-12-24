package ana.util.math;


// main

public enum Radians {
    HALF_PI     (1.57079632679489661923132169163975),
    PI          (3.14159265358979323846264338327950),
    NEG_HALF_PI (4.71238898038468985769396507491925),
    TWO_PI      (6.28318530717958647692528676655900);

    // attr

    private final double n;

    // constr

    Radians(double n) {
        this.n = n;
    }

    // get

    public double val() {
        return n;
    }
}
