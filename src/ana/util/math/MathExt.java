package ana.util.math;


// dep

import ana.util.math.vector.point.DoublePoint;
import ana.util.math.vector.point.IntPoint;


// main

public final class MathExt {
    // no inst

    private MathExt() {}

    // const

    public static final double SQRT2 = Math.sqrt(2);


    // --


    // lerp

    public static double lerpClamp(double n, double a, double b) {
        return lerp(Math.clamp(n, 0, 1), a, b);
    }

    public static double lerp(double n, double a, double b) {
        return a + (n * (b - a));
        // return (a * (n-1)) + (b * (n));
    }

    // range

    public static boolean inBounds(IntPoint p, IntPoint min, IntPoint max) {
        return inBounds(p.x(), p.y(), p.z(), min.x(), min.y(), max.z(), max.x(), max.y(), max.z());
    }

    public static boolean inBounds(DoublePoint p, DoublePoint min, DoublePoint max) {
        return inBounds(p.x(), p.y(), p.z(), min.x(), min.y(), max.z(), max.x(), max.y(), max.z());
    }

    public static boolean inBounds(int x, int y, int minX, int minY, int maxX, int maxY) {
        return inBounds(x, y, 0, minX, minY, 0, maxX, maxY, 0);
    }

    public static boolean inBounds(double x, double y, double minX, double minY, double maxX, double maxY) {
        return inBounds(x, y, 0, minX, minY, 0, maxX, maxY, 0);
    }

    public static boolean inBounds(
        int x, int y, int z,
        int minX, int minY, int minZ,
        int maxX, int maxY, int maxZ
    ) {
        return (
            inRangeIncl(x, minX, maxX)
            && inRangeIncl(y, minY, maxY)
            && inRangeIncl(z, minZ, maxZ)
        );
    }

    public static boolean inBounds(
        double x, double y, double z,
        double minX, double minY, double minZ,
        double maxX, double maxY, double maxZ
    ) {
        return (
               inRangeIncl(x, minX, maxX)
            && inRangeIncl(y, minY, maxY)
            && inRangeIncl(z, minZ, maxZ)
        );
    }

    public static boolean inRange(int val, int min, int max, boolean inclusiveMin, boolean inclusiveMax) {
        if (!inclusiveMin && !inclusiveMax) return inRangeIncl(val, min, max);

        boolean lower = inclusiveMin ? min <= val : min < val;
        boolean upper = inclusiveMax ? val <= max : val < max;
        return lower && upper;
    }

    public static boolean inRange(double val, double min, double max, boolean inclusiveMin, boolean inclusiveMax) {
        if (!inclusiveMin && !inclusiveMax) return inRangeIncl(val, min, max);

        boolean lower = inclusiveMin ? min <= val : min < val;
        boolean upper = inclusiveMax ? val <= max : val < max;
        return lower && upper;
    }

    public static boolean inRangeIncl(int val, int min, int max) {
        return min <= val && val <= max;
    }

    public static boolean inRangeIncl(double val, double min, double max) { // inclusive
        return min <= val && val < max;
    }

    // random

    public static int randSgn() {
        return Math.random() > 0 ? 1 : -1;
    }

    public static double randDouble(double max) {
        return randDouble(0, max);
    }

    public static double randDouble(double min, double max) {
        return map(Math.random(), 0, 1, min, max);
    }

    public static int randInt(int max) {
        return randInt(0, max);
    }

    public static int randInt(int min, int max) {
        return (int) map(Math.random(), 0, 1, min, max);
    }

    // normalize

    public static double norm(double val, double min, double max) {
        return (val - min) / (max - min);
    }

    // map

    public static double mapClamp(double val, double min1, double max1, double min2, double max2) {
        return map(Math.clamp(val, min1, max1), min1, max1, min2, max2);
    }

    public static double map(double val, double min1, double max1, double min2, double max2) {
        return (norm(val, min1, max1) * (max2 - min2)) + min2;
    }

    // constrain

    public static double[] clampAll(double[] vals, double min, double max) {
        double[] r = new double[vals.length];

        for (int i = 0; i < vals.length; i++) {
            r[i] = Math.clamp(vals[i], min, max);
        }

        return r;
    }

    public static int[] clampAll(int[] vals, int min, int max) {
        int[] r = new int[vals.length];

        for (int i = 0; i < vals.length; i++) {
            r[i] = Math.clamp(vals[i], min, max);
        }

        return r;
    }

    public static long[] clampAll(long[] vals, long min, long max) {
        long[] r = new long[vals.length];

        for (int i = 0; i < vals.length; i++) {
            r[i] = Math.clamp(vals[i], min, max);
        }

        return r;
    }

    // max

    public static double max(double val, double... vals) {
        double m = val;
        for (double v : vals) {
            m = Math.max(m, v);
        }
        return m;
    }

    public static int max(int val, int... vals) {
        int m = val;
        for (int v : vals) {
            m = Math.max(m, v);
        }
        return m;
    }

    public static long max(long val, long... vals) {
        long m = val;
        for (long v : vals) {
            m = Math.max(m, v);
        }
        return m;
    }

    // min

    public static double min(double val, double... vals) {
        double m = val;
        for (double v : vals) {
            m = Math.min(m, v);
        }
        return m;
    }

    public static int min(int val, int... vals) {
        int m = val;
        for (int v : vals) {
            m = Math.min(m, v);
        }
        return m;
    }

    public static long min(long val, long... vals) {
        long m = val;
        for (long v : vals) {
            m = Math.min(m, v);
        }
        return m;
    }
}
