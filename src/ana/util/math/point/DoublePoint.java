package ana.util.math.point;


// dep

import ana.util.math.vector.DoubleVector;
import ana.util.math.vector.IntVector;


// main

public interface DoublePoint {
    // conversions

    DoubleVector clone();

    default DoublePoint toImmut() { return clone(); }

    IntVector toIntVector();

    // get

    double x();
    double y();
    double z();

    // equals

    boolean equals(DoublePoint p);
    boolean equals(double x, double y);
    boolean equals(double x, double y, double z);

    // mag, magSq, dot, cross

    double mag();
    double magSq();
    double dot(DoublePoint p);
    double dot(double x, double y);
    double dot(double x, double y, double z);
    DoubleVector cross(DoublePoint v);
}
