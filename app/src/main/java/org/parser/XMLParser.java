package org.parser;

import java.math.BigDecimal;
import java.util.HashMap;

import org.Tags;

public class XMLParser {

    XMLReader reader;
    public XMLParser(XMLReader reader) {
        this.reader = reader;
        Tag<Tags.Node, Number> center = parseCenterPoint(reader.bound);
        reader.adresses.forEach(adress -> System.out.println(adress.getNode()));
    }   

    /**
     * Get the center point of the area from a bounds tag. The center point is calculated as the average of the min and max latitude and longitude.
     * The center point is mainly used to split the bounds into smaller areas for effeciency.
     * @param tag - The bounds tag to parse.
     * @return A Tag object containing the center point, based on the {@link Tags.Node} enum. ID is not used.
     */
    private Tag<Tags.Node, Number> parseCenterPoint(ParseTagResult tag) {
        if(!tag.isBounds()) throw new IllegalArgumentException("The tag is not a bounds tag.");

        HashMap<Tags.Bounds, BigDecimal> map = tag.getBounds().getTag();


        BigDecimal x1 = map.get(Tags.Bounds.MINLAT);
        BigDecimal x2 = map.get(Tags.Bounds.MAXLAT);
        BigDecimal y1 = map.get(Tags.Bounds.MINLON);
        BigDecimal y2 = map.get(Tags.Bounds.MAXLON);
    
        // Calculate the center of the area.
        BigDecimal centerX = x1.add(x2.subtract(x1).divide(new BigDecimal(2)));
        BigDecimal centerY = y1.add(y2.subtract(y1).divide(new BigDecimal(2)));
    
        Tag<Tags.Node, Number> center = new Tag<Tags.Node, Number>(new HashMap<Tags.Node, Number>() {{
            put(Tags.Node.LAT, centerX);
            put(Tags.Node.LON, centerY);
        }});

        return center;
    }

    private void parseSplitArea(ParseTagResult bound) {
        // Split the area into smaller areas.
        // The area is split into 4 smaller areas, each with a center point.
        // The center point is calculated as the average of the min and max latitude and longitude.
        // The center point is mainly used to split the bounds into smaller areas for effeciency.
        
        BigDecimal x1 = bound.getBounds().getTag().get(Tags.Bounds.MINLAT);
        BigDecimal x2 = bound.getBounds().getTag().get(Tags.Bounds.MAXLAT);
        BigDecimal y1 = bound.getBounds().getTag().get(Tags.Bounds.MINLON);
        BigDecimal y2 = bound.getBounds().getTag().get(Tags.Bounds.MAXLON);

        System.out.println(reader);



    }


}
