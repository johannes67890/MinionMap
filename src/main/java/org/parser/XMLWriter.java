package org.parser;
import javax.xml.stream.*;

import java.io.*;
import java.util.*;


class XMLWriter {
    private ChunkFiles chunkFiles = new ChunkFiles();
    private String directoryPath = "src/main/resources/chunks/";
    private int chunkId = 0;

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
            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(createChunkFile());
            writer.writeStartDocument();
                writer.writeStartElement("osm");
                writer.writeAttribute("version", "0.6");
                writer.writeAttribute("ChunkId", Integer.toString(this.chunkId));

                fileParser.getChunck().getQuadrant(index).createXMLElement(writer);

            writer.writeEndDocument();
            writer.flush(); 
            writer.close();
            chunkFiles.appendChunkFile(fileParser.getChunck().getQuadrant(index), localChunkPath);
            this.chunkId++; // Increment the chunkId for the next chunkFile. So each chunkFile has a unique id
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

        public HashMap<TagBound, String> getChunkFiles(){
            return this.chunkFiles;
        }

        @Override
        public String toString(){
            return this.chunkFiles.toString();
        }


    }
}