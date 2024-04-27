package parser;

import java.util.HashMap;
import java.util.List;

import util.MecatorProjection;
import java.io.Serializable;
/**
 * Abstract class for a tag.
 * <p>
 * A tag is a node, way or relation in the OSM XML file.
 * </p>
 * <p>
 * The Tag class is a subclass of {@link HashMap} with the key as an {@link Enum} that is from one of the five possible OSM XML Tags- and the value is stored as an {@link Object}.
 * Not all tags have the same keys, so the Enum is used to define the keys for the different tags.
 * </p>
 * <p>
 * The possible tags are:
 * <ul>
 *  <li>{@link TagNode} - A node in the OSM XML file.</li>
 *  <li>{@link TagWay} - A way in the OSM XML file.</li>
 *  <li>{@link TagAddress} - An address in the OSM XML file.</li>
 *  <li>{@link TagRelation} - A relation in the OSM XML file.</li>
 *  <li>{@link TagBound} - A bound in the OSM XML file.</li>
 * </ul>
 * <p>
 * The Tag class has the following abstract methods:
 * <ul>
 *    <li>{@link #getId()} - Get the id of the tag.</li>
 *   <li>{@link #getLat()} - Get the latitude of the tag.</li>
 *  <li>{@link #getLon()} - Get the longitude of the tag.</li>
 * </ul>
 * **Note** that not all tags, like {@link TagRelation} and {@link TagWay} uses {@link #getLat()} and {@link #getLon()} methods.
 * </p>
 */
public abstract class Tag implements Serializable{ 
    public Tag() {}

    /**
     * Get the id of the tag.
     * @return The id of the tag.
     */
    public abstract long getId();
    /**
     * Get the latitude of the tag.
     * @throws UnsupportedOperationException if the tag does not have a latitude value.
     * @return The latitude of the tag.
     */
    public abstract float getLat();
    /**
     * Get the longitude of the tag.
     * @throws UnsupportedOperationException if the tag does not have a longitude value.
     * @return The longitude of the tag.
     */
    public abstract float getLon();

    public abstract Type getType();

    /**
     * Check if a tag is within a specified {@link TagBound}.
     * <p>
     * if the tag is a {@link TagWay} or {@link TagRelation} it will check if any of the nodes or members are within the {@link TagBound}. If yes return true.
     * </p>
     * @param bound - The {@link TagBound} to check if the tag is within.
     * @return True if the tag is within the {@link TagBound}, false otherwise.
     */
    public boolean isInBounds(TagBound bound) {        
        if(this instanceof TagWay) {
            for (TagNode w  :((TagWay)this).getNodes()) {
                if(w.isInBounds(bound)) return true;
            }
            return false; // if none of the nodes are in bounds
        }
        
        // TODO: Implement this for TagRelation
        // if(this instanceof TagRelation) {
        //     for (TagWay r : ((TagRelation)this).getMembers()) {
        //         if(r.isInBounds(bound)) return true;
        //     }
        //     return false; // if none of the members are in bounds
        // }
        
        // float lat = MecatorProjection.unprojectLat(this.getLat());
        // float lon = MecatorProjection.unprojectLon(this.getLon());
        
        
        return Float.valueOf(this.getLat()).compareTo(bound.getMinLat()) == 1 && Float.valueOf(this.getLat()).compareTo(bound.getMaxLat()) == -1
            && Float.valueOf(this.getLon()).compareTo(bound.getMinLon()) == 1 && Float.valueOf(this.getLon()).compareTo(bound.getMaxLon()) == -1;
    }

    

    /**
     * Calculate the distance between two tags.
     * <p>
     * The distance is calculated using the Haversine formula.
     * </p>
     * @param a - The tag to calculate the distance to.
     * @return The distance between the two tags.
     */
    public double distance(Tag a){
        double lat1Rad = Math.toRadians(this.getLat());
        double lat2Rad = Math.toRadians(a.getLat());
        double lon1Rad = Math.toRadians(this.getLon());
        double lon2Rad = Math.toRadians(a.getLon());

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        double distance = Math.sqrt(x * x + y * y) * MecatorProjection.getEarthRadius();

        return distance;
    }

    public TagNode shortestDistanceAndIntersection(TagWay way) {
        TagNode[] nodes = way.getNodes();
        TagNode closest1 = null, closest2 = null;
        float minDist1 = Float.MAX_VALUE, minDist2 = Float.MAX_VALUE;
    
        for (TagNode node : nodes) {
            float dist = (float) Math.sqrt(Math.pow(node.getLat() - this.getLat(), 2) + Math.pow(node.getLon() - this.getLon(), 2));
            if (dist < minDist1) {
                minDist2 = minDist1;
                closest2 = closest1;
                minDist1 = dist;
                closest1 = node;
            } else if (dist < minDist2) {
                minDist2 = dist;
                closest2 = node;
            }
        }
    
        // Now closest1 and closest2 are the two closest points
        // You can now use these points to find the intersection point and shortest distance as before
    
        // Line equation coefficients for line formed by closest1 and closest2
        float A1 = closest2.getLat() - closest1.getLat();
        float B1 = closest1.getLon() - closest2.getLon();
        float C1 = A1 * closest1.getLon() + B1 * closest1.getLat();
    
        // Line equation coefficients for line perpendicular to above line and passing through the point
        float A2 = -B1;
        float B2 = A1;
        float C2 = A2 * this.getLon() + B2 * this.getLat();
    
        // Intersection point of the two lines
        float det = A1 * B2 - A2 * B1;
        float x = (B2 * C1 - B1 * C2) / det;
        float y = (A1 * C2 - A2 * C1) / det;
    
        // Create new TagNode at intersection point
        TagNode intersection = new TagNode(x, y);
    
        // Calculate shortest distance
        float shortestDistance = Math.abs(A1 * this.getLon() + B1 * this.getLat() + C1) / (float) Math.sqrt(A1 * A1 + B1 * B1);
    
        System.out.println("Shortest distance: " + shortestDistance);
        return intersection;
    }


}