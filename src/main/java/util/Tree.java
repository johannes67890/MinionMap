package util;

import java.util.ArrayList;
import java.util.*;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import gnu.trove.map.hash.TLongObjectHashMap;
import parser.Tag;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.Type;

public class Tree {

    static KdTree kdtree;
    ArrayList<Tag> nodesInBounds;
    ArrayList<TagWay> waysInBounds;
    ArrayList<TagRelation> relationsInBounds;
    static boolean isLoaded = false;

    public Tree(TLongObjectHashMap<? extends Tag> tags) {
        kdtree = new KdTree();
        kdtree.setBound(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        for (Tag tag : tags.valueCollection()){
            insertTagInTree(tag);
        }    
    }

    public Tree(Tag tag) {
        kdtree = new KdTree();
        kdtree.setBound(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
         insertTagInTree(tag);
    }

    public static void initialize(ArrayList<Tag> tags){
        kdtree = new KdTree();
        // TODO: Fix this line of code. get the min and max value of all nodes!
        //kdtree.setBound(1300000, -8000000, 1400000, -7000000);
        kdtree.setBound(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
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
        if (tag instanceof TagNode){
            TagNode node = (TagNode) tag;
            Point2D temp = new Point2D(node.getLon(), node.getLat());
            kdtree.insert(temp, tag);
        }else if (tag instanceof TagWay){
            TagWay way = (TagWay) tag;
            for (TagNode node : way.getNodes()){
                Point2D temp = new Point2D(node.getLon(), node.getLat());
                kdtree.insert(temp, tag);
            }
        }else if (tag instanceof TagRelation){
            TagRelation relation = (TagRelation) tag;
            for (TagWay way : relation.getWays()){
                for (TagNode node : way.getNodes()){
                    Point2D temp = new Point2D(node.getLon(), node.getLat());
                    kdtree.insert(temp, tag);
                }
            }
        }
    }
    
    /**
     * Returns the tags near a point
     * @param point Point to search near
     * @return ArrayList of Tag-objects near the point
     */
    public static ArrayList<Tag> getTagsNearPoint(Point2D point){
        return kdtree.nearestTags(point);
    }

    /**
     * Returns the tags near a {@link Tag}
     * @param point Point to search near
     * @return ArrayList of Tag-objects near the point
     */
    public static ArrayList<Tag> getTagsNearTag(Tag tag){
        return kdtree.nearestTags(new Point2D(tag.getLon(), tag.getLat()));
    }

    /**
     * Returns the tags near a {@link Tag}
     * @param point Point to search near
     * @return ArrayList of Tag-objects near the point
     */
    public static ArrayList<Tag> getTagsNearTag(Tag tag, Type searchType){
        return kdtree.nearestTags(new Point2D(tag.getLon(), tag.getLat()), searchType);
    }

    /**
     * Returns the nearest point in the tree to a given point
     * @param point Point to search near
     * @return Point2D object nearest to the given point
     */
    public static Point2D getNearestPoint(Point2D point){
        return kdtree.nearest(point);
    }
    
    /**
     * Returns the nearest point in the tree to a given point
     * @param point Point to search near
     * @return Point2D object nearest to the given point
     */
    public static Point2D getNearestTag(Tag tag){
        return kdtree.nearest(new Point2D(tag.getLon(), tag.getLat()));
    }

    /**
     * Returns the tags in a given bounds
     * @param rect Bounds to search in
     * @return HashSet of given tag-objects in the given bounds
     */
    public static HashSet<Tag> getTagsInBounds(RectHV rect) {
        HashSet<Tag> tagsInBounds = kdtree.rangeNode(rect);
        return tagsInBounds;
    }

    /**
     * Returns a list of tags containing objects in a given bounds
     * @param point Point to search near
     * @return ArrayList of tag-objects in the given bounds
     */
    public static ArrayList<Tag> getTagsFromPoint(Point2D point){
        return kdtree.getTagsFromPoint(point);
    }
    /**
     * Returns a list of tags containing objects in a given bounds
     * @param point Point to search near
     * @return ArrayList of tag-objects in the given bounds
     */
    public ArrayList<Tag> getTagsFromPoint(Tag node){
        return kdtree.getTagsFromPoint(new Point2D(node.getLon(), node.getLat()));
    }

    public static boolean isLoaded(){
        return isLoaded;
    }

    public static KdTree getKDTree(){
        return kdtree;
    }
}
