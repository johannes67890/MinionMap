package util;

import java.util.ArrayList;
import java.util.HashSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import parser.TagNode;

import util.KdTree;

public class Tree {
    
    public ArrayList<TagNode> nodes;
    TagNode node;

    KdTree kdtree = new KdTree();

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

    /**
     * @return HashSet<Point2D> of nodes in the given bounds
     */
    public HashSet<Point2D> getNodesInBounds() {
        return this.points;
    }



}
