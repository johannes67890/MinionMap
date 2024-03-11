package org.parser;

import java.math.BigDecimal;
import java.util.HashMap;
import javax.xml.stream.XMLStreamReader;

enum Bounds {
    MINLAT, MAXLAT, MINLON, MAXLON
}

public class TagBound extends Tag<Bounds, BigDecimal>{
    public TagBound() {
        super();
    }

    public TagBound(XMLStreamReader event) {
        super(setBounds(event));
    }

    public BigDecimal getMinLat() {
        return this.getTag().get(Bounds.MINLAT);
    }

    public BigDecimal getMaxLat() {
        return this.getTag().get(Bounds.MAXLAT);
    }

    public BigDecimal getMinLon() {
        return this.getTag().get(Bounds.MINLON);
    }

    public BigDecimal getMaxLon() {
        return this.getTag().get(Bounds.MAXLON);
    }

    /**
    * Construct HashMap with bounds from the XML file.
    * @param event - the XML event.
    * @return HashMap<{@link Bounds}, Float> - the bounds from the XML event.
    */
    static private HashMap<Bounds, BigDecimal> setBounds(XMLStreamReader event) {
        HashMap<Bounds, BigDecimal> bounds = new HashMap<Bounds, BigDecimal>();



        bounds.put(Bounds.MINLAT, getAttributeByBigDecimal(event, "minlat"));
        bounds.put(Bounds.MAXLAT, getAttributeByBigDecimal(event, "maxlat"));
        bounds.put(Bounds.MINLON, getAttributeByBigDecimal(event, "minlon"));
        bounds.put(Bounds.MAXLON, getAttributeByBigDecimal(event, "maxlon"));
        
        return bounds;
    }
}
