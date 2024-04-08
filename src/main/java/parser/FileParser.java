package parser;

import java.util.HashMap;

public class FileParser {
    private Chunck chunck;

    public FileParser(TagBound bound) {
        this.chunck = new Chunck(bound);
    }

    public Chunck getChunck(){
        return this.chunck;
    }

    /**
     * Get the center point of the area from a bounds tag. The center point is calculated as the average of the min and max latitude and longitude.
     * The center point is mainly used to split the bounds into smaller areas for effeciency.
     * @param tag - The bounds tag to parse.
     * @return A Tag object containing the center point, based on the {@link Tags.Node} enum. ID is not used.
     */
    public TagNode centerPoint(TagBound tag) {
        double x1 = tag.getMinLat();
        double x2 = tag.getMaxLat();
        double y1 = tag.getMinLon(); 
        double y2 = tag.getMaxLon();
    
        // Calculate the center of the area.
        double centerX = Math.round((x1 + (x2 - x1) / 2) * 10000000d) / 10000000d;
        double centerY =  Math.round((y1 + (y2 - y1) / 2) * 10000000d) / 10000000d;
        
        TagNode center = new TagNode(centerX, centerY);
        return center;
    }


    /**
     * 
     */
    public static class CompasPoints {
        private enum Direction {
            NORTH, SOUTH, EAST, WEST
        }
        private TagNode north;
        private TagNode south;
        private TagNode east;
        private TagNode west;


        public CompasPoints(TagNode center, TagBound bound) {
            HashMap<Direction, TagNode> map = calculateCompasPoints(center, bound);
            this.north = map.get(Direction.NORTH);
            this.south = map.get(Direction.SOUTH);
            this.east = map.get(Direction.EAST);
            this.west = map.get(Direction.WEST);
        }
    
        public HashMap<Direction, TagNode> calculateCompasPoints(TagNode center, TagBound bound) {
           HashMap<Direction, TagNode> map = new HashMap<Direction, TagNode>();
    
            // North: lat = maxLat, lon = maxLon - (centerLon - minLon)
            map.put(Direction.NORTH, new TagNode(bound.getMaxLat(), center.getLon()));
            // South: lat = minLat, lon = maxLon - (centerLon - minLon)
            map.put(Direction.SOUTH, new TagNode(bound.getMinLat(), center.getLon()));
            // East: lat = maxLat - (centerLat - minLat), lon = maxLon
            map.put(Direction.EAST, new TagNode(center.getLat(), bound.getMaxLon()));
            // West: lat = maxLat - (centerLat - minLat), lon = minLon
            map.put(Direction.WEST, new TagNode(center.getLat(), bound.getMinLon()));

            return map;
        }

        public TagNode getNorth() {
            return this.north;
        }

        public TagNode getSouth() {
            return this.south;
        }

        public TagNode getEast() {
            return this.east;
        }

        public TagNode getWest() {
            return this.west;
        }
        
    }
    
    /**
     * A chunk is a smaller area of the bounds.

     */
    public class Chunck {
        private TagBound bound;
        private TagNode center;
        private HashMap<Quadrant, TagBound> chunks;

        enum Quadrant {
            Q1, Q2, Q3, Q4
        }

        public Chunck(TagBound bound) {
            this.bound = bound;
            this.center = FileParser.this.centerPoint(bound);
            this.chunks = constuctQuadrants();
        }

        public TagBound getQuadrant(int index){
            return this.chunks.get(Quadrant.values()[index]);
        }

        public TagBound getQuadrantOne(){
            return this.chunks.get(Quadrant.Q1);
        }
        public TagBound getQuadrantTwo(){
            return this.chunks.get(Quadrant.Q2);
        }
        public TagBound getQuadrantThree(){
            return this.chunks.get(Quadrant.Q3);
        }
        public TagBound getQuadrantFour(){
            return this.chunks.get(Quadrant.Q4);
        }

        /*
         * Method to constrtct four new TagBounds for each quadrant of the given bound
         * @param bound - The bound to split
         * @return An {@param HashMap<>} of four new bounds, each representing a {@link Quadrant} of the given bound.
        */
        public HashMap<Quadrant, TagBound> constuctQuadrants(){
            HashMap<Quadrant, TagBound> quadrants = new HashMap<Quadrant, TagBound>();

            CompasPoints compas = new CompasPoints(center, bound);

            // Q1/North-West
            quadrants.put(Quadrant.Q1, new TagBound(
                compas.getWest().getLat(),
                bound.getMaxLat(),
                compas.getWest().getLon(),
                center.getLon()
            ));
            // Q2/North-East
            quadrants.put(Quadrant.Q2, new TagBound(
                center.getLat(),
                compas.getNorth().getLat(),
                center.getLon(),
                bound.getMaxLon()
            ));
            // Q4/South-West
            quadrants.put(Quadrant.Q3, new TagBound(
                compas.getSouth().getLat(),
                center.getLat(),
                bound.getMinLon(),
                center.getLon()
            ));
            // Q3/South-East
            quadrants.put(Quadrant.Q4, new TagBound(
                compas.getSouth().getLat(),
                center.getLat(),
                center.getLon(),
                bound.getMaxLon()
            ));
            return quadrants;
        }
    }

}