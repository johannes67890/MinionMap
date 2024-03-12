package org.parser;

import java.math.BigDecimal;
import java.util.HashMap;
import javax.xml.stream.XMLStreamReader;

enum Bounds {
    MINLAT, MAXLAT, MINLON, MAXLON
}

public class TagBound extends HashMap<Bounds, BigDecimal>{
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


    // public BigDecimal getMinLat() {
    //     return this.getTag().get(Bounds.MINLAT);
    // }

    // public BigDecimal getMaxLat() {
    //     return this.getTag().get(Bounds.MAXLAT);
    // }

    // public BigDecimal getMinLon() {
    //     return this.getTag().get(Bounds.MINLON);
    // }

    // public BigDecimal getMaxLon() {
    //     return this.getTag().get(Bounds.MAXLON);
    // }
}
