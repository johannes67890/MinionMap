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
public class TagWay extends HashMap<Way, Object>{


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
/*
    public ArrayList<String> getKeys() {
        return (ArrayList<String>) this.get(Way.TAGKEYS);
    }

    public ArrayList<String> getValues() {
        return (ArrayList<String>) this.get(Way.TAGVALUES);
    }
*/
    // public Long[] getTags() {
    //     return tags;
    // }
        
    public boolean isEmpty() {
        return getNodes().size() == 0;
    }

    public int size() {
        return getNodes().size();
    }

    public boolean isLine(){
        return isLine;
    }

    

/*
    public Color tagToColor(){
        ArrayList<String> tagKs = this.getType();
        ArrayList<String> tagVs = this.getValues();

        for (int index = 0; index < tagKs.size(); index++){
            switch (tagKs.get(index)){
                case ("building"):
                    if (tagVs.get(index).equals("yes")){
                        isLine = false;
                        return Color.CORAL;
                    }else{
                        return null;
                    }
    
                case ("highway"):
                    isLine = true;
                    if (tagVs.get(index).equals("tertiary")){
                        return Color.BLACK;
                    } else if (tagVs.get(index).equals("residential")) {
                        return Color.GRAY;
                    }
                    return Color.BLACK;
                case ("natural"):
                    if (tagVs.get(index).equals("water")){
                        isLine = false;
                        return Color.LIGHTSKYBLUE;
                    }else{
                        return null;
                    }
                case ("amenity"):
                    if (tagVs.get(index).equals("parking")){
                        isLine = false;
                        return Color.GRAY;
                    }else{
                        return null;
                    }
    
                case ("leisure"):
                    isLine = false;
                    if (tagVs.get(index).equals("park")){
                        return Color.PALEGREEN;
                    }else if (tagVs.get(index).equals("pitch")){
                        return Color.PALEGREEN;
                    }else{
                        return null;
                    }
    
                case ("landuse"):
                    isLine = false;
                    if (tagVs.get(index).equals("grass")){
                        return Color.PALEGREEN;
                    }else{
                        return null;
                    }
    
                case ("railway"):
                    isLine = true;
                    if (tagVs.get(index).equals("subway")){
                        return Color.LIGHTGRAY;
                    }else{
                        return Color.DIMGREY;
                    }    
            }


        }
        System.out.println("NO COLOR FOUND FOR WAY");
        for (String tagK : tagKs){
            System.out.println(tagK);
        }
        return null;
            
    }

    */


}
