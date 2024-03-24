package org.parser;
import javax.xml.stream.*;

import java.io.*;
import java.util.*;


class XMLWriter {
    private ChunkFiles chunkFiles = new ChunkFiles();
    private String directoryPath = "src/main/resources/chunks/";
    private int chunkId = 0;
    private List<XMLStreamWriter> writers = new ArrayList<XMLStreamWriter>();
    
    public XMLWriter(){}

    public XMLWriter(TagBound bounds) {
        FileParser fileParser = new FileParser(bounds);
        try {
            initChunkFiles(fileParser);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        System.out.println(chunkFiles.toString());
    }

    public void initChunkFiles(FileParser fileParser) throws XMLStreamException{   
        String localChunkPath = directoryPath + this.chunkId + ".xml";
        
        for (int i = 0; i < 4; i++) {
            // Create a new file for each chunk
            final int index = i; // Declare a final variable to use in the lambda expression

            // Create the directory if it doesn't exist
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // mkdirs() creates parent directories if they don't exist
            }
            XMLStreamWriter writer = writers.get(index);
            writer = XMLOutputFactory.newInstance().createXMLStreamWriter(createChunkFile());
            writer.writeStartDocument();
                writer.writeStartElement("osm");
                writer.writeAttribute("version", "0.6");
                writer.writeAttribute("ChunkId", Integer.toString(this.chunkId));

                fileParser.getChunck().getQuadrant(index).createXMLElement(writer);
            writer.writeEndDocument();
            chunkFiles.appendChunkFile(fileParser.getChunck().getQuadrant(index), localChunkPath);
            this.chunkId++; // Increment the chunkId for the next chunkFile. So each chunkFile has a unique id
        }
    }

    public void writeTagNode(TagNode node, TagBound bound) throws XMLStreamException{
        XMLStreamWriter writer = getStreamWriter(bound);
    
        node.createXMLElement(writer);
    }
    
    private XMLStreamWriter getStreamWriter(int index){
        return this.writers.get(index);
    }
    /**
     * Get the XMLStreamWriter for the a specific bound. The useage of this method is to write to a specific chunk file.
     * @param bound - The bound to get the writer for
     * @return XMLStreamWriter - The writer for the bound
     */
    private XMLStreamWriter getStreamWriter(TagBound bound){
        int chunkId = chunkFiles.getChunkId(bound);

        return getStreamWriter(chunkId);
    }

    public void closeWrtier(XMLStreamWriter writer) throws XMLStreamException{
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

    public void closeAllWriters() throws XMLStreamException{
        for (XMLStreamWriter xmlStreamWriter : writers) {
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
        }
    }

    private FileWriter createChunkFile(){
        FileWriter w = null;
        try {
            w = new FileWriter(directoryPath + (this.chunkId) + ".xml");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file. Is the id unique and the directory path correct? ");
            System.out.println(e.getMessage());
        }
        return w;
    }

    // public void writeTagNode(TagNode node, XMLStreamWriter writer) throws XMLStreamException{
    //     BigDecimal lat = node.getLat();
    //     BigDecimal lon = node.getLon();

    //     createXMLElement(writer, "node", new HashMap<String, String>() {
    //         {
    //             put("id", node.getId().toString());
    //             put("lat", node.getLat().toString());
    //             put("lon", node.getLon().toString());
    //         }
    //     });
    // }

    /**
     * A class to control the chunk files' paths and the bounds of the chunks
     * 
     * <p>
     *  This class does not have any relation to the content within the chunk files.
     * </p>
     * 
     */
    private class ChunkFiles {
        private HashMap<TagBound, String> chunkFiles = new HashMap<TagBound, String>();

        public void appendChunkFile(TagBound bound, String path){
            this.chunkFiles.put(bound, path);
        }

        public String getChunkFilePath(TagBound bound){
            return this.chunkFiles.get(bound);
        }

        public TagBound getBound(String path){
            for (Map.Entry<TagBound, String> entry : this.chunkFiles.entrySet()) {
                if (entry.getValue().equals(path)) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public int getChunkId(TagBound bound){
            return Integer.parseInt(this.chunkFiles.get(bound).split(".")[0]);
        }

        public HashMap<TagBound, String> getChunkFiles(){
            return this.chunkFiles;
        }

        @Override
        public String toString(){
            return this.chunkFiles.toString();
        }
    }
}