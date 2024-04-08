package parser;

import java.util.ArrayList;
import java.util.HashMap;

enum Way {
    ID, REFS, NAME, TYPE
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
                put(Way.REFS, builder.getWayBuilder().getRefNodes());
                put(Way.TYPE, builder.getType());
                put(Way.NAME, builder.getName());
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
    public ArrayList<Long> getNodes() {
        return (ArrayList<Long>) this.get(Way.REFS);
    }
        
    public boolean isEmpty() {
        return getNodes().size() == 0;
    }

    public int size() {
        return getNodes().size();
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

        public boolean isEmpty() {
            return isEmpty;
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
