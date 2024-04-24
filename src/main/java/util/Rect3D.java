/******************************************************************************
 *  Compilation:  javac Rect3D.java
 *  Execution:    none
 *  Dependencies: Point3D.java
 *
 *  Immutable data type for 3D axis-aligned rectangle.
 *
 ******************************************************************************/

package util;

/**
 * This class is made from the RectHV basis but changed to accomendate
 * 3 dimensional points.
 * 
 * The {@code Rect3D} class is an immutable data type to encapsulate a
 * two-dimensional axis-aligned rectagle with real-value coordinates.
 * The rectangle is <em>closed</em>—it includes the points on the boundary.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/12oop">Section 1.2</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */

public final class Rect3D {
    private final float xmin, ymin;  // minimum x- and y-coordinates
    private final float xmax, ymax; // maximum x- and y-coordinates
    private final int zmin, zmax;

    /**
     * Initializes a new rectangle [<em>xmin</em>, <em>xmax</em>]
     * x [<em>ymin</em>, <em>ymax</em>].
     *
     * @param xmin the <em>x</em>-coordinate of the lower-left endpoint
     * @param ymin the <em>y</em>-coordinate of the lower-left endpoint
     * @param zmin the <em>z</em>-coordinate of the lower-left endpoint
     * @param xmax the <em>x</em>-coordinate of the upper-right endpoint
     * @param ymax the <em>y</em>-coordinate of the upper-right endpoint
     * @param zmax the <em>z</em>-coordinate of the upper-right endpoint
     * @throws IllegalArgumentException if any of {@code xmin},
     *                                  {@code xmax}, {@code ymin}, {@code ymax},
     *                                  {@code zmin} or {@code zmax}
     *                                  is {@code Double.NaN}.
     * @throws IllegalArgumentException if {@code xmax < xmin} or
     *                                  {@code ymax < ymin} or {@code zmax < zmin}.
     */
    public Rect3D(float xmin, float ymin, int zmin, float xmax, float ymax, int zmax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.zmin = zmin;
        this.xmax = xmax;
        this.ymax = ymax;
        this.zmax = zmax;
        if (Double.isNaN(xmin) || Double.isNaN(xmax)) {
            throw new IllegalArgumentException("x-coordinate is NaN: " + toString());
        }
        if (Double.isNaN(ymin) || Double.isNaN(ymax)) {
            throw new IllegalArgumentException("y-coordinate is NaN: " + toString());
        }
        if (Double.isNaN(zmin) || Double.isNaN(zmax)) {
            throw new IllegalArgumentException("zcoordinate is NaN: " + toString());
        }
        if (xmax < xmin) {
            throw new IllegalArgumentException("xmax < xmin: " + toString());
        }
        if (ymax < ymin) {
            throw new IllegalArgumentException("ymax < ymin: " + toString());
        }
        if (zmax < zmin) {
            throw new IllegalArgumentException("zmax < zmin" + toString());
        }
    }

    /**
     * Returns the minimum <em>x</em>-coordinate of any point in this rectangle.
     *
     * @return the minimum <em>x</em>-coordinate of any point in this rectangle
     */
    public double xmin() {
        return xmin;
    }

    /**
     * Returns the maximum <em>x</em>-coordinate of any point in this rectangle.
     *
     * @return the maximum <em>x</em>-coordinate of any point in this rectangle
     */
    public double xmax() {
        return xmax;
    }

    /**
     * Returns the minimum <em>y</em>-coordinate of any point in this rectangle.
     *
     * @return the minimum <em>y</em>-coordinate of any point in this rectangle
     */
    public double ymin() {
        return ymin;
    }

    /**
     * Returns the maximum <em>y</em>-coordinate of any point in this rectangle.
     *
     * @return the maximum <em>y</em>-coordinate of any point in this rectangle
     */
    public double ymax() {
        return ymax;
    }

    public double zmin() {
        return zmin;
    }

    public double zmax() {
        return zmax;
    }

    /**
     * Returns the width of this rectangle.
     *
     * @return the width of this rectangle {@code xmax - xmin}
     */
    public double width() {
        return xmax - xmin;
    }

    /**
     * Returns the height of this rectangle.
     *
     * @return the height of this rectangle {@code ymax - ymin}
     */
    public double height() {
        return ymax - ymin;
    }

    public double depth() {
        return zmax - zmin;
    }

    /**
     * Returns true if the two rectangles intersect. This includes
     * <em>improper intersections</em> (at points on the boundary
     * of each rectangle) and <em>nested intersctions</em>
     * (when one rectangle is contained inside the other)
     *
     * @param that the other rectangle
     * @return {@code true} if this rectangle intersect the argument
     *         rectangle at one or more points
     */
    public boolean intersects(Rect3D that) {
        return this.xmax >= that.xmin && this.ymax >= that.ymin
                && that.xmax >= this.xmin && that.ymax >= this.ymin
                && this.zmax >= that.zmin && that.zmax >= this.zmin;
    }

    /**
     * Returns true if this rectangle contain the point.
     * 
     * @param p the point
     * @return {@code true} if this rectangle contain the point {@code p},
     *         possibly at the boundary; {@code false} otherwise
     */
    public boolean contains(Point3D p) {
        return (p.x() >= xmin) && (p.x() <= xmax)
                && (p.y() >= ymin) && (p.y() <= ymax)
                && (p.z() >= zmin) && (p.z() <= zmax);
    }

    /**
     * Returns the Euclidean distance between this rectangle and the point
     * {@code p}.
     *
     * @param p the point
     * @return the Euclidean distance between the point {@code p} and the closest
     *         point
     *         on this rectangle; 0 if the point is contained in this rectangle
     */
    public double distanceTo(Point3D p) {
        return Math.sqrt(this.distanceSquaredTo(p));
    }

    /**
     * Returns the square of the Euclidean distance between this rectangle and the
     * point {@code p}.
     *
     * @param p the point
     * @return the square of the Euclidean distance between the point {@code p} and
     *         the closest point on this rectangle; 0 if the point is contained
     *         in this rectangle
     */
    public double distanceSquaredTo(Point3D p) {
        double dx = 0.0, dy = 0.0, dz = 0.0;
        if (p.x() < xmin)
            dx = p.x() - xmin;
        else if (p.x() > xmax)
            dx = p.x() - xmax;
        if (p.y() < ymin)
            dy = p.y() - ymin;
        else if (p.y() > ymax)
            dy = p.y() - ymax;
        if (p.z() < zmin)
            dz = p.z() - zmin;
        else if (p.z() > zmax)
            dz = p.z() - zmax;
        return dx * dx + dy * dy + dz * dz;
    }

    /**
     * Compares this rectangle to the specified rectangle.
     *
     * @param other the other rectangle
     * @return {@code true} if this rectangle equals {@code other};
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
        Rect3D that = (Rect3D) other;
        if (this.xmin != that.xmin)
            return false;
        if (this.ymin != that.ymin)
            return false;
        if (this.xmax != that.xmax)
            return false;
        if (this.ymax != that.ymax)
            return false;
        return true;
    }

    /**
     * Returns a string representation of this rectangle.
     *
     * @return a string representation of this rectangle, using the format
     *         {@code [xmin, xmax] x [ymin, ymax]}
     */
    @Override
    public String toString() {
        return "[" + xmin + ", " + xmax + "] x [" + ymin + ", " + ymax + "] x [" + zmin + ", " + zmax + "]";
    }

}
