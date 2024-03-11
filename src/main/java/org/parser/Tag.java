package org.parser;

import java.util.HashMap;
import java.math.BigDecimal;

import javax.xml.stream.XMLStreamReader;

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
                case "node": 
                    return new TagNode(reader);
              
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

    public boolean isBoundsType() {
        return this.getClass().equals(TagBound.class);
    }

    public boolean isNodeType() {
        return this.getClass().equals(TagNode.class);
    }

    public boolean isAdressType() {
        return this.getClass().equals(TagAdress.class);
    }

    private static boolean isAnAdress(XMLStreamReader event) {
        return event.getAttributeValue(null, "k").equals("addr:street");
    }

    static private String getTagName(XMLStreamReader event) {
        return event.getLocalName().intern();
    }
  
    public static BigDecimal getAttributeByBigDecimal(XMLStreamReader event, String name) {
        return new BigDecimal(event.getAttributeValue(null, name));
    }
}
