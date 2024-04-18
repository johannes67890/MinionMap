package util;

import java.util.ArrayList;

import parser.TagNode;

/**
 * Helper class containing various mathematical methods.
 */
public final class MathUtil {

    /**
     * 
     * Clamps a value by ensuring, that it does not go below min, and above max.
     * @param val - The value to check
     * @param min - The minimum value
     * @param max - the maximum value
     * @return the clamped value
     */
    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Reduces the amount of points in a polygon, by removing points that are closer than minDistance.
     * @param nodes - The list of points to reduce
     * @param minDistance - The minimum distance between points
     * @return a list of points with reduced amount of points
     */
    public static ArrayList<TagNode> reduce(ArrayList<TagNode> nodes, double minDistance) {
        ArrayList<TagNode> simplified = new ArrayList<TagNode>();
        int size = nodes.size();
    
        TagNode ref = nodes.get(0);
        simplified.add(ref);
        for (int i = 1; i < size; i++) {
            TagNode v0 = nodes.get(i);
            if (ref.distance(v0) > minDistance) {
                // set the new reference vertex
                ref = v0;
                // add it to the simplified polygon
                simplified.add(v0);
            }
            // if it's smaller, then we ignore the vertex
            // and continue
        }
        simplified.add(nodes.get(nodes.size() - 1));

        return simplified;
    }

   //---------------------------------------------------------------------------------------------------------------------------------------------
    public static ArrayList<TagNode> dp(ArrayList<TagNode> polyline, double max) {
        int size = polyline.size();
        TagNode first = polyline.get(0);
        TagNode last = polyline.get(size - 1);
    
        double maxDistance = 0;
        int maxVertex = 0;
        for (int i = 1; i < size - 1; i++) {
            TagNode v = polyline.get(i);
            // get the distance from v to the line created by first/last
            double d = distancePointToLine(v, first, last);
            if (d > maxDistance) {
                maxDistance = d;
                maxVertex = i;
            }
        }
    
        if (maxDistance >= max) {
            // subdivide
            ArrayList<TagNode> one = dp(sublist(0, maxVertex + 1, polyline), max);
            ArrayList<TagNode> two = dp(sublist(maxVertex, size, polyline), max);
            // rejoin the two (TODO without repeating the middle point)
            ArrayList<TagNode> simplified = new ArrayList<TagNode>();
            simplified.addAll(one);
            simplified.addAll(two);
            return simplified;
        } else {
            // return only the first/last vertices
            ArrayList<TagNode> simplified = new ArrayList<TagNode>();
            simplified.add(first);
            simplified.add(last);
            return simplified;
        }
    }

    /**
     * Returns a sublist of a list
     * @param startindex the index of the first index from the list that the sublist should contain
     * @param endIndex the index of the last index from the list that the sublist should contain
     * @param list - The list to get the sublist from
     * @return a sublist of the list
     */
    public static ArrayList<TagNode> sublist(int startindex, int endIndex, ArrayList<TagNode> list) {

        ArrayList<TagNode> subList = new ArrayList<>();

        for (int i = startindex; i < endIndex; i++){
            subList.add(list.get(i));
        }

        return subList;
    }

    /**
     * Calculates distance between a point from a line, using hardcoded mercatorprojections for Denmark.
     * @param point - Point distanced from the line
     * @param l1 - Startpoint of line
     * @param l2 - Endpoint of line
     * @return the distance between the point and the line
     */
    public static double distancePointToLine(final TagNode point, final TagNode l1, final TagNode l2){
        return Math
        .abs((l2.getLon() - l1.getLon()) * (l1.getLat() - point.getLat())
                - (l1.getLat() - point.getLat()) * (l2.getLat() - l1.getLat()))
        / Math.sqrt(Math.pow(l2.getLon() - l1.getLon(), 2) + Math.pow(l2.getLat() - l1.getLon(), 2));
    }

        

    
}
