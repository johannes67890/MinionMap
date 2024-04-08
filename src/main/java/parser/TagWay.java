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
public class TagWay extends Tag<Way>{
    public TagWay(XMLBuilder builder) {
        super(new HashMap<Way, Object>(){
            {
                put(Way.ID, builder.getId());
                put(Way.NAME, builder.getName());
                put(Way.REFS, builder.getWayBuilder().getRefNodes());
                if(!builder.getType().equals(Type.BUILDING)){
                put(Way.SPEEDLIMIT, builder.getWayBuilder().getSpeedLimit());
                }
                put(Way.TYPE, builder.getType());
            }
        });
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
    public ArrayList<TagNode> getRefs() {
        return (ArrayList<TagNode>) this.get(Way.REFS);
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
        private ArrayList<TagNode> refNodes = new ArrayList<TagNode>();
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
                XMLReader.getNodeById(id).remove(id, node);
            }
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
            refNodes.add(migrateNode(ref));
        }

        public ArrayList<TagNode> getRefNodes() {
            return refNodes;
        }
    }
}
