package parser;
import javax.xml.stream.*;
import java.io.*;
import java.util.*;

public class XMLWriter {
    private XMLStreamReader reader;
    private XMLStreamWriter writer;
    /**
     * Constructor for the XMLWriter class
     * @param inputFile file to read from
     * @param outputFile file to write to
     */
    public XMLWriter(String inputFile, String outputFile) {
        try {
            this.reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(inputFile));
            this.writer = XMLOutputFactory.newInstance().createXMLStreamWriter(new FileWriter(outputFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeXML();
    }
    // TODO: Rewrite so no endtag is written like: <tag></tag>... Change to <tag/>
    
    /**
     * Writes the XML file and returns the content
     */
    public void writeXML() {
        try {
            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        // Check if it's the element you want to modify
                        if ("node".equals(reader.getLocalName())) {
                            // Modify the attribute value
                            String[] attributes = {"version", "timestamp", "uid", "user", "changeset"}; // Attributes to be deleted
                            deleteAtrribute(attributes);
                        } else {
                            writer.writeStartElement(reader.getLocalName());
                            // Copy existing attributes
                            for (int i = 0; i < reader.getAttributeCount(); i++) {
                                writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                            }
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        writer.writeCharacters(reader.getText());
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        writer.writeEndElement();
                        break;
                }
            }

            // Close streams
            reader.close();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
    }}

    /**
     * Deletes the attribute from the XML file
     * @param attribute the attribute to be deleted
     * @throws XMLStreamException if the attribute is not found in the XML file
     */
    private void deleteAtrribute(String attribute) throws XMLStreamException {
        writer.writeStartElement(reader.getLocalName()); 
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            if (!attribute.equals(reader.getAttributeLocalName(i))) { // Skip the attribute to be deleted
                writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i)); // Write other attributes
            }
        }
    }

    /**
     * Deletes multiple attributes from the XML file
     * @param attribute String array containing the attributes to be deleted
     * @throws XMLStreamException if an attribute from the array is not found in the XML file
     */
    private void deleteAtrribute(String[] attribute) throws XMLStreamException {
        writer.writeStartElement(reader.getLocalName()); 
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            if(!Arrays.asList(attribute).contains(reader.getAttributeLocalName(i))) {
                writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
            }
        }
    }
}
