package parser;

import java.util.HashMap;
import gnu.trove.set.hash.TCustomHashSet;
import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;

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
public class TagWay extends Tag<Way>{
    TCustomHashMap<Way, Object> way = new TCustomHashMap<Way, Object>();

    public TagWay(XMLBuilder builder) {
       way = new TCustomHashMap<Way, Object>(){
            {
                put(Way.ID, builder.getId());
                put(Way.NAME, builder.getName());
                put(Way.REFS, builder.getWayBuilder().getRefNodes());
                put(Way.SPEEDLIMIT, builder.getWayBuilder().getSpeedLimit());
                put(Way.TYPE, builder.getType());     
            }
        };
    }
    /**
     * Get the id of the way.
     * @return The id of the way.
     */
    @Override
    public long getId(){
        return Long.parseLong(this.get(Way.ID).toString());
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
        return Integer.parseInt(this.get(Way.SPEEDLIMIT).toString());
    }

    /**
     * Get the type of the way.
     * @return The {@link Type} of the way.
     */
    public Type getType() {
        return (Type) this.get(Way.TYPE);
    }
    /**
     * Get the refrerence nodes of the way.
     * @return Long[] of the reference nodes of the way.
     */
    public TLongObjectHashMap<TagNode> getRefs() {
        return (TLongObjectHashMap<TagNode>) this.get(Way.REFS);
    }

    public TagNode getNodeById(Long id){
        return getRefs().get(id);
    }

    public boolean isEmpty() {
        return getRefs().size() == 0;
    }

    public int size() {
        return getRefs().size();
    }

    /**
    * Builder for a single way.
    * <p>
    * Constructs a instance of the builder, that later can be used to construct a {@link TagWay}.
    * </p>
    */
    public static class WayBuilder {
        private TLongObjectHashMap<TagNode> refNodes;
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
        private TagNode migrateNode(Long id){
            TagNode node = XMLReader.getNodeById(id);
            // if(node != null){
            //     XMLReader.getNodeById(id).remove(node);
            // }
            return node;
        }

        public void setSpeedLimit(int speedLimit) {
            isEmpty = false;
            this.speedLimit = speedLimit;
        }

        public void addNode(Long ref) {
            if (isEmpty) {
                isEmpty = false;
            }
            refNodes.put(ref, migrateNode(ref));
        }

        public TLongObjectHashMap<TagNode> getRefNodes() {
            return refNodes;
        }
    }
}
