package parser;

import java.util.HashMap;
import javax.xml.stream.XMLStreamReader;
import gnu.trove.map.hash.TObjectDoubleHashMap;

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
public class TagBound extends Tag<Bounds, TObjectDoubleHashMap<Bounds>> implements Comparable<TagBound>{

    private TObjectDoubleHashMap<Bounds> bounds = new TObjectDoubleHashMap<Bounds>();

    /**
     * Create a new TagBound with the given values.
     * @param minlat - The minimum latitude of the bounds.
     * @param maxlat - The maximum latitude of the bounds.
     * @param minlon - The minimum longitude of the bounds.
     * @param maxlon - The maximum longitude of the bounds.
     */
    public TagBound(XMLStreamReader reader) {
        bounds = new TObjectDoubleHashMap<Bounds>(){
            {
                put(Bounds.MINLAT, XMLBuilder.getAttributeByDouble(reader, "minlat"));
                put(Bounds.MAXLAT, XMLBuilder.getAttributeByDouble(reader, "maxlat"));
                put(Bounds.MINLON, XMLBuilder.getAttributeByDouble(reader, "minlon"));
                put(Bounds.MAXLON, XMLBuilder.getAttributeByDouble(reader, "maxlon"));
            }
        };
    }

    public TagBound(double minlat, double maxlat, double minlon, double maxlon) {
        bounds = new TObjectDoubleHashMap<Bounds>(){
            {
                put(Bounds.MINLAT, minlat);
                put(Bounds.MAXLAT, maxlat);
                put(Bounds.MINLON, minlon);
                put(Bounds.MAXLON, maxlon);
            }
        };
    }

    @Override
    public TObjectDoubleHashMap<Bounds> getMap() {
        return this.bounds;
    }

    @Override
    public long getId() {
        throw new UnsupportedOperationException("TagBound does not have an id value.");
    }

    /**
     * Get the minimum latitude of the bounds.
     * @return The minimum latitude of the bounds.
     */
    public double getMinLat() {
        return bounds.get(Bounds.MINLAT);
    }
    /**
     * Get the maximum latitude of the bounds.
     * @return The maximum latitude of the bounds.
     */
    public double getMaxLat() {
        return bounds.get(Bounds.MAXLAT);
    }
    /**
     * Get the minimum longitude of the bounds.
     * @return The minimum longitude of the bounds.
     */
    public double getMinLon() {
        return bounds.get(Bounds.MINLON);
    }
    /**
     * Get the maximum longitude of the bounds.
     * @return The maximum longitude of the bounds.
     */
    public double getMaxLon() {
        return bounds.get(Bounds.MAXLON);
    }

    @Override
    public boolean isEmpty() {
        return bounds.isEmpty();
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
        return Double.valueOf(this.getMaxLat()).equals(Double.valueOf(tag.getMaxLat())) && Double.valueOf(this.getMinLat()).equals(Double.valueOf(tag.getMinLat())) && Double.valueOf(this.getMaxLon()).equals(Double.valueOf(tag.getMaxLon())) && Double.valueOf(this.getMinLon()).equals(Double.valueOf(tag.getMinLon()));
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
