/******************************************************************************
 *  Compilation:  javac Point3D.java
 *  Execution:    java Point3D x0 y0 n
 *
 *  Immutable point data type for points in the plane.
 *
 ******************************************************************************/

package structures.KDTree;

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

    public Point3D(float x, float y, int z) {
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
            this.z = (byte) z;
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

    /**
     * Returns the z-coordiante
     * @return the z-coordinate
     */
    public int z() {
        return z;
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
     * Returns the 2-Dimensional Euclidean distance between this point and that point.
     * 
     * @param that the other point
     * @return the Euclidean distance between this point and that point
     */
    public float distance2DTo(Point3D that) {
        float dx = this.x - that.x;
        float dy = this.y - that.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
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
     * Returns the square of the 2D Euclidean distance between this point and that
     * point.
     * 
     * @param that the other point
     * @return the square of the Euclidean distance between this point and that
     *         point
     */
    public float distance2DSquaredTo(Point3D that) {
        float dx = this.x - that.x;
        float dy = this.y - that.y;
        return dx * dx + dy * dy;
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
