package parser;

import java.util.ArrayList;
import java.util.HashMap;

enum Way {
    ID, REFS, NAME, TYPE, TAGKEYS, TAGVALUES
}

/**
 * Class for storing a {@link HashMap} of a single way.
 * Contains the following tags:
 * <p>
 * {@link Way#ID}, {@link Way#REFS}, {@link Way#NAME}, {@link Way#TYPE}
 * </p>
 */
public class TagWay extends HashMap<Way, Object> implements Comparable<TagWay>{


    boolean isLine = false;

    public TagWay(XMLReader.Builder builder) {
        super(new HashMap<Way, Object>(){
            {
                put(Way.ID, builder.getID());
                put(Way.REFS, builder.getWayBuilder().getRefNodes());
                put(Way.TYPE, builder.getType());
                put(Way.NAME, builder.getName());
                put(Way.TAGKEYS, builder.getWayBuilder().getTagKeys());
                put(Way.TAGVALUES, builder.getWayBuilder().getTagValues());
                
            }
        });
    }
    /**
     * Get the id of the way.
     * @return The id of the way.
     */
    public Long getId() {
        return (Long) this.get(Way.ID);
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

    



}
