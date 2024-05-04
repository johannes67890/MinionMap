package parser;

import javax.xml.stream.XMLStreamReader;
import util.Type;

enum Bounds {
    MINLAT, MAXLAT, MINLON, MAXLON
}

/**
 * Class representing a bound in the OSM XML file.
 * 
 * <p>
 * The bound is represented by the following attributes:
 * <ul>
 * <li>{@link Bounds#MINLAT} - The minimum latitude of the bound.</li>
 * <li>{@link Bounds#MAXLAT} - The maximum latitude of the bound.</li>
 * <li>{@link Bounds#MINLON} - The minimum longitude of the bound.</li>
 * <li>{@link Bounds#MAXLON} - The maximum longitude of the bound.</li>
 * </ul>
 * </p>
 * @implNote This class implements the {@link Comparable} interface to allow for comparison of bounds.
 * @see Tag The abstract class for a tag.
*/
public class TagBound extends Tag implements Comparable<TagBound>{
    float minLat;
    float maxLat;
    float minLon;
    float maxLon;

    /**
     * Create a new TagBound with the given values.
     * @param reader - The {@link XMLStreamReader} to get the attribute from.
     */
    public TagBound(XMLStreamReader reader) {
        minLat = XMLBuilder.getAttributeByFloat(reader, "minlat");
        maxLat = XMLBuilder.getAttributeByFloat(reader, "maxlat");
        minLon = XMLBuilder.getAttributeByFloat(reader, "minlon");
        maxLon = XMLBuilder.getAttributeByFloat(reader, "maxlon");
    }
    /**
     * Create a new TagBound with the given values.
     * @param minlat - The minimum latitude of the bounds. 
     * @param maxlat - The maximum latitude of the bounds.
     * @param minlon - The minimum longitude of the bounds.
     * @param maxlon - The maximum longitude of the bounds.
     */
    public TagBound(float minlat, float maxlat, float minlon, float maxlon) {
        this.minLat = minlat;
        this.maxLat = maxlat;
        this.minLon = minlon;
        this.maxLon = maxlon;
    }

    /**
     * Get the minimum latitude of the bounds.
     * @return The minimum latitude of the bounds.
     */
    public float getMinLat() {
        return minLat;
    }
    /**
     * Get the maximum latitude of the bounds.
     * @return The maximum latitude of the bounds.
     */
    public float getMaxLat() {
        return maxLat;
    }
    /**
     * Get the minimum longitude of the bounds.
     * @return The minimum longitude of the bounds.
     */
    public float getMinLon() {
        return minLon;
    }
    /**
     * Get the maximum longitude of the bounds.
     * @return The maximum longitude of the bounds.
     */
    public float getMaxLon() {
        return maxLon;
    }

       /**
     * Compares two {@link TagBound} objects.
     *
     * <p>
     * The comparison is based on the numerical values of the bounds.
     * </p>
     *
     * @param   o   the {@link TagBound} to be compared.
     * @return  the value {@code 0} if {@code o} is
     *          numerically equal to this {@link TagBound}; a value
     *          less than {@code 0} if this {@link TagBound}
     *          is numerically less than {@code o};
     *          and a value greater than {@code 0} if this
     *          {@link TagBound} is numerically greater than
     *          {@code o}.
     */
    @Override
    public boolean isInBounds(TagBound bound) {
        TagNode tl = new TagNode(this.getMaxLat(), this.getMinLon()); // Top left
        TagNode tr = new TagNode(this.getMaxLat(), this.getMaxLon()); // Top right
        TagNode bl = new TagNode(this.getMinLat(), this.getMinLon()); // Bottom left
        TagNode br = new TagNode(this.getMinLat(), this.getMaxLon());  // Bottom right
        
        // Check if any of the corners are within the bounds
        if(tl.isInBounds(bound) || tr.isInBounds(bound) || bl.isInBounds(bound) || br.isInBounds(bound)) return true;
        // Check if the bounds are within the corners
        else if
        (this.getMinLat() > bound.getMinLat() && this.getMaxLat() < bound.getMaxLat() &&
         this.getMinLon() > bound.getMinLon() && this.getMaxLon() < bound.getMaxLon()) return true;
        // Check if the bound are bigger than the corners
        else if
        (this.getMinLat() < bound.getMinLat() && this.getMaxLat() > bound.getMaxLat() &&
         this.getMinLon() < bound.getMinLon() && this.getMaxLon() > bound.getMaxLon()) return true;
        return false;
    }

    @Override
    public int compareTo(TagBound o) {
        if (Double.valueOf(this.getMaxLat()).compareTo(Double.valueOf(o.getMaxLat())) == 0) {
            if (Double.valueOf(this.getMinLat()).compareTo(Double.valueOf(o.getMinLat())) == 0) {
                if (Double.valueOf(this.getMaxLon()).compareTo(Double.valueOf(o.getMaxLon())) == 0) {
                    return Double.valueOf(this.getMinLon()).compareTo(Double.valueOf(o.getMinLon()));
                }
                return Double.valueOf(this.getMaxLon()).compareTo(Double.valueOf(o.getMaxLon()));
            }
            return Double.valueOf(this.getMinLat()).compareTo(Double.valueOf(o.getMinLat()));
        }
        return Double.valueOf(this.getMaxLat()).compareTo(Double.valueOf(o.getMaxLat()));
    }

    /**
     * Compares this {@link TagBound} to the specified object.
     * The result is {@code true} if and only if the argument is not {@code null} and is a {@link TagBound} object that represents the same bounds as this object.
     *
     * @param obj The object to compare this {@link TagBound} against
     * @return {@code true} if the given object represents a {@link TagBound} equivalent to this tag, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TagBound)) {
            return false;
        }
        TagBound tag = (TagBound) obj;

        return 
        Double.valueOf(this.getMaxLat()).equals(Double.valueOf(tag.getMaxLat())) && 
        Double.valueOf(this.getMinLat()).equals(Double.valueOf(tag.getMinLat())) && 
        Double.valueOf(this.getMaxLon()).equals(Double.valueOf(tag.getMaxLon())) && 
        Double.valueOf(this.getMinLon()).equals(Double.valueOf(tag.getMinLon()));
    }

    @Override
    public long getId() {
        throw new UnsupportedOperationException("TagBound does not have an id value.");
    }
    @Override
    public float getLat() {
        throw new UnsupportedOperationException("TagBound does not have a latitude value.");
    }
    @Override
    public float getLon() {
        throw new UnsupportedOperationException("TagBound does not have a longitude value.");
    }
    @Override
    public Type getType() {
        throw new UnsupportedOperationException("TagBound does not have a type.");
    }
    @Override
    public String toString(){
        return "minlat: " + minLat + " maxlat: " + maxLat + " minlon: " + minLon + " maxlon: " + maxLon;
    }
 
}
