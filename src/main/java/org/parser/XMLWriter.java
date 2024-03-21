package org.parser;
import javax.xml.stream.*;

import org.parser.FileParser.Chunck;

import java.io.*;
import java.util.*;


public class XMLWriter {
    public XMLWriter(){

    }

    public XMLWriter(TagBound bounds) {
        FileParser fileParser = new FileParser(bounds);
        initChunkFiles(fileParser);
    }

    public XMLWriter(TagAddress inputFile, String outputFile) {


        // try {
        //     this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileWriter(outputFile));
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // writeXML();
    }


    
    // TODO: Rewrite so no endtag is written like: <tag></tag>... Change to <tag/>
    // Writes the XML file and returns the content
    // public void writeXML() {
    //     try {
    //         while (reader.hasNext()) {
    //             int event = reader.next();

    //             switch (event) {
    //                 case XMLStreamConstants.START_ELEMENT:
    //                     // Check if it's the element you want to modify
    //                     if ("node".equals(reader.getLocalName())) {
    //                         // Modify the attribute value
    //                         String[] attributes = {"version", "timestamp", "uid", "user", "changeset"}; // Attributes to be deleted
    //                         deleteAtrribute(attributes);
    //                     } else {
    //                         writer.writeStartElement(reader.getLocalName());
    //                         // Copy existing attributes
    //                         for (int i = 0; i < reader.getAttributeCount(); i++) {
    //                             writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
    //                         }
    //                     }
    //                     break;
    //                 case XMLStreamConstants.CHARACTERS:
    //                     writer.writeCharacters(reader.getText());
    //                     break;
    //                 case XMLStreamConstants.END_ELEMENT:
    //                     writer.writeEndElement();
    //                     break;
    //             }
    //         }

    //         // Close streams
    //         reader.close();
    //         writer.close();

    //     } catch (Exception e) {
    //         e.printStackTrace();
    // }}


    public void initChunkFiles(FileParser fileParser){
        for (int i = 0; i < 4; i++) {
            // Create a new file for each chunk
            try {
                XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileWriter("chunk" + i + ".xml"));
                writer.writeStartDocument();
                writer.writeStartElement("osm");
                // writer.writeAttribute("version", "0.6");
                
                    createXMLElement(writer, "bounds", new HashMap<String, String>(){
                        {
                            put("minlat", fileParser.getChunck().getQuadrant(i).getMinLat().toString());
                            put("minlon", fileParser.getChunck().getQuadrant(i).getMinLon().toString());
                            put("maxlat", fileParser.getChunck().getQuadrant(i).getMaxLat().toString());
                            put("maxlon", fileParser.getChunck().getQuadrant(i).getMaxLon().toString());
                        }
                    });
                
                writer.writeEndDocument(); 
                
                // Writing the content on XML file and 
                // close xmlStreamWriter using close() method 
                writer.flush(); 
                writer.close();
                
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
    }

    private void createXMLElement(XMLStreamWriter wrinter, String element, HashMap<String, String> attributes) throws XMLStreamException{
        wrinter.writeStartElement(element);
        // Iterate through the attributes and write them to the XML file
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            wrinter.writeAttribute(entry.getKey(), entry.getValue());
        }
        wrinter.writeEndElement();
    }

    // private void deleteAtrribute(String attribute) throws XMLStreamException {
    //     writer.writeStartElement(reader.getLocalName()); 
    //     for (int i = 0; i < reader.getAttributeCount(); i++) {
    //         if (!attribute.equals(reader.getAttributeLocalName(i))) { // Skip the attribute to be deleted
    //             writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i)); // Write other attributes
    //         }
    //     }
    // }

    // private void deleteAtrribute(String[] attribute) throws XMLStreamException {
    //     writer.writeStartElement(reader.getLocalName()); 
    //     for (int i = 0; i < reader.getAttributeCount(); i++) {
    //         if(!Arrays.asList(attribute).contains(reader.getAttributeLocalName(i))) {
    //             writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
    //         }
    //     }
    // }
}
