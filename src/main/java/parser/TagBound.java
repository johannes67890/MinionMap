package parser;

import java.util.HashMap;
import javax.xml.stream.XMLStreamReader;

enum Bounds {
    MINLAT, MAXLAT, MINLON, MAXLON
}

/**
 * Class for storing a {@link HashMap} of the bounds tags.
 * Contains the following tags:
 * <p>
 * {@link Bounds#MINLAT}, {@link Bounds#MAXLAT}, {@link Bounds#MINLON}, {@link Bounds#MAXLON}
 * </p>
*/
public class TagBound extends Tag implements Comparable<TagBound>{

    float minLat;
    float maxLat;
    float minLon;
    float maxLon;


    /**
     * Create a new TagBound with the given values.
     * @param minlat - The minimum latitude of the bounds. 
     * @param maxlat - The maximum latitude of the bounds.
     * @param minlon - The minimum longitude of the bounds.
     * @param maxlon - The maximum longitude of the bounds.
     */
    public TagBound(XMLStreamReader reader) {
        minLat = XMLBuilder.getAttributeByFloat(reader, "minlat");
        maxLat = XMLBuilder.getAttributeByFloat(reader, "maxlat");
        minLon = XMLBuilder.getAttributeByFloat(reader, "minlon");
        maxLon = XMLBuilder.getAttributeByFloat(reader, "maxlon");


    }

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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TagBound)) {
            return false;
        }
        TagBound tag = (TagBound) obj;
        return Double.valueOf(
        this.getMaxLat()).equals(Double.valueOf(tag.getMaxLat())) && Double.valueOf(this.getMinLat()).equals(Double.valueOf(tag.getMinLat())) && 
        Double.valueOf(this.getMaxLon()).equals(Double.valueOf(tag.getMaxLon())) 
        && Double.valueOf(this.getMinLon()).equals(Double.valueOf(tag.getMinLon()));
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
