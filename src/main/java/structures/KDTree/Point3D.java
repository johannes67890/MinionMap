/******************************************************************************
 *  Compilation:  javac Point3D.java
 *  Execution:    java Point3D x0 y0 n
 *  Dependencies: StdDraw.java StdRandom.java
 *
 *  Immutable point data type for points in the plane.
 *
 ******************************************************************************/

package structures.KDTree;

import java.util.Comparator;

/**
 * This class has been build on top of Robert and Kevins Point2D.
 * It has been changed to accomendate 3 dimensional points.
 * 
 * The {@code Point} class is an immutable data type to encapsulate a
 * two-dimensional point with real-value coordinates.
 * <p>
 * Note: in order to deal with the difference behavior of double and
 * Double with respect to -0.0 and +0.0, the Point3D constructor converts
 * any coordinates that are -0.0 to +0.0.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/12oop">Section 1.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public final class Point3D implements Comparable<Point3D> {

    /**
     * Compares two points by x-coordinate.
     */
    public static final Comparator<Point3D> X_ORDER = new XOrder();

    /**
     * Compares two points by y-coordinate.
     */
    public static final Comparator<Point3D> Y_ORDER = new YOrder();

    /**
     * Compares two points by z-coordinate.
     */
    public static final Comparator<Point3D> Z_ORDER = new ZOrder();
    /**
     * Compares two points by polar radius.
     */
    public static final Comparator<Point3D> R_ORDER = new ROrder();

    private final float x; // x coordinate
    private final float y; // y coordinate
    private final byte z; // z coordinate

    /**
     * Initializes a new point (x, y).
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @throws IllegalArgumentException if either {@code x} or {@code y}
     *                                  is {@code Double.NaN},
     *                                  {@code Double.POSITIVE_INFINITY} or
     *                                  {@code Double.NEGATIVE_INFINITY}
     */
    public Point3D(float x, float y, byte z) {
        if (Float.isInfinite(x) || Float.isInfinite(y))
            throw new IllegalArgumentException("Coordinates must be finite");
        if (Float.isNaN(x) || Float.isNaN(y))
            throw new IllegalArgumentException("Coordinates cannot be NaN");
        if (x == 0.0)
            this.x = 0.0f; // convert -0.0 to +0.0
        else
            this.x = x;

        if (y == 0.0)
            this.y = 0.0f; // convert -0.0 to +0.0
        else
            this.y = y;

        if (z == 0.0)
            this.z = 0;
        else
            this.z = z;
    }

    /**
     * Returns the x-coordinate.
     * 
     * @return the x-coordinate
     */
    public float x() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     * 
     * @return the y-coordinate
     */
    public float y() {
        return y;
    }

    public int z() {
        return z;
    }

    /**
     * Returns the angle between this point and that point.
     * 
     * @return the angle in radians (between –&pi; and &pi;) between this point and
     *         that point (0 if equal)
     */
    private float angleTo(Point3D that) {
        float dx = that.x - this.x;
        float dy = that.y - this.y;
        return (float) Math.atan2(dy, dx);
    }

    /**
     * Returns true if a→b→c is a counterclockwise turn.
     * 
     * @param a first point
     * @param b second point
     * @param c third point
     * @return { -1, 0, +1 } if a→b→c is a { clockwise, collinear; counterclocwise }
     *         turn.
     */
    public static int ccw(Point3D a, Point3D b, Point3D c) {
        float area2 = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
        if (area2 < 0)
            return -1;
        else if (area2 > 0)
            return +1;
        else
            return 0;
    }

    /**
     * Returns the Euclidean distance between this point and that point.
     * 
     * @param that the other point
     * @return the Euclidean distance between this point and that point
     */
    public float distanceTo(Point3D that) {
        float dx = this.x - that.x;
        float dy = this.y - that.y;
        float dz = this.z - that.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    /**
     * Returns the square of the Euclidean distance between this point and that
     * point.
     * 
     * @param that the other point
     * @return the square of the Euclidean distance between this point and that
     *         point
     */
    public float distanceSquaredTo(Point3D that) {
        float dx = this.x - that.x;
        float dy = this.y - that.y;
        float dz = this.z - that.z;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point (x1,
     * y1)
     * if and only if either {@code y0 < y1} or if {@code y0 == y1} and
     * {@code x0 < x1}.
     *
     * @param that the other point
     * @return the value {@code 0} if this string is equal to the argument
     *         string (precisely when {@code equals()} returns {@code true});
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point3D that) {
        if (this.y < that.y)
            return -1;
        if (this.y > that.y)
            return +1;
        if (this.x < that.x)
            return -1;
        if (this.x > that.x)
            return +1;
        if (this.z < that.z)
            return -1;
        if (this.z > that.z)
            return +1;
        return 0;
    }

    /**
     * Compares two points by polar angle (between 0 and 2&pi;) with respect to this
     * point.
     *
     * @return the comparator
     */
    public Comparator<Point3D> polarOrder() {
        return new PolarOrder();
    }

    /**
     * Compares two points by atan2() angle (between –&pi; and &pi;) with respect to
     * this point.
     *
     * @return the comparator
     */
    public Comparator<Point3D> atan2Order() {
        return new Atan2Order();
    }

    /**
     * Compares two points by distance to this point.
     *
     * @return the comparator
     */
    public Comparator<Point3D> distanceToOrder() {
        return new DistanceToOrder();
    }

    // compare points according to their x-coordinate
    private static class XOrder implements Comparator<Point3D> {
        public int compare(Point3D p, Point3D q) {
            if (p.x < q.x)
                return -1;
            if (p.x > q.x)
                return +1;
            return 0;
        }
    }

    // compare points according to their y-coordinate
    private static class YOrder implements Comparator<Point3D> {
        public int compare(Point3D p, Point3D q) {
            if (p.y < q.y)
                return -1;
            if (p.y > q.y)
                return +1;
            return 0;
        }
    }

    // compare points according to their z-coordinate
    private static class ZOrder implements Comparator<Point3D> {
        public int compare(Point3D p, Point3D q) {
            if (p.z < q.z)
                return -1;
            if (p.z > q.z)
                return +1;
            return 0;
        }
    }

    // compare points according to their polar radius
    private static class ROrder implements Comparator<Point3D> {
        public int compare(Point3D p, Point3D q) {
            double delta = (p.x * p.x + p.y * p.y + p.z * p.z) - (q.x * q.x + q.y * q.y + q.z * q.z);
            if (delta < 0)
                return -1;
            if (delta > 0)
                return +1;
            return 0;
        }
    }

    // compare other points relative to atan2 angle (bewteen -pi/2 and pi/2) they
    // make with this Point
    private class Atan2Order implements Comparator<Point3D> {
        public int compare(Point3D q1, Point3D q2) {
            double angle1 = angleTo(q1);
            double angle2 = angleTo(q2);
            if (angle1 < angle2)
                return -1;
            else if (angle1 > angle2)
                return +1;
            else
                return 0;
        }
    }

    // compare other points relative to polar angle (between 0 and 2pi) they make
    // with this Point
    private class PolarOrder implements Comparator<Point3D> {
        public int compare(Point3D q1, Point3D q2) {
            double dx1 = q1.x - x;
            double dy1 = q1.y - y;
            double dz1 = q1.z - z;
            double dx2 = q2.x - x;
            double dy2 = q2.y - y;
            double dz2 = q2.z - z;

            if (dy1 >= 0 && dy2 < 0)
                return -1; // q1 above; q2 below
            else if (dy2 >= 0 && dy1 < 0)
                return +1; // q1 below; q2 above
            else if (dy1 == 0 && dy2 == 0) { // 3-collinear and horizontal
                if (dx1 >= 0 && dx2 < 0)
                    return -1;
                else if (dx2 >= 0 && dx1 < 0)
                    return +1;
                else if (dx1 == 0 && dx2 == 0) {
                    if (dz1 >= 0 && dz2 < 0)
                        return -1;
                    else if (dz2 >= 0 && dz1 < 0)
                        return +1;
                    else
                        return 0;
                } else
                    return 0;

            } else
                return -ccw(Point3D.this, q1, q2); // both above or below

            // Note: ccw() recomputes dx1, dy1, dx2, and dy2
        }
    }

    // compare points according to their distance to this point
    private class DistanceToOrder implements Comparator<Point3D> {
        public int compare(Point3D p, Point3D q) {
            double dist1 = distanceSquaredTo(p);
            double dist2 = distanceSquaredTo(q);
            if (dist1 < dist2)
                return -1;
            else if (dist1 > dist2)
                return +1;
            else
                return 0;
        }
    }

    /**
     * Compares this point to the specified point.
     * 
     * @param other the other point
     * @return {@code true} if this point equals {@code other};
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (other == null)
            return false;
        if (other.getClass() != this.getClass())
            return false;
        Point3D that = (Point3D) other;
        return this.x == that.x && this.y == that.y && this.z == that.z;
    }

    /**
     * Return a string representation of this point.
     * 
     * @return a string representation of this point in the format (x, y)
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
