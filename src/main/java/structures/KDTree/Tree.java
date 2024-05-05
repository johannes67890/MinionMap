package structures.KDTree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import parser.Tag;
import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import util.Type;
import parser.TagGrid;

public class Tree implements Serializable{

    ArrayList<Tag> nodesInBounds;
    ArrayList<TagWay> waysInBounds;
    ArrayList<TagRelation> relationsInBounds;
    static boolean isLoaded = false;

    static K3DTree multiTree;

    public static void initialize(ArrayList<Tag> tags){
        multiTree = new K3DTree();
        multiTree.setBound(Float.MIN_VALUE, Float.MIN_VALUE, Byte.MIN_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, Byte.MAX_VALUE);
        for (Tag tag : tags){
            insertTagInTree(tag);
        }
        isLoaded = true;
    }

    /**
     * Inserts a tag in the tree
     * @param tag Tag to be inserted in the tree
     */
    public static void insertTagInTree(Tag tag){
        if (tag instanceof TagBound){
            TagBound bound = (TagBound) tag;
            
            multiTree.insert(new Point3D(bound.getMinLon(), bound.getMinLat(),(byte) 10), tag);
            multiTree.insert(new Point3D(bound.getMinLon(), bound.getMaxLat(),(byte) 10), tag);
            multiTree.insert(new Point3D(bound.getMaxLon(), bound.getMinLat(),(byte) 10), tag);
            multiTree.insert(new Point3D(bound.getMaxLon(), bound.getMaxLat(),(byte) 10), tag);
        }else if (tag instanceof TagWay){
            TagWay way = (TagWay) tag;
            for (TagNode node : way.getRefNodes()){
                Point3D temp;
                if (way.getType() != null){
                    temp = new Point3D(node.getLon(), node.getLat(),(byte) way.getType().getThisHierarchy());
                }else{
                    temp = new Point3D(node.getLon(), node.getLat(), (byte) 0);
                }
                multiTree.insert(temp, tag);
                if(node.getNext() == null) break;
            }
            for (TagGrid gridPoint : way.getGrid()){

                Point3D temp;
                if (way.getType() != null){
                    temp = new Point3D(gridPoint.getLon(), gridPoint.getLat(),(byte) way.getType().getThisHierarchy());
                }else{
                    temp = new Point3D(gridPoint.getLon(), gridPoint.getLat(), (byte) 0);
                }
                multiTree.insert(temp, tag);
            }
           
        }else if (tag instanceof TagRelation){
            TagRelation relation = (TagRelation) tag;

            for (TagWay way : relation.getHandledOuter()){
                for (TagNode node : way.getRefNodes()){
                    Point3D temp;
                    if (relation.getType() != null){
                        temp = new Point3D(node.getLon(), node.getLat(), (byte) relation.getType().getThisHierarchy());
                    }else{
                        temp = new Point3D(node.getLon(), node.getLat(), (byte) 0);
                    }
                    multiTree.insert(temp, tag);
                    if(node.getNext() == null) break;
                }
                for (TagGrid gridPoint : way.getGrid()){

                    Point3D temp;
                    if (way.getType() != null){
                        temp = new Point3D(gridPoint.getLon(), gridPoint.getLat(),(byte) way.getType().getThisHierarchy());
                    }else{
                        temp = new Point3D(gridPoint.getLon(), gridPoint.getLat(), (byte) 0);
                    }
                    multiTree.insert(temp, tag);
                }
            }
        }
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

    // TODO:
    /**
     * Returns the tags near a {@link Tag}
     * @param point Point to search near
     * @return ArrayList of Tag-objects near the point
     */
    // public static ArrayList<Tag> getTagsNearTag(Tag tag, List<Type> searchType){
    //     return kdtree.nearestTags(new Point2D(tag.getLon(), tag.getLat()), searchType);
    // }

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

    public static ArrayList<Tag> getNearestOfType(Tag tag, List<Type> searchType){
        return multiTree.nearestTags(new Point3D(tag.getLon(), tag.getLat(), (byte) 10), searchType);
    }

    public static ArrayList<Tag> getNearestOfTypeBruteForce(Tag tag, List<Type> searchType){
        return multiTree.getTagsFromPoint(multiTree.nearestBruteForce(new Point3D(tag.getLon(), tag.getLat(), (byte) 0), searchType));
    }

    public static boolean isLoaded(){
        return isLoaded;
    }

    public static K3DTree getKDTree(){
        return multiTree;
    }
}
