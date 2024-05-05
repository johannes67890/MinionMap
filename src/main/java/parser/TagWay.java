package parser;

import gnu.trove.list.linked.TLinkedList;
import structures.TagGrid;
import util.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.Serializable;
import gnu.trove.list.linked.TLinkedList;

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
    TagRelation relationParent;
    short speedLimit;
    List<TagGrid> grid = new ArrayList<>();
    boolean isOneWay;
    Type type;


    public TagWay(XMLBuilder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.speedLimit = builder.getWayBuilder().getSpeedLimit();
        this.type = builder.getType();
        this.isOneWay = builder.getWayBuilder().isOneWay();
        this.nodes = builder.getWayBuilder().getRefNodes(this);

        if (this.type != null){

            switch (this.type) {
                case RESIDENTIAL:
                    constructGrid();
                    break;
                case FOREST:
                    constructGrid();
                    break;
                case INDUSTRIAL:
                    constructGrid();
                    break;
                case FARMFIELD:
                    constructGrid();
                    break;
            
                default:
                    break;
            }
        }
    }

    public TagWay(long id, String name, TLinkedList<TagNode> nodes, short speedLimit, Type type) {
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
    public TagWay(TagRelation relation, long id, TLinkedList<TagNode> nodes, short speedLimit) {
        this.id = id;
        this.name = relation.getName();
        this.nodes = nodes;
        this.speedLimit = speedLimit;
        this.type = relation.getType();

        if (this.type != null){
            switch (this.type) {
                case BORDER:
                    break;  
                case REGION:
                    break;          
                default:
                    constructGrid();
                    break;
            }
        }
    


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

    public short getSpeedLimit(){
        return speedLimit;
    }

    public String getName(){
        return name;
    }

    public void setRelationParent(TagRelation relation){
        relationParent = relation;
    }

    public TagRelation getRelationParent(){
        return relationParent;
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

    public void setIsLine(boolean isLine){
        this.isLine = isLine;
    }

    @Override
    public String toString() {
        return "Way: " + id + " name: " + name + " type " + type + " limit " + speedLimit + " oneway: " + isOneWay;
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

    public void constructGrid(){

        float minLon = Float.MAX_VALUE;
        float maxLon = Float.MIN_VALUE;
        float minLat = Float.MAX_VALUE;
        float maxlat = Float.MIN_VALUE;

        for (TagNode tag : nodes){

            if (tag.getLon() > maxLon){
                maxLon = tag.getLon();
            }
            if (tag.getLon() < minLon){
                minLon = tag.getLon();
            }
            if (tag.getLat() > maxlat){
                maxlat = tag.getLat();
            }
            if (tag.getLat() < minLat){
                minLat = tag.getLat();
            }
        }

        int counter = 0;

        for (float i = minLon; i < maxLon; i += 200){
            for (float j = minLat; j < maxlat; j += 200){
                grid.add(new TagGrid(j, i));
            }

        }
    }

    public List<TagGrid> getGrid(){
        return grid;
    }




    /**
    * Builder for a single way.
    * <p>
    * Constructs an instance of the builder, that later can be used to construct a {@link TagWay}.
    * </p>
    */
    public static class WayBuilder implements Serializable{
        private List<TagNode> refNodes = new ArrayList<TagNode>();
        private TLinkedList<TagNode> refNodesList = new TLinkedList<TagNode>();
        private boolean isEmpty = true;
        private boolean isOneWay = false;
        private short speedLimit = 50; // Default speed limit is 1 to not break edge cases in pathfinding.

        public boolean isEmpty() {
            return isEmpty;
        }

        public void setOneWay(boolean isOneWay) {
            isEmpty = false;
            this.isOneWay = isOneWay;
        }

        public boolean isOneWay() {
            return isOneWay;
        }

        public short getSpeedLimit() {
            return speedLimit;
        }

        public void setSpeedLimit(short speedLimit) {
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
                node.setType(way.getType());
                if(node.getNext() != null || node.getPrevious() != null){
                    TagNode newNode = new TagNode(node);
                    newNode.clearLinks();
                    refNodesList.add(newNode);
                    continue;
                }else{
                    refNodesList.add(node);
                }
            }

            
            refNodesList.getFirst().setParent(way);
            refNodes.clear();

           return refNodesList;
        }
    }
}
