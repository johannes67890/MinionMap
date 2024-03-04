package org.parser;

import java.math.BigDecimal;
import java.util.HashMap;

import org.Tags;
// TODO: name the class to "FileParser"?
public class XMLParser {

    XMLReader reader;
    public XMLParser(XMLReader reader) {
        
    }   

    /**
     * Get the center point of the area from a bounds tag. The center point is calculated as the average of the min and max latitude and longitude.
     * The center point is mainly used to split the bounds into smaller areas for effeciency.
     * @param tag - The bounds tag to parse.
     * @return A Tag object containing the center point, based on the {@link Tags.Node} enum. ID is not used.
     */
    private Tag<Tags.Node, BigDecimal> parseCenterPoint(Tag tag) {
        // if(!tag.isBounds()) throw new IllegalArgumentException("The tag is not a bounds tag.");

        HashMap<Tags.Bounds, BigDecimal> map = tag.getTag();


        BigDecimal x1 = map.get(Tags.Bounds.MINLAT);
        BigDecimal x2 = map.get(Tags.Bounds.MAXLAT);
        BigDecimal y1 = map.get(Tags.Bounds.MINLON);
        BigDecimal y2 = map.get(Tags.Bounds.MAXLON);
    
        // Calculate the center of the area.
        BigDecimal centerX = x1.add(x2.subtract(x1).divide(new BigDecimal(2)));
        BigDecimal centerY = y1.add(y2.subtract(y1).divide(new BigDecimal(2)));
    
        Tag<Tags.Node, BigDecimal> center = new Tag<Tags.Node, BigDecimal>(new HashMap<Tags.Node, BigDecimal>() {{
            put(Tags.Node.LAT, centerX);
            put(Tags.Node.LON, centerY);
        }});

        return center;
    }

    private void parseSplitArea(Tag<Tags.Node, BigDecimal> bound) {
        // Split the area into smaller areas.
        // The area is split into 4 smaller areas, each with a center point.
        // The center point is calculated as the average of the min and max latitude and longitude.
        // The center point is mainly used to split the bounds into smaller areas for effeciency.
        Tag<Tags.Node, BigDecimal> center = parseCenterPoint(bound);
        BigDecimal cx = center.getTag().get(Tags.Node.LAT);
        BigDecimal cy = center.getTag().get(Tags.Node.LON);

        BigDecimal x1 = bound.getTag().get(Tags.Bounds.MINLAT);
        BigDecimal x2 = bound.getTag().get(Tags.Bounds.MAXLAT);
        BigDecimal y1 = bound.getTag().get(Tags.Bounds.MINLON);
        BigDecimal y2 = bound.getTag().get(Tags.Bounds.MAXLON);

        
        Tag<Tags.Node, BigDecimal> west = new Tag<Tags.Node, BigDecimal>(new HashMap<Tags.Node, BigDecimal>() {{
            put(Tags.Node.LAT, x2.subtract(cx.subtract(x1)));
            put(Tags.Node.LON, y1);
        }});

        Tag<Tags.Node, BigDecimal> east = new Tag<Tags.Node, BigDecimal>(new HashMap<Tags.Node, BigDecimal>() {{
            put(Tags.Node.LAT, x2.subtract(cx.subtract(x1)));
            put(Tags.Node.LON, y2);
        }});

        Tag<Tags.Node, BigDecimal> north = new Tag<Tags.Node, BigDecimal>(new HashMap<Tags.Node, BigDecimal>() {{
            put(Tags.Node.LAT, x2);
            put(Tags.Node.LON, y2.subtract(cy.subtract(y1)));
        }});

        Tag<Tags.Node, BigDecimal> south = new Tag<Tags.Node, BigDecimal>(new HashMap<Tags.Node, BigDecimal>() {{
            put(Tags.Node.LAT, x1);
            put(Tags.Node.LON, y2.subtract(cy.subtract(y1)));
        }});
    }


}
