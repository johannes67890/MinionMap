package parser;

import java.util.HashMap;

import util.MecatorProjection;

enum Quadrant {
    Q1, Q2, Q3, Q4
}

public class Chunk {
    private HashMap<Quadrant, TagBound> quadrants;

    public Chunk(TagBound bound) {
        this.quadrants = constuctQuadrants(centerPoint(bound), bound);
    }

    /**
     * Get the center point of the area from a bounds tag. The center point is calculated as the average of the min and max latitude and longitude.
     * The center point is mainly used to split the bounds into smaller areas for effeciency.
     * @param tag - The bounds tag to parse.
     * @return A Tag object containing the center point, based on the {@link Tags.Node} enum. ID is not used.
     */
    public static TagNode centerPoint(TagBound tag) {
        float x1 = tag.getMinLat();
        float x2 = tag.getMaxLat();
        float y1 = tag.getMinLon(); 
        float y2 = tag.getMaxLon();

        // Calculate the center of the area.
        // x = x1 + (x2 - x1) / 2
        // y = y1 + (y2 - y1) / 2
        // Round to 7 decimal places to avoid floating point errors.
        float centerX = Math.round((x1 + (x2 - x1) / 2));
        float centerY = Math.round((y1 + (y2 - y1) / 2));
        return new TagNode(centerX, centerY);
    }

    public TagBound getQuadrantOne(){
        return quadrants.get(Quadrant.Q1);
    }

    public TagBound getQuadrantTwo(){
        return quadrants.get(Quadrant.Q2);
    }

    public TagBound getQuadrantThree(){
        return quadrants.get(Quadrant.Q3);
    }

    public TagBound getQuadrantFour(){
        return quadrants.get(Quadrant.Q4);
    }
    
    public Quadrant getBoundQuadrant(TagBound bound){
        for(Quadrant q : Quadrant.values()){
            if(quadrants.get(q).equals(bound)){
                return q;
            }
        }
        return null;
    }

    public TagBound getQuadrant(Quadrant quadrant){
        return quadrants.get(quadrant);
    }

    public TagBound getQuadrant(int index){
        if(index < 0 || index > 3)
            throw new IllegalArgumentException("Index must be between 0 and 3");

        return quadrants.get(Quadrant.values()[index]);
    }

    public static HashMap<Quadrant, TagBound> getQuadrants(TagBound bound){
        return constuctQuadrants(centerPoint(bound), bound);
    }

        /**
         * Construct the four quadrants of the area from the center point and bounds.
         * The quadrants are calculated as follows:
         * <p>
         * Q1/North-West: minLat = centerLat, maxLat = maxLat, minLon = minLon, maxLon = centerLon
         * Q2/North-East: minLat = centerLat, maxLat = maxLat, minLon = centerLon, maxLon = maxLon
         * Q3/South-West: minLat = minLat, maxLat = centerLat, minLon = minLon, maxLon = centerLon
         * Q4/South-East: minLat = minLat, maxLat = centerLat, minLon = centerLon, maxLon = maxLon
         * </p>
         * @return A {@link HashMap} of the four quadrants, with the {@link Quadrant} as key and {@link TagBound} as value.
         */
        public static HashMap<Quadrant, TagBound> constuctQuadrants(TagNode center, TagBound bound){
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

    /**
     * Class to calculate the four cardinal points of a given center point and bound.
     * The cardinal points are calculated as follows:
     * <p>
     * North: lat = maxLat, lon = centerLon
     * South: lat = minLat, lon = centerLon
     * East: lat = centerLat, lon = maxLon
     * West: lat = centerLat, lon = minLon
     * </p>
     * The cardinal points are mainly used to split the bounds into smaller areas for effeciency.
     * @param center - The center point of the area.
     * @param bound - The bounds of the area.
     * @return A {@link HashMap} of the four cardinal points, with the {@link Direction} as key.
     * @see {@link ChunkFiles}
     * @see {@link ChunkFiles#constuctQuadrants()}
     * @see {@link ChunkFiles#getQuadrant(int)}
     * @see {@link ChunkFiles#centerPoint(TagBound)}
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
}