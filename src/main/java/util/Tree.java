package util;

import java.util.ArrayList;
import java.util.HashSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import parser.Tag;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import util.Point3D;
import util.Rect3D;
import util.K3DTree;

public class Tree {

    ArrayList<Tag> nodesInBounds;
    ArrayList<TagWay> waysInBounds;
    ArrayList<TagRelation> relationsInBounds;
    static boolean isLoaded = false;

    static K3DTree multiTree;

    public static void initialize(ArrayList<Tag> tags){
        multiTree = new K3DTree();
        multiTree.setBound(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
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
        if (tag instanceof TagWay){
            TagWay way = (TagWay) tag;
            for (TagNode node : way.getNodes()){
                Point3D temp;
                if (way.getType() != null){
                    temp = new Point3D(node.getLon(), node.getLat(),(byte) way.getType().getThisHierarchy());
                }else{
                    temp = new Point3D(node.getLon(), node.getLat(), (byte) 0);
                }
                multiTree.insert(temp, tag);
            }
        }else if (tag instanceof TagRelation){
            TagRelation relation = (TagRelation) tag;

            for (TagWay way : relation.getHandledOuter()){
                for (TagNode node : way.getNodes()){
                    Point3D temp;
                    if (way.getType() != null){
                        temp = new Point3D(node.getLon(), node.getLat(), (byte) relation.getType().getThisHierarchy());
                    }else{
                        temp = new Point3D(node.getLon(), node.getLat(), (byte) relation.getType().getThisHierarchy());
                    }
                    multiTree.insert(temp, tag);
                }
            }
        }else if (tag instanceof TagRelation){
            TagNode node = (TagNode) tag;
            multiTree.insert(new Point3D(node.getLon(), node.getLat(),(byte) 0), tag);
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
     * Returns the nearest point in the tree to a given point
     * @param point Point to search near
     * @return Point2D object nearest to the given point
     */
    public static Point3D getNearestPoint(Point3D point){
        return multiTree.nearest(point);
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

    public static boolean isLoaded(){
        return isLoaded;
    }
}
