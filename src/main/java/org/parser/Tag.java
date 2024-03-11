package org.parser;

import java.util.HashMap;
import java.math.BigDecimal;
import javax.xml.stream.XMLStreamReader;

enum Node {
    ID, LAT, LON;
}

public class Tag<K,V> {
    private HashMap<K ,V> tag = new HashMap<K,V>();

    public Tag(){};

    public Tag(HashMap<K,V> map) {;
        this.tag = map;
    }

    static public Tag parseTag(XMLStreamReader reader) {
        try {
            switch (getTagName(reader)) {
                case "bounds": 
                    return new TagBound(reader);
                // case "node": 
                //     // If there is children tags in the node tag, then it is an adress tag.    
                //     if(isStartTag(reader.nextTag(), "tag")){
                //         return new TagAdress(event, reader);                         
                //     } else {
                //         return new TagNode(event);
                //     }
                // case "way": break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Tag(); // Return empty tag if no tag is found.
    }

    @Override
    public String toString() {
        return this.tag.toString();
    }

    /**
     * The the current tag. Returns null if the tag is empty.
     * 
     * @return The tag of type HashMap<K,V>, where the types are of {@link ParseTagResult}.
     */
    public HashMap<K, V> getTag() {
        return !this.tag.isEmpty() ? tag : null;
    }

    public Long getID() {
        return (Long) this.getTag().get(Node.ID);
    }

    public BigDecimal getLat() {
        return (BigDecimal) this.getTag().get(Node.LAT);
    }

    public BigDecimal getLon() {
        return (BigDecimal) this.getTag().get(Node.LON);
    }

    public Class<?> getType() {
        return this.tag.getClass();
    } 

    // Method to set the tag.
    public void setTag(HashMap<K,V> tag) {
        this.tag = tag;
    }

    public boolean isBounds() {
        return this.getClass().equals(TagBound.class);
    }

    public boolean isNode() {
        return this.getClass().equals(TagNode.class);
    }

    public boolean isAdress() {
        return this.getClass().equals(TagAdress.class);
    }

    public boolean isEmpty() {
        return this.getClass().equals(Tag.class);
    }

    static private String getTagName(XMLStreamReader event) {
        return event.getLocalName().intern();
    }
  
    public static BigDecimal getAttributeByBigDecimal(XMLStreamReader event, String name) {
        return new BigDecimal(event.getAttributeValue(null, name));
    }


    /**
     * InnerTag
     */

    public class TagNode extends Tag<Node, Number> {

        TagNode(XMLStreamReader event) {
            super(setNode(event));
        }
        
        static private HashMap<Node, Number> setNode(XMLStreamReader event){
            HashMap<Node, Number> node = new HashMap<>();
    
            node.put(Node.ID, getAttributeByBigDecimal(event, "id"));
            node.put(Node.LAT, getAttributeByBigDecimal(event, "lat"));
            node.put(Node.LON, getAttributeByBigDecimal(event, "lon"));
            return node;
        }
    }

}
