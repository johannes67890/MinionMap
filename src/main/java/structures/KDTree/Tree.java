package structures.KDTree;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import parser.Tag;
import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import structures.TagGrid;
import util.Type;

public class Tree implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    ArrayList<Tag> nodesInBounds;
    ArrayList<TagWay> waysInBounds;
    ArrayList<TagRelation> relationsInBounds;
    static boolean isLoaded = false;

    static K3DTree multiTree;

    /**
     * Initializes the K3DTree with all the associated Tag and their Point3Ds,
     * and resets XMLReader
     * @param tags
     */
    public static void initializeTree(){
        multiTree = new K3DTree();
        multiTree.setBound(Float.MIN_VALUE, Float.MIN_VALUE, Byte.MIN_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Byte.MAX_VALUE);
    }

    public static void isNowLoaded(){
        isLoaded = true;
    }

    public static void insertTagBoundsInTree(TagBound bound){
        multiTree.insert(new Point3D(bound.getMinLon(), bound.getMinLat(),(byte) 10), bound);
        multiTree.insert(new Point3D(bound.getMinLon(), bound.getMaxLat(),(byte) 10), bound);
        multiTree.insert(new Point3D(bound.getMaxLon(), bound.getMinLat(),(byte) 10), bound);
        multiTree.insert(new Point3D(bound.getMaxLon(), bound.getMaxLat(),(byte) 10), bound);
    }

    public static void insertTagRelationInTree(TagRelation relation){
        for (TagWay way : relation.getHandledOuter()){
            for (TagNode node : way.getRefNodes()){
                Point3D temp;
                if (relation.getType() != null){
                    temp = new Point3D(node.getLon(), node.getLat(), (byte) relation.getType().getThisHierarchy());
                }else {
                    temp = new Point3D(node.getLon(), node.getLat(), (byte) 0);
                }
                multiTree.insert(temp, relation);
                if(node.getNext() == null) break;
            }
            for (TagGrid gridPoint : way.getGrid()){

                Point3D temp;
                if (way.getType() != null){
                    temp = new Point3D(gridPoint.getLon(), gridPoint.getLat(),(byte) way.getType().getThisHierarchy());
                } else {
                    temp = new Point3D(gridPoint.getLon(), gridPoint.getLat(), (byte) 0);
                }
                multiTree.insert(temp, relation);
            }
        }
    }

    public static void insertTagWayInTree(TagWay way){
        for (TagNode node : way.getRefNodes()){
            Point3D temp;
            if (way.getType() != null){
                temp = new Point3D(node.getLon(), node.getLat(),(byte) way.getType().getThisHierarchy());
            }else{
                temp = new Point3D(node.getLon(), node.getLat(), (byte) 0);
            }
            multiTree.insert(temp, way);
            if(node.getNext() == null) break;
        }

        for (TagGrid gridPoint : way.getGrid()){

            Point3D temp;
            if (way.getType() != null){
                temp = new Point3D(gridPoint.getLon(), gridPoint.getLat(),(byte) way.getType().getThisHierarchy());
            }else{
                temp = new Point3D(gridPoint.getLon(), gridPoint.getLat(), (byte) 0);
            }
            multiTree.insert(temp, way);
        }
    }

    public static void insertTagAdressInTree(Tag tag) {
        multiTree.putMap(new Point3D(tag.getLon(), tag.getLat(), (byte) 0), tag);
    }


    /**
     * Returns the tags near a point
     * @param point Point to search near
     * @return ArrayList of Tag-objects near the point
     */
    public static ArrayList<Tag> getTagsNearPoint(Point3D point){
        return multiTree.nearestTags(point);
    }

    /**
     * Returns the tags near a {@link Tag}
     * @param point Point to search near
     * @return ArrayList of Tag-objects near the point
     */
    public static ArrayList<Tag> getTagsNearTag(Tag tag){
        return multiTree.nearestTags(new Point3D(tag.getLon(), tag.getLat(), (byte) 0));
    }

    /**
     * Returns the tags near a {@link Tag}
     * @param point Point to search near
     * @return ArrayList of Tag-objects near the point
     */
    public static ArrayList<Tag> getTagsNearTag(Tag tag, List<Type> searchType){
        return multiTree.nearestTags(new Point3D(tag.getLon(), tag.getLat(), (byte) 10), searchType);
    }

    /**
     * Returns the nearest point in the tree to a given point
     * @param point Point to search near
     * @return Point2D object nearest to the given point
     */
    public static Point3D getNearestPoint(Point3D point){
        return multiTree.nearest(point);
    }
    
    /**
     * Returns the nearest point in the tree to a given point
     * @param point Point to search near
     * @return Point2D object nearest to the given point
     */
    public static Point3D getNearestTag(Tag tag){
        return multiTree.nearest(new Point3D(tag.getLon(), tag.getLat(), (byte) 0));
    }

    public static Point3D getNearestPointOfType(Tag tag, List<Type> types){
        return multiTree.nearest(new Point3D(tag.getLon(), tag.getLat(), (byte) 0), types);
    }

    /**
     * Returns the tags in a given bounds
     * @param rect Bounds to search in
     * @return HashSet of given tag-objects in the given bounds
     */
    public static HashSet<Tag> getTagsInBounds(Rect3D rect){
        return multiTree.rangeNode(rect);
    }

    /**
     * Returns a list of tags containing objects in a given bounds
     * @param point Point to search near
     * @return ArrayList of tag-objects in the given bounds
     */
    public static ArrayList<Tag> getTagsFromPoint(Point3D point){
        return multiTree.getTagsFromPoint(point);
    }
    /**
     * Returns a list of tags containing objects in a given bounds
     * @param point Point to search near
     * @return ArrayList of tag-objects in the given bounds
     */
    public static ArrayList<Tag> getTagFromPoint(Tag node){
        return multiTree.getTagsFromPoint(new Point3D(node.getLon(), node.getLat(), (byte) 10));
    }

    /**
     * Returns an ArrayList<Tag> that is associated with the nearest Point3D in the K3DTree with a specific Type.
     * This uses the build-in function from the K3DTree that searches through the tree.
     * 
     * @param tag           The specified tag of which you need to search for the nearest Point3D
     * @param searchType    The specified List<Type> that contains all the allowed types
     * @return              An ArrayList<Tag> that is associated with the nearest Point3D in the K3DTree with a specific Type
     */
    public static ArrayList<Tag> getNearestOfType(Tag tag, List<Type> searchType){
        return multiTree.nearestTags(new Point3D(tag.getLon(), tag.getLat(), (byte) 10), searchType);
    }

    /**
     * Returns an ArrayList<Tag> that is associated with the nearest Point3D in the K3DTree with a specific Type.
     * This uses a brute-force method of comparing every Point2D in the HashMap in K3DTree and find the Point3D
     * that is closest to the parameter tag.
     * 
     * @param tag           The specified tag of which you need to search for the nearest Point3D
     * @param searchType    The specified List<Type> that contains all the allowed types
     * @return              An ArrayList<Tag> that is associated with the nearest Point3D in the K3DTree with a specific Type
     */
    public static ArrayList<Tag> getNearestOfTypeBruteForce(Tag tag, List<Type> searchType){
        return multiTree.getTagsFromPoint(multiTree.nearestBruteForce(new Point3D(tag.getLon(), tag.getLat(), (byte) 0), searchType));
    }

    /**
     * Returns an ArrayList<Tag> that is associated with the nearest Point3D in the K3DTree of a specific Class type.
     * This uses a brute-force method of comparing every Point2D in the HashMap in K3DTree and find the Point3D
     * that is closest to the parameter tag.
     * 
     * @param tag           The specified tag of which you need to search for the nearest Point3D
     * @param classType     The specified Class type, that the returned Tags should be of.
     * @return              An ArrayList<Tag> that is associated with the nearest Point3D in the K3DTree with a specific Type
     */
    public static ArrayList<Tag> getNearestOfClassBruteForce(Tag tag, Class<?> classType){
        return multiTree.getTagsFromPoint(multiTree.nearestBruteForce(new Point3D(tag.getLon(), tag.getLat(), (byte) 0), classType));
    }

    /**
     * @return returns true if the K3DTree has been initialized
     */
    public static boolean isLoaded(){
        return isLoaded;
    }

    /**
     * @return returns the K3DTree
     */
    public static K3DTree getKDTree(){
        return multiTree;
    }
}
