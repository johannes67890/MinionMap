// package org.parser;

// import java.math.BigDecimal;
// import java.util.HashMap;

// // TODO: name the class to "FileParser"?
// public class XMLParser {

//     XMLReader reader;
//     public XMLParser(XMLReader reader) {
        
//     }   

//     /**
//      * Get the center point of the area from a bounds tag. The center point is calculated as the average of the min and max latitude and longitude.
//      * The center point is mainly used to split the bounds into smaller areas for effeciency.
// //      * @param tag - The bounds tag to parse.
// //      * @return A Tag object containing the center point, based on the {@link Node} enum. ID is not used.
// //      */
// //     private Tag<Node, BigDecimal> parseCenterPoint(Tag tag) {
// //         // if(!tag.isBounds()) throw new IllegalArgumentException("The tag is not a bounds tag.");

// //         HashMap<Bounds, BigDecimal> map = tag.getTag();


// //         BigDecimal x1 = map.get(Bounds.MINLAT);
// //         BigDecimal x2 = map.get(Bounds.MAXLAT);
// //         BigDecimal y1 = map.get(Bounds.MINLON);
// //         BigDecimal y2 = map.get(Bounds.MAXLON);
    
// //         // Calculate the center of the area.
// //         BigDecimal centerX = x1.add(x2.subtract(x1).divide(new BigDecimal(2)));
// //         BigDecimal centerY = y1.add(y2.subtract(y1).divide(new BigDecimal(2)));
    
// //         Tag<Node, BigDecimal> center = new Tag<Node, BigDecimal>(new HashMap<Node, BigDecimal>() {{
// //             put(Node.LAT, centerX);
// //             put(Node.LON, centerY);
// //         }});

// //         return center;
// //     }

// //     private void parseSplitArea(TagBound bound) {
// //         // Split the area into smaller areas.
// //         // The area is split into 4 smaller areas, each with a center point.
// //         // The center point is calculated as the average of the min and max latitude and longitude.
// //         // The center point is mainly used to split the bounds into smaller areas for effeciency.
// //         Tag<Node, BigDecimal> center = parseCenterPoint(bound);
// //         BigDecimal cx = center.getTag().get(Node.LAT);
// //         BigDecimal cy = center.getTag().get(Node.LON);

// //         BigDecimal x1 = bound.getTag().get(Bounds.MINLAT);
// //         BigDecimal x2 = bound.getTag().get(Bounds.MAXLAT);
// //         BigDecimal y1 = bound.getTag().get(Bounds.MINLON);
// //         BigDecimal y2 = bound.getTag().get(Bounds.MAXLON);


//         public CompasPoints(TagNode center, TagBound bound) {
//             HashMap<Direction, TagNode> map = calculateCompasPoints(center, bound);
//             this.north = map.get(Direction.NORTH);
//             this.south = map.get(Direction.SOUTH);
//             this.east = map.get(Direction.EAST);
//             this.west = map.get(Direction.WEST);
//         }
    
//         public HashMap<Direction, TagNode> calculateCompasPoints(TagNode center, TagBound bound) {
//            HashMap<Direction, TagNode> map = new HashMap<Direction, TagNode>();
    
//            // North: lat = maxLat, lon = maxLon - (centerLon - minLon)
//            map.put(Direction.NORTH, new TagNode(new HashMap<Tags.Node, Number>() {{
//                put(Tags.Node.LAT, bound.getMaxLat());
//                put(Tags.Node.LON, bound.getMaxLon().subtract(center.getLon().subtract(bound.getMinLon())));
//            }}));
//            // South: lat = minLat, lon = maxLon - (centerLon - minLon)
//             map.put(Direction.SOUTH, new TagNode(new HashMap<Tags.Node, Number>() {{
//                 put(Tags.Node.LAT, bound.getMinLat());
//                 put(Tags.Node.LON, bound.getMaxLon().subtract(center.getLon().subtract(bound.getMinLon())));
//            }}));
//            // East: lat = maxLat - (centerLat - minLat), lon = maxLon
//             map.put(Direction.EAST, new TagNode(new HashMap<Tags.Node, Number>() {{
//                 put(Tags.Node.LAT, bound.getMaxLat().subtract(center.getLat().subtract(bound.getMinLat())));
//                 put(Tags.Node.LON, bound.getMaxLon());
//             }}));
//             // West: lat = maxLat - (centerLat - minLat), lon = minLon
//             map.put(Direction.WEST, new TagNode(new HashMap<Tags.Node, Number>() {{
//                 put(Tags.Node.LAT, bound.getMaxLat().subtract(center.getLat().subtract(bound.getMinLat())));
//                 put(Tags.Node.LON, bound.getMinLon());
//             }}));
    
//             return map;
//         }

//         public TagNode getNorth() {
//             return this.north;
//         }

//         public TagNode getSouth() {
//             return this.south;
//         }

//         public TagNode getEast() {
//             return this.east;
//         }

//         public TagNode getWest() {
//             return this.west;
//         }
        
//         Tag<Node, BigDecimal> west = new Tag<Node, BigDecimal>(new HashMap<Node, BigDecimal>() {{
//             put(Node.LAT, x2.subtract(cx.subtract(x1)));
//             put(Node.LON, y1);
//         }});

//         Tag<Node, BigDecimal> east = new Tag<Node, BigDecimal>(new HashMap<Node, BigDecimal>() {{
//             put(Node.LAT, x2.subtract(cx.subtract(x1)));
//             put(Node.LON, y2);
//         }});

//         Tag<Node, BigDecimal> north = new Tag<Node, BigDecimal>(new HashMap<Node, BigDecimal>() {{
//             put(Node.LAT, x2);
//             put(Node.LON, y2.subtract(cy.subtract(y1)));
//         }});

//         Tag<Node, BigDecimal> south = new Tag<Node, BigDecimal>(new HashMap<Node, BigDecimal>() {{
//             put(Node.LAT, x1);
//             put(Node.LON, y2.subtract(cy.subtract(y1)));
//         }});
//     }


// }
