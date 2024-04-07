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
    public TagWay(XMLReader.Builder builder) {
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


}
