package org.parser;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import java.io.FileInputStream;
import java.util.ArrayList;

import java.math.BigDecimal;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

public class XMLReader {
    XMLStreamReader reader; 
    XMLEvent event;

    public TagBound bound = new TagBound();
    public ArrayList<Tag.TagNode> nodes = new ArrayList<Tag.TagNode>();
    public ArrayList<TagAdress> adresses = new ArrayList<TagAdress>();
    
    public XMLReader(FileDistributer filename) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            this.reader = factory.createXMLStreamReader(new FileInputStream(filename.getFilePath()));
            while (reader.hasNext()) {
                reader.next();
                switch (reader.getEventType()) {
                    case START_ELEMENT:
                        Tag tag = Tag.parseTag(reader);
                        
                        break;
                
                    default:
                        break;
                }
                
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        new XMLParser(this);

    }




}


