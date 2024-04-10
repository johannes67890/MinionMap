package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.sql.rowset.spi.XmlReader;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.XMLReader;

import util.KdTree;

public class Tree {
    
    public ArrayList<TagNode> nodes;
    TagNode node;

    KdTree kdtree;
    ArrayList<TagNode> nodesInBounds;
    ArrayList<TagWay> waysInBounds;
    ArrayList<TagRelation> relationsInBounds;

    private HashSet<Point2D> points = new HashSet<Point2D>();

    public Tree(ArrayList<TagNode> nodes){
        this.nodes = nodes;
        kdtree = new KdTree();
        for (TagNode node : nodes){
            Point2D temp = new Point2D(node.getLon()+180, node.getLat()+180);
            kdtree.insert(temp, node);
        }
        
    }

    
    
    public void setScreenBounds(int width, int height) {
        
        RectHV rect = new RectHV(0, 0, width, height);
        setNodesInBounds(rect);
    }

    /**
     * Sets nodes in the given bounds
     * @param rect screenbounds
     */
    private void setNodesInBounds(RectHV rect) {
        HashSet<Point2D> points = new HashSet<Point2D>();
        this.kdtree.range(rect).forEach(point -> {points.add(point);});

    }

    public ArrayList<TagNode> getNodesInBounds(RectHV rect) {
        
        nodesInBounds = new ArrayList<>();
        waysInBounds = new ArrayList<>();
        relationsInBounds = new ArrayList<>();

        nodesInBounds = kdtree.rangeNode(rect);
        return nodesInBounds;
    }

    public ArrayList<TagWay> getWaysInBounds(RectHV rect){

        if (!waysInBounds.isEmpty()){
            return waysInBounds;
        }

        ArrayList<TagWay> allWays = new ArrayList<>(XMLReader.getWays().values());
        ArrayList<TagNode> nodes = getNodesInBounds(rect);

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
