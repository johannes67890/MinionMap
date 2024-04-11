package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.sql.rowset.spi.XmlReader;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import parser.Tag;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.XMLReader;

import util.KdTree;

public class Tree {

    KdTree kdtree;
    ArrayList<Tag<?>> nodesInBounds;
    ArrayList<TagWay> waysInBounds;
    ArrayList<TagRelation> relationsInBounds;

    public Tree(ArrayList<Tag<?>> tags){
        kdtree = new KdTree();
        kdtree.setBound(-180, -180, 180, 180);
        for (Tag<?> tag : tags){
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
        
    }

    public HashSet<Tag<?>> getTagsInBounds(RectHV rect) {
        HashSet<Tag<?>> tagsInBounds = kdtree.rangeNode(rect);
        return tagsInBounds;
    }

    public ArrayList<TagWay> getWaysInBounds(RectHV rect){

        if (!waysInBounds.isEmpty()){
            return waysInBounds;
        }

        ArrayList<TagWay> allWays = new ArrayList<>(XMLReader.getWays().values());
        ArrayList<TagNode> nodes = new ArrayList<>();// = getNodesInBounds(rect);

        for (TagNode node : nodes){
            boolean wayFound = false;
            for (TagWay way : allWays){
                if (wayFound){
                    break;
                }
                for (TagNode allNode : way.getNodes()){
                    if (allNode.getId() == node.getId()){
                        waysInBounds.add(way);
                        wayFound = true;
                        break;
                    }
                }
            }
        }

        return waysInBounds;

    }

    public ArrayList<TagRelation> getRelationsInBounds(RectHV rect){

        if (!relationsInBounds.isEmpty()){
            return relationsInBounds;
        }

        ArrayList<TagRelation> allRelations = new ArrayList<>(XMLReader.getRelations().values());
        ArrayList<TagWay> ways = getWaysInBounds(rect);

        for (TagRelation relation : allRelations){
            boolean relationFound = false;
            for (TagWay way : relation.getWays()){
                if (relationFound){
                    break;
                }
                for (TagWay wayInTree : ways){
                    if (way == wayInTree){
                        relationsInBounds.add(relation);
                        relationFound = true;
                        break;
                    }
                }
            }
        }
        return relationsInBounds;
    }
}
