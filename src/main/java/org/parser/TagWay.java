package org.parser;

import java.util.HashMap;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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
public class TagWay extends HashMap<Way, Object>{
    public TagWay(XMLReader.Builder builder) {
        super(new HashMap<Way, Object>(){
            {
                put(Way.ID, builder.getID());
                put(Way.REFS, builder.getWayBuilder().getRefNodes());
                put(Way.TYPE, builder.getType());
                put(Way.NAME, builder.getName());
            }
        });
    }

    public void createXMLElement(XMLStreamWriter wrinter) throws XMLStreamException{
        wrinter.writeStartElement("bounds");
     
        for (Way key : this.keySet()) {
            switch (key) {
                case ID:
                    wrinter.writeAttribute("id", this.get(Way.ID).toString());
                    break;
                case REFS:
                    Long[] refs = (Long[]) this.get(Way.REFS);
                    for (Long ref : refs) {
                        wrinter.writeStartElement("nd");
                        wrinter.writeAttribute("ref", ref.toString());
                        wrinter.writeEndElement();
                    }
                    break;
                case NAME:
                    wrinter.writeAttribute("name", this.get(Way.NAME).toString());
                    break;
                case TYPE:
                    wrinter.writeAttribute("type", this.get(Way.TYPE).toString());
                    break;
                default:
                    break;
            }
        }
        wrinter.writeEndElement();
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
    public Long[] getNodes() {
        return (Long[]) this.get(Way.REFS);
    }

    // public Long[] getTags() {
    //     return tags;
    // }
        
    public boolean isEmpty() {
        return getNodes().length == 0;
    }

    public int size() {
        return getNodes().length;
    }


}
