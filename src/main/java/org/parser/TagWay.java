package org.parser;

import java.util.HashMap;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import java.io.StringWriter;
import java.util.ArrayList;

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

    public void createXMLElement(XMLStreamWriter writer) throws XMLStreamException {

        writer.writeStartElement("way");
        writer.writeAttribute("id", this.getId().toString());
        if(this.get(Way.NAME) != null){
            writer.writeAttribute("name", this.get(Way.NAME).toString());
        }
        if(this.get(Way.TYPE) != null){
            writer.writeAttribute("type", this.get(Way.TYPE).toString());
        }
        ArrayList<TagNode> refs = this.getRefs();
        for (TagNode ref : refs) {
            writer.writeCharacters("\n"); // Add a newline character
            writer.writeCharacters("\t");
            ref.createXMLRefElement(writer);
        }
        writer.writeCharacters("\n");
        writer.writeEndElement();

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
    public ArrayList<TagNode> getRefs() {
        return (ArrayList<TagNode>) this.get(Way.REFS);
    }
        
    public boolean isEmpty() {
        return getRefs().size() == 0;
    }

    public int size() {
        return getRefs().size();
    }


}
