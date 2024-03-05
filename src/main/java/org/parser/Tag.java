package org.parser;

import java.util.HashMap;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;


public class Tag<K,V> {
    private HashMap<K ,V> tag = new HashMap<K,V>();

    public Tag(){};

    public Tag(HashMap<K,V> map) {;
        this.tag = map;
    }

    static public Tag parseTag(XMLEventReader eventReader) {
        try {
            XMLEvent event = eventReader.nextEvent();

            if(!isStartTag(event)) return new Tag();

            switch (getTagName(event)) {
                case "bounds": 
                    return new TagBound(event);
                case "node": 
                    // If there is children tags in the node tag, then it is an adress tag.    
                    if(isStartTag(eventReader.nextTag(), "tag")){
                        return new TagAdress(event, eventReader);                         
                    } else {
                        return new TagNode(event);
                    }
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

    static private String getTagName(XMLEvent event) {
        return event.asStartElement().getName().getLocalPart();
    }

    /**
     * Check if the XML event is a specific START tag.
     * @param event - the XML event.
     * @param tag - the tag to check for.
     * @return boolean - true if the XML event is the tag.
     */
    static private boolean isStartTag(XMLEvent event) {
        return event.isStartElement() ? true : false; 
    }  
    
    /**
     * Check if the XML event is a specific START tag.
     * @param event - the XML event.
     * @param tag - the tag to check for.
     * @return boolean - true if the XML event is the tag.
     */
    static public boolean isEndTag(XMLEvent event) {
        return event.isEndElement() ? true : false; 
    }   

    /**
     * Check if the XML event is a specific START tag.
     * @param event - the XML event.
     * @param tag - the tag to check for.
     * @return boolean - true if the XML event is the tag.
     */
    static public boolean isStartTag(XMLEvent event, String tag) {
        if(event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(tag)) {
            return true;
        }
        return false;
    }   

    /**
     * Check if the XML event is a specific END tag.
     * @param event - the XML event.
     * @param tag - the tag to check for.
     * @return boolean - true if the XML event is the tag.
     */
    static public boolean isEndTag(XMLEvent event, String tag) {
        if(event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(tag)) {
            return true;
        }
        return false;
    }   

}
