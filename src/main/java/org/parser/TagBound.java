package org.parser;

import java.math.BigDecimal;

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
public class TagBound extends Tag<Bounds>{
    public TagBound(XMLStreamReader reader) {
        super(new HashMap<Bounds, BigDecimal>(){
            {
                put(Bounds.MINLAT, XMLReader.getAttributeByBigDecimal(reader, "minlat"));
                put(Bounds.MAXLAT, XMLReader.getAttributeByBigDecimal(reader, "maxlat"));
                put(Bounds.MINLON, XMLReader.getAttributeByBigDecimal(reader, "minlon"));
                put(Bounds.MAXLON, XMLReader.getAttributeByBigDecimal(reader, "maxlon"));
            }
        });
    }

    public TagBound(BigDecimal maxLat, BigDecimal minLat, BigDecimal maxLon, BigDecimal minLon) {
        super(new HashMap<Bounds, BigDecimal>(){
            {
                put(Bounds.MINLAT, minLat);
                put(Bounds.MAXLAT, maxLat);
                put(Bounds.MINLON, minLon);
                put(Bounds.MAXLON, maxLon);
            }
        });
    }

    /**
     * Get the minimum latitude of the bounds.
     * @return The minimum latitude of the bounds.
     */
    public BigDecimal getMinLat() {
        return new BigDecimal(this.get(Bounds.MINLAT).toString());
    }
    /**
     * Get the maximum latitude of the bounds.
     * @return The maximum latitude of the bounds.
     */
    public BigDecimal getMaxLat() {
        return new BigDecimal(this.get(Bounds.MAXLAT).toString());
    }
    /**
     * Get the minimum longitude of the bounds.
     * @return The minimum longitude of the bounds.
     */
    public BigDecimal getMinLon() {
        return new BigDecimal(this.get(Bounds.MINLON).toString());
    }
    /**
     * Get the maximum longitude of the bounds.
     * @return The maximum longitude of the bounds.
     */
    public BigDecimal getMaxLon() {
        return new BigDecimal(this.get(Bounds.MAXLON).toString());
    }
}
