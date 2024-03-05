package org.parser;

import java.math.BigDecimal;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.lang.reflect.*;
import org.Tags;

import com.google.common.graph.ElementOrder.Type;
import com.google.common.math.BigDecimalMath;

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

                    Tag<Tags.Node, Number> node = new Tag<Tags.Node, Number>();
                    node.tag = setNode(event);
                    
                    // If there is children tags in the node tag, then it is an adress tag.
                    if(isStartTag(eventReader.nextTag(), "tag")){
                        
                        Tag<Tags.Adress, String> adress = new Tag<Tags.Adress, String>();
                        // Grab all the tags in the node tag. and convert them to a adress tag.
                        node.tag.forEach((key, value) -> {
                            Tags.Adress adressKey = Tags.convert(key);
                            String adressValue = value.toString();
                            adress.tag.put(adressKey, adressValue);
                        });

                        adress.tag.putAll(setAdress(eventReader));
                        return new Tag<Tags.Adress, String>(adress.tag);  
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
        return new Tag<>();
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
        return this.tag.isEmpty();
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
    private boolean isEndTag(XMLEvent event) {
        return event.isEndElement() ? true : false; 
    }   

    /**
     * Check if the XML event is a specific START tag.
     * @param event - the XML event.
     * @param tag - the tag to check for.
     * @return boolean - true if the XML event is the tag.
     */
    static private boolean isStartTag(XMLEvent event, String tag) {
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
    static private boolean isEndTag(XMLEvent event, String tag) {
        if(event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(tag)) {
            return true;
        }
        return false;
    }   

    static private HashMap<Tags.Adress , String> setAdress(XMLEventReader eventReader) throws XMLStreamException {
        HashMap<Tags.Adress , String> adress = new HashMap<Tags.Adress , String>();
        Tags.Adress key = null;
        String value = null;

        while(eventReader.hasNext())  {
            XMLEvent event = eventReader.nextEvent();

            if(isEndTag(event, "node")) return adress;
            if(isStartTag(event, "tag")) {
                key = Tags.convert(event.asStartElement().getAttributeByName(new QName("k")).getValue());
                value = event.asStartElement().getAttributes().next().getValue();
                if(key != null && value != null) adress.put(key, value);
            }
        }

        return adress;
    }

}
