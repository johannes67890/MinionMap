package org.parser;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import java.io.FileInputStream;
import java.util.ArrayList;
import org.Tags;

import java.math.BigDecimal;


public class XMLReader {
    XMLEventReader eventReader; 
    XMLEvent event;

    public TagBound bound = new TagBound();
    public ArrayList<TagNode> nodes = new ArrayList<TagNode>();
    public ArrayList<TagAdress> adresses = new ArrayList<TagAdress>();
    
    public XMLReader(FileDistributer filename) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            this.eventReader = factory.createXMLEventReader(new FileInputStream(filename.getFilePath()));

            while (eventReader.hasNext()) {
                Tag tag = Tag.parseTag(eventReader);
                
                if(!tag.isEmpty()) {
                    System.out.println(tag);
                    if(tag.isBounds()) {
                        this.bound = (TagBound) tag;
                    } else if(tag.isNode()) {
                        nodes.add((TagNode) tag);
                    } else if(tag.isAdress()) {
                        adresses.add((TagAdress) tag);
                    }
                }
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        new XMLParser(this);

    }

    /**
     * Get the XML event.
     * @return - the XML event.
     */
    public XMLEvent getEvent() {
        return event;
    }

    /**
     * Get the event reader.
     * @return {@link XMLEventReader} - the event reader.
     */
    public XMLEventReader getEventReader() {
        return eventReader;
    }



}


