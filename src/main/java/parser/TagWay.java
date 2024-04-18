package parser;

import java.util.HashMap;
import java.util.LinkedList;

import gnu.trove.map.hash.THashMap;
import gnu.trove.strategy.HashingStrategy;

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
public class TagWay extends Tag<Way, THashMap<Way, Object>> implements Comparable<TagWay>{
    private THashMap<Way, Object> way = new THashMap<Way, Object>();

    public TagWay(XMLBuilder builder) {
        way = new THashMap<Way, Object>(){
            {
                put(Way.NAME, builder.getName());
                put(Way.REFS, builder.getWayBuilder().getRefNodes());
                put(Way.SPEEDLIMIT, builder.getWayBuilder().getSpeedLimit());
                put(Way.TYPE, builder.getType());     
            }
        };
    }

     /**
     * 
     * TagWay that is created from Relation's Outer ways.
     * 
     * @param builder
     */
    public TagWay(TagRelation relation, long id, TagNode[] nodes, int speedLimit) {
        way = new THashMap<Way, Object>(){
            {
                put(Way.ID, id);
                put(Way.NAME, relation.getName());
                put(Way.REFS, nodes);
                put(Way.SPEEDLIMIT, speedLimit);
                put(Way.TYPE, relation.getType());     
            }
        };
    }

    @Override
    public THashMap<Way, Object> getMap() {
        return this.way;
    }

    @Override
    public long getId() {
        return (long) this.way.get(Way.ID);
    }

    @Override
    public double getLat() {
        throw new UnsupportedOperationException("TagWay does not have a latitude value.");
    }
    @Override
    public double getLon() {
        throw new UnsupportedOperationException("TagWay does not have a longitude value.");
    }

    public int getSpeedLimit(){
        return (int) this.way.get(Way.SPEEDLIMIT);
    }

    /**
     * Get the type of the way.
     * @return The {@link Type} of the way.
     */
    public Type getType() {
        return (Type) this.way.get(Way.TYPE);
    }

    public void setType(Type t){
        way.put(Way.TYPE, t);     
    }
    
    /**
     * Get the refrerence nodes of the way.
     * @return Long[] of the reference nodes of the way.
     */
    public LinkedList<TagNode> getRefs() {
        return (LinkedList<TagNode>) this.way.get(Way.REFS);
    }

    public TagNode getNodeById(Long id){
        for (TagNode node : getRefs()) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return getRefs().size() == 0;
    }

    /**
     * Check if the way loops.
     * @return True if the way loops, false if not.
     */
    public boolean Looped(){
        if (getRefs().get(0) != null){
            return getRefs().get(0).equals(getRefs().get(getRefs().size()-1));
        } else{return false;}
    }

    public TagNode firsTagNode(){
        return getRefs().getFirst();
    }

    public TagNode lastTagNode(){
        return getRefs().getLast();
    }

    public int size() {
        return getRefs().size();
    }

    @Override
    public int compareTo(TagWay o) {
        return Integer.compare(this.size(), o.size());
    }

    /**
    * Builder for a single way.
    * <p>
    * Constructs a instance of the builder, that later can be used to construct a {@link TagWay}.
    * </p>
    */
    public static class WayBuilder {
        private LinkedList<TagNode> refNodes = new LinkedList<TagNode>();
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

        public void addNode(Long ref) {
            if (isEmpty) {
                isEmpty = false;
            }
            refNodes.add(XMLReader.getNodeById(ref));
        }

        public LinkedList<TagNode> getRefNodes() {
            return refNodes;
        }
    }
}