package parser;

import java.lang.Math;

import gnu.trove.list.linked.TLinkedList;
/**
 * Class for projecting and unprojecting nodes to and from the Mercator projection.
 * <p>
 * The mercator projection is a cylindrical map projection that is conformal.
 * This means that the projection preserves angles locally.
 * </p>
 * <p>
 * The mercator projection is defined by the following equations:
 * <ul>
 * <li>x = R * lon</li>
 * <li>y = R * ln(tan(PI/4 + lat/2))</li>
 * </ul>
 * </p>
 * @see <a href="https://en.wikipedia.org/wiki/Mercator_projection">Mercator projection</a>
 */
public class MecatorProjection {

    private static final double RADIUS = 6378137.0; /* in meters on the equator */
     /**
     * Projects a {@link TagBound} to the mercator projection.
     * 
     * @param bound The {@link TagBound} to project
     * @return The projected {@link TagBound}
     */
    public static TagBound project(TagBound bound){ 
        TagNode max = project(bound.getMaxLon(), bound.getMaxLat());
        TagNode min = project(bound.getMinLon(), bound.getMinLat());
        return new TagBound(
            min.getLat(),
            max.getLat(),
            min.getLon(),
            max.getLon()
        );
    }

    /**
     * Projects a {@link TagNode} to the mercator projection.
     * 
     * @param node The node to project
     * @return The projected node
     */
    public static TagNode project(TagNode node){
        return new TagNode(
            node.getId(),
            projectLat(node.getLat()),
            projectLon(node.getLon())
        );
    }

    /**
     * Projects a {@link TagNode} to the mercator projection.
     * 
     * @param id The id of the node
     * @param x The x coordinate of the node
     * @param y The y coordinate of the node
     * @return The projected node
     */
    public static TagNode project(int id, float x, float y){
        return new TagNode(
                id,
                projectLat(y),
                projectLon(x)
        );
    }

     /**
     * Projects a {@link TagNode} to the ercator projection.
     * 
     * @param x The x coordinate of the node
     * @param y The y coordinate of the node
     * @return The projected node
     */
    public static TagNode project(float x, float y){
        return new TagNode(
            0,
            projectLat(y),
            projectLon(x)
        );
    }

    /**
     * Projects a longitute to the mercator projection.
     * 
     * @param lon The longitude to project
     * @return The projected longitude
     */
    public static float projectX(float x){
        return projectLon(x);
    }

    /**
     * Projects a latitude to the mercator projection.
     * 
     * @param lat The latitude to project
     * @return The projected latitude
     */
    public static float projectY(float y){
        return projectLat(y);
     }

    /****************/
    /* Unprojection */
    /****************/

    /**
     * Unprojects a bound from the mercator projection.
     * This takes the {@link TagBound} and turns x and y into lat and lon.
     * <p>
     * The lat and lon is in degrees.
     * </p>
     * @param bound The bound to unproject
     * @return The unprojected bound
     */
    public static TagBound unproject(TagBound bound){ 
        TagNode max = unproject(bound.getMaxLon(), bound.getMaxLat());
        TagNode min = unproject(bound.getMinLon(), bound.getMinLat());
        return new TagBound(
            min.getLat(),
            max.getLat(),
            min.getLon(),
            max.getLon()
        );
    }

    /**
     * Unprojects a node from the mercator projection.
     * This takes the {@link TagNode} and turns x and y into lat and lon.
     * <p>
     * The lat and lon is in degrees.
     * </p>
     * @param node The node to unproject
     * @return The unprojected node
     */
    public static TagNode unproject(TagNode node){
        return new TagNode(
            node.getId(),
            unprojectLat(node.getLat()),
            unprojectLon(node.getLon())
        );
    }

    /**
     * Unprojects the {@link TagNode}s from a given {@link TagWay} from the mercator projection.
     * <p>
     * The lat and lon is in degrees.
     * </p>
     * @param way The way to unproject
     * @return The unprojected nodes in the way
     */
    public static TagWay unproject(TagWay way){
        return new TagWay(
            way.getId(),
            way.getName(),
            unproject(way.getRefNodes()),
            way.getSpeedLimit(),
            way.getType()
        );
    }

    /**
     * Unprojects a node from the mercator projection.
     * This takes the {@link TagNode} and turns x and y into lat and lon.
     * <p>
     * The lat and lon is in degrees.
     * </p>
     * @param x The x coordinate of the node
     * @param y The y coordinate of the node
     * @return The unprojected node
     */
    public static TagNode unproject(float x, float y){
        return new TagNode(
            0,
            unprojectLat(y),
            unprojectLon(x)
        );
    }

    // Projections
    /* These functions take their angle parameter in degrees and return a length in meters */
    public static final float projectLat(float aLat) {
        return (float) (Math.log(Math.tan(Math.PI / 4 + Math.toRadians(aLat) / 2)) * RADIUS);
    }  

    public static float projectLon(float aLong) {
        return (float) (Math.toRadians(aLong) * RADIUS);
    }

    // Unprojections
    /* These functions take their length parameter in meters and return an angle in degrees */
    public static float unprojectLat(float aY) {
        return (float) (Math.toDegrees(Math.atan(Math.exp(aY / RADIUS)) * 2 - Math.PI/2));
    }

    public static float unprojectLon(float aX) {
        return (float)  Math.toDegrees(aX / RADIUS);
    }

    /**
     * Unprojects a array of nodes from the mercator projection.
     * @param nodes The nodes to unproject
     * @return a {@TLinkedList} of the unprojected {@link TagNode}s
     */
    private static TLinkedList<TagNode> unproject(TLinkedList<TagNode> nodes) {
        TLinkedList<TagNode> unprojected = new TLinkedList<>();
        for (TagNode node : nodes) {
            unprojected.add(unproject(node));
        }
        return unprojected;
    }

    // Utility
    public static double getEarthRadius() {
        return RADIUS;
    }
}
