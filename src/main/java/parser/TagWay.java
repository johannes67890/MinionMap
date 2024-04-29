package parser;

import edu.princeton.cs.algs4.Stack;
import gnu.trove.list.linked.TLinkedList;
import javafx.print.Collation;

import java.util.*;

enum Way {
    ID, REFS, NAME, TYPE, SPEEDLIMIT
}

/**
 * Class for storing a {@link HashMap} of a single way.
 * Contains the following tags:
 * <p>
 * {@link Way#ID}, {@link Way#REFS}, {@link Way#NAME}, {@link Way#TYPE}
 * </p>
 */
public class TagWay extends Tag implements Comparable<TagWay>{


    boolean isLine = false;

    long id;
    String name;
    TLinkedList<TagNode> nodes = new TLinkedList<TagNode>();
    int speedLimit;
    boolean isOneWay;
    Type type;


    public TagWay(XMLBuilder builder) {
        TagWay x = XMLReader.getWayById(27806594l);
        this.id = builder.getId();
        this.name = builder.getName();
        this.speedLimit = builder.getWayBuilder().getSpeedLimit();
        this.type = builder.getType();
        this.nodes = builder.getWayBuilder().getRefNodes(this);
    }

    public TagWay(long id, String name, TLinkedList<TagNode> nodes, int speedLimit, Type type) {
        this.id = id;
        this.name = name;
        this.nodes = nodes;
        this.speedLimit = speedLimit;
        this.type = type;
    }

    /**
     * 
     * TagWay that is created from Relation's Outer ways.
     * 
     * @param builder
     */
    public TagWay(TagRelation relation, long id, TLinkedList<TagNode> nodes, int speedLimit) {
        this.id = id;
        this.name = relation.getName();
        this.nodes = nodes;
        this.speedLimit = speedLimit;
        this.type = relation.getType();
    }

    /**
     * Get the id of the way.
     * @return The id of the way.
     */

     public long getId(){
        return id;
    }
    public float getLat() {
        throw new UnsupportedOperationException("TagWay does not have a latitude value.");
    }


    public float getLon() {
        throw new UnsupportedOperationException("TagWay does not have a longitude value.");
    }

    public int getSpeedLimit(){
        return speedLimit;
    }

    public String getName(){
        return name;
    }

    /**
     * Get the type of the way.
     * @return The {@link Type} of the way.
     */
    public Type getType() {
        return type;
    }
    public void setType(Type t){
        type = t;     
    }
    public boolean isOneWay(){
        return isOneWay;
    }

    public boolean loops(){
        if(getRefNodes().getFirst().getId() == getRefNodes().getLast().getId()){
            return true;
        } else{
            return false;
        }
    }
    
    /**
     * Get the refrerence nodes of the way.
     * @return Long[] of the reference nodes of the way.
     */
    public TLinkedList<TagNode> getRefNodes() {
        return nodes;
    }

    

    public boolean isLine(){
        return isLine;
    }

    @Override
    public String toString() {
        return "Way: " + id + " " + name + " " + type + " " + speedLimit;
    }

    public int compareTo(TagWay tW){

        int tWLayer = tW.getType().getLayer();
        int thisLayer = this.getType().getLayer();

        if (thisLayer == tWLayer){
            return 0;
        } else if (thisLayer > tWLayer){
            return 1;
        } else{
            return -1;
        }
    }



    /**
    * Builder for a single way.
    * <p>
    * Constructs an instance of the builder, that later can be used to construct a {@link TagWay}.
    * </p>
    */
    public static class WayBuilder {
        private List<TagNode> refNodes = new ArrayList<TagNode>();
        private TLinkedList<TagNode> refNodesList = new TLinkedList<TagNode>();
        private boolean isEmpty = true;
        private int speedLimit;

        public boolean isEmpty() {
            return isEmpty;
        }

        public int getSpeedLimit() {
            return speedLimit;
        }

        public void setSpeedLimit(int speedLimit) {
            isEmpty = false;
            this.speedLimit = speedLimit;
        }

        public void addNode(TagNode node) {
            if (isEmpty) {
                isEmpty = false;
            }
            refNodes.add(node);
        }

        public TLinkedList<TagNode> getRefNodes(TagWay way) {
            for (TagNode node : refNodes) {
                TagNode newNode = new TagNode(node);
                newNode.clearLinks();
                refNodesList.add(newNode);
            }

            
            refNodesList.getFirst().setParent(way);
            refNodes.clear();

           return refNodesList;
        }
    }
}
