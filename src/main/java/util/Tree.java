package util;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import parser.TagNode;

import util.KdTree;

public class Tree {
    
    public ArrayList<TagNode> nodes;
    TagNode node;

    public Tree(ArrayList<TagNode> nodes){
        this.nodes = nodes;
        
        KdTree kdtree = new KdTree();

        for (TagNode node : nodes){
            kdtree.insert(new Point2D(node.getLon(), node.getLat()));
        }
    }
    
    public void setScreenBounds(int width, int height) {
        
        RectHV rect = new RectHV(0, 0, width, height);
    }

}
