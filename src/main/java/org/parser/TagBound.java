package org.parser;

import org.Tags;
import java.math.BigDecimal;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.lang.reflect.*;
import org.Tags;


import java.math.BigDecimal;

public class TagBound extends Tag<Tags.Bounds, BigDecimal>{

    public TagBound() {
        super();
    }

    public TagBound(XMLEvent event) {
        super(setBounds(event));
    }

    public BigDecimal getMinLat() {
        return this.getTag().get(Tags.Bounds.MINLAT);
    }

    public BigDecimal getMaxLat() {
        return this.getTag().get(Tags.Bounds.MAXLAT);
    }

    public BigDecimal getMinLon() {
        return this.getTag().get(Tags.Bounds.MINLON);
    }

    public BigDecimal getMaxLon() {
        return this.getTag().get(Tags.Bounds.MAXLON);
    }

    /**
    * Construct HashMap with bounds from the XML file.
    * @param event - the XML event.
    * @return HashMap<{@link Tags.Bounds}, Float> - the bounds from the XML event.
    */
    static private HashMap<Tags.Bounds, BigDecimal> setBounds(XMLEvent event) {
        HashMap<Tags.Bounds, BigDecimal> bounds = new HashMap<Tags.Bounds, BigDecimal>();

        event.asStartElement().getAttributes().forEachRemaining(attribute -> {
            switch (attribute.getName().getLocalPart()) {
                case "minlat":
                    bounds.put(Tags.Bounds.MINLAT, new BigDecimal(attribute.getValue()));
                    break;
                case "maxlat":
                    bounds.put(Tags.Bounds.MAXLAT, new BigDecimal(attribute.getValue()));
                    break;
                case "minlon":
                    bounds.put(Tags.Bounds.MINLON, new BigDecimal(attribute.getValue()));
                    break;
                case "maxlon":
                    bounds.put(Tags.Bounds.MAXLON, new BigDecimal(attribute.getValue()));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid attribute for bounds: " + attribute.getName().getLocalPart());
            }
        });
       
        return bounds;
    }
   
}
