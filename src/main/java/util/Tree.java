package util;

import java.util.ArrayList;
import java.util.HashSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import parser.Tag;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;

public class Tree {

    KdTree kdtree;
    ArrayList<Tag<?>> nodesInBounds;
    ArrayList<TagWay> waysInBounds;
    ArrayList<TagRelation> relationsInBounds;

    public Tree(ArrayList<Tag<?>> tags){
        kdtree = new KdTree();
        kdtree.setBound(-180, -180, 180, 180);
        for (Tag<?> tag : tags){
            insertTagInTree(tag);
        }
        
    }

    public void insertTagInTree(Tag<?> tag){
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

    public ArrayList<Tag<?>> getTagsNearPoint(Point2D point){
        return kdtree.nearestTags(point);
    }

    public Point2D getNearestPoint(Point2D point){
        return kdtree.nearest(point);
    }

    public HashSet<Tag<?>> getTagsInBounds(RectHV rect) {
        HashSet<Tag<?>> tagsInBounds = kdtree.rangeNode(rect);
        return tagsInBounds;
    }

    public ArrayList<Tag<?>> getTagsFromPoint(Point2D point){
        return kdtree.getTagsFromPoint(point);
    }
}
