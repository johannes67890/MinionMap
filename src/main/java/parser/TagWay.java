package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
public class TagWay extends Tag<Way> implements Comparable<TagWay>{


    boolean isLine = false;

    long id;
    String name;
    TagNode[] nodes;
    int speedLimit;
    Type type;



    public TagWay(XMLBuilder builder) {
        super(new HashMap<Way, Object>(){
            {
                //put(Way.ID, builder.getId());
                //put(Way.NAME, builder.getName());
                //put(Way.REFS, builder.getWayBuilder().getRefNodesList());
                //put(Way.SPEEDLIMIT, builder.getWayBuilder().getSpeedLimit());
                //put(Way.TYPE, builder.getType());     
            }
        });

        this.id = builder.getId();
        this.name = builder.getName();
        this.nodes = builder.getWayBuilder().getRefNodesList();
        this.speedLimit = builder.getWayBuilder().getSpeedLimit();
        this.type = builder.getType();


    }

    /**
     * 
     * TagWay that is created from Relation's Outer ways.
     * 
     * @param builder
     */
    public TagWay(TagRelation relation, long id, TagNode[] nodes, int speedLimit) {
        super(new HashMap<Way, Object>(){
            {
                //put(Way.ID, id);
                //put(Way.NAME, relation.getName());
                //put(Way.REFS, nodes);
                //put(Way.SPEEDLIMIT, speedLimit);
                //put(Way.TYPE, relation.getType());     
            }
        });

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

    public boolean loops(){
        if (getNodes()[0] != null){

            return getNodes()[0].getId() == getNodes()[size() - 1].getId();
        } else{
            return false;}
    }

    public TagNode firsTagNode(){
        return getNodes()[0];
    }

    public TagNode lastTagNode(){
        return getNodes()[size() - 1];
    }
    
    /**
     * Get the refrerence nodes of the way.
     * @return Long[] of the reference nodes of the way.
     */
    public TagNode[] getNodes() {
        return nodes;
    }

    public boolean isEmpty() {
        return getNodes().length == 0;
    }

    public int size() {
        return getNodes().length;
    }

    public boolean isLine(){
        return isLine;
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
        private ArrayList<TagNode> refNodesList = new ArrayList<TagNode>();
        private TagNode[] refNodes;
        private boolean isEmpty = true;
        private int speedLimit;

        public boolean isEmpty() {
            return isEmpty;
        }

        public int getSpeedLimit() {
            return speedLimit;
        }

        /**
         * Returns and removes a node from XMLReader node List.
         * @param id - The id of the node to migrate.
         * @return The node from the id.
         */
        public TagNode migrateNode(Long id){
            TagNode node = XMLReader.getNodeById(id);
            if(node != null){
               //  XMLReader.getNodeById(id).remove(id, node);
            }
            return node;
        }

        public void setSpeedLimit(int speedLimit) {
            isEmpty = false;
            this.speedLimit = speedLimit;
        }

        public void addNode(long ref) {
            if (isEmpty) {
                isEmpty = false;
            }
            refNodesList.add(migrateNode(ref));
        }

        public void addNode(TagNode node) {
            if (isEmpty) {
                isEmpty = false;
            }
            refNodesList.add(node);
        }

        public void closeNodeList(){

            refNodes = refNodesList.toArray(new TagNode[refNodesList.size()]);
            refNodesList.clear();
        }

        public TagNode[] getRefNodesList() {

            closeNodeList();

            return getRefNodes();
        }

        public TagNode[] getRefNodes(){

            return refNodes;

        }


    }
}
