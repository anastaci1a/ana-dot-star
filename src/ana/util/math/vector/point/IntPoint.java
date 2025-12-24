package ana.util.math.vector.point;


// dep

import ana.util.math.vector.DoubleVector;
import ana.util.math.vector.IntVector;


// main

public interface IntPoint {
    // conversions

    IntVector clone();

    default IntPoint toImmut() { return clone(); }

    DoubleVector toDoubleVector();

    // get

    int x();
    int y();
    int z();

    // equals

    boolean equals(IntPoint p);
    boolean equals(int x, int y);
    boolean equals(int x, int y, int z);

    // mag, magSq, dot, cross

    double mag();
    int magSq();
    int dot(IntPoint p);
    int dot(int x, int y);
    int dot(int x, int y, int z);
    IntVector cross(IntPoint v);
}
