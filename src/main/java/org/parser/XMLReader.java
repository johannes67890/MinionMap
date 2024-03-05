package org.parser;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import java.io.FileInputStream;
import java.util.ArrayList;

public class XMLReader {
    XMLEventReader eventReader; 
    XMLEvent event;

    public ParseTagResult bound = new ParseTagResult();
    public ArrayList<ParseTagResult> nodes = new ArrayList<ParseTagResult>();
    public ArrayList<ParseTagResult> adresses = new ArrayList<ParseTagResult>();
    
    public XMLReader(FileDistributer filename) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            this.eventReader = factory.createXMLEventReader(new FileInputStream(filename.getFilePath()));

            while (eventReader.hasNext()) {
                ParseTagResult tag = Tag.parseTag(eventReader);

                if(tag.isNode()) nodes.add(tag);
                if(tag.isAdress()) adresses.add(tag);
                if(tag.isNode()) this.bound = tag;
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


