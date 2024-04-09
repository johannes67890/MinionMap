package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

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

    KdTree kdtree = new KdTree();
    ArrayList<TagNode> nodesInBounds;
    ArrayList<TagWay> waysInBounds;
    ArrayList<TagRelation> relationsInBounds;

    private HashSet<Point2D> points = new HashSet<Point2D>();

    public Tree(ArrayList<TagNode> nodes){
        this.nodes = nodes;
        
        for (TagNode node : nodes){
            this.kdtree.insert(new Point2D(node.getLon(), node.getLat()));
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

        ArrayList<TagNode> allNodes = (ArrayList<TagNode>) XMLReader.getNodes().values().stream().toList();
        Iterator<Point2D> it = kdtree.range(rect).iterator();

        while(it.hasNext()){
            Point2D point = it.next();
            for (TagNode node : allNodes){
                if (node.getLon() == point.x() && node.getLat() == point.y()){
                    nodesInBounds.add(node);
                    break;
                }
            }
        }

        return nodes;
    }

    public ArrayList<TagWay> getWaysFromTree(RectHV rect){

        if (!waysInBounds.isEmpty()){
            return waysInBounds;
        }

        ArrayList<TagWay> allWays = (ArrayList<TagWay>) XMLReader.getWays().values().stream().toList();
        ArrayList<TagNode> nodes = getNodesInBounds(rect);

        for (TagNode node : nodes){
            boolean wayFound = false;
            for (TagWay way : allWays){
                if (wayFound){
                    break;
                }
                for (Long id : way.getNodes()){
                    if (id == node.getId()){
                        waysInBounds.add(way);
                        wayFound = true;
                        break;
                    }
                }
            }
        }

        return waysInBounds;

    }

    public ArrayList<TagRelation> getRelationsFromTree(RectHV rect){

        if (!relationsInBounds.isEmpty()){
            return relationsInBounds;
        }

        ArrayList<TagRelation> allRelations = (ArrayList<TagRelation>) XMLReader.getRelations().values().stream().toList();
        ArrayList<TagWay> ways = getWaysFromTree(rect);

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
