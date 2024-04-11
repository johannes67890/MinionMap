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
public class TagBound extends Tag<Bounds> implements Comparable<TagBound>{

    /**
     * Create a new TagBound with the given values.
     * @param minlat - The minimum latitude of the bounds.
     * @param maxlat - The maximum latitude of the bounds.
     * @param minlon - The minimum longitude of the bounds.
     * @param maxlon - The maximum longitude of the bounds.
     */
    public TagBound(XMLStreamReader reader) {
        super(new HashMap<Bounds, Object>(){
            {
                put(Bounds.MINLAT, XMLBuilder.getAttributeByDouble(reader, "minlat"));
                put(Bounds.MAXLAT, XMLBuilder.getAttributeByDouble(reader, "maxlat"));
                put(Bounds.MINLON, XMLBuilder.getAttributeByDouble(reader, "minlon"));
                put(Bounds.MAXLON, XMLBuilder.getAttributeByDouble(reader, "maxlon"));
            }
        });
    }

    public TagBound(double minlat, double maxlat, double minlon, double maxlon) {
        super(new HashMap<Bounds, Object>(){
            {
                put(Bounds.MINLAT, minlat);
                put(Bounds.MAXLAT, maxlat);
                put(Bounds.MINLON, minlon);
                put(Bounds.MAXLON, maxlon);
            }
        });
    }

    /**
     * Get the minimum latitude of the bounds.
     * @return The minimum latitude of the bounds.
     */
    public double getMinLat() {
        return Double.parseDouble(this.get(Bounds.MINLAT).toString());
    }
    /**
     * Get the maximum latitude of the bounds.
     * @return The maximum latitude of the bounds.
     */
    public double getMaxLat() {
        return Double.parseDouble(this.get(Bounds.MAXLAT).toString());
    }
    /**
     * Get the minimum longitude of the bounds.
     * @return The minimum longitude of the bounds.
     */
    public double getMinLon() {
        return Double.parseDouble(this.get(Bounds.MINLON).toString());
    }
    /**
     * Get the maximum longitude of the bounds.
     * @return The maximum longitude of the bounds.
     */
    public double getMaxLon() {
        return Double.parseDouble(this.get(Bounds.MAXLON).toString());
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
    public double getLat() {
        throw new UnsupportedOperationException("TagWay does not have a latitude value.");
    }
    @Override
    public double getLon() {
        throw new UnsupportedOperationException("TagWay does not have a longitude value.");
    }
 
}
