package org.parser;
import javax.xml.stream.*;

import java.io.*;
import java.util.*;


public class XMLWriter {
    public XMLWriter(){
        
    }

    public XMLWriter(TagBound bounds) {
        FileParser fileParser = new FileParser(bounds);
        try {
            initChunkFiles(fileParser);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    
    public void initChunkFiles(FileParser fileParser) throws XMLStreamException, IOException{
        String directoryPath = "src/main/resources/chunks/";
        
        
        for (int i = 0; i < 4; i++) {
            // Create a new file for each chunk
         
                // Create the directory if it doesn't exist
                File directory = new File(directoryPath);
                if (!directory.exists()) {
                    directory.mkdirs(); // mkdirs() creates parent directories if they don't exist
                }
                XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileWriter(directoryPath + i + ".xml"));
                
                writer.writeStartDocument();
                writer.writeStartElement("osm");
                // writer.writeAttribute("version", "0.6");

                final int index = i; // Declare a final variable to use in the lambda expression

                createXMLElement(writer, "bounds", new HashMap<String, String>() {
                    {
                        put("minlat", fileParser.getChunck().getQuadrant(index).getMinLat().toString());
                        put("minlon", fileParser.getChunck().getQuadrant(index).getMinLon().toString());
                        put("maxlat", fileParser.getChunck().getQuadrant(index).getMaxLat().toString());
                        put("maxlon", fileParser.getChunck().getQuadrant(index).getMaxLon().toString());
                    }
                });
                
                writer.writeEndDocument();
                
                // Writing the content on XML file and 
                // close xmlStreamWriter using close() method 
                writer.flush(); 
                writer.close();
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
}