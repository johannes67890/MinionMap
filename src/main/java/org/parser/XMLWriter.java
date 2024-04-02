package org.parser;
import javax.xml.stream.*;

import java.io.*;
import java.util.*;


class XMLWriter {
    private ChunkFiles chunkFiles = new ChunkFiles();
    private String directoryPath = "src/main/resources/chunks/";
    private int chunkId = 0;
    private static HashMap<TagBound, XMLStreamWriter> writers = new HashMap<TagBound, XMLStreamWriter>();

    public XMLWriter(){}

    public XMLWriter(TagBound bounds) {

        FileParser fileParser = new FileParser(bounds);
        
        try {
            initChunkFiles(fileParser);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            
            TagBound ChunkBound = fileParser.getChunck().getQuadrant(index);

            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(createChunkFile());
            writer.writeStartDocument();
                writer.writeStartElement("osm");
                writer.writeAttribute("version", "0.6");
                writer.writeAttribute("ChunkId", Integer.toString(this.chunkId));
                ChunkBound.createXMLElement(writer);
                writer.writeCharacters("\n"); // Add a newline character

            chunkFiles.appendChunkFile(ChunkBound, localChunkPath);
            writers.put(ChunkBound, writer); // Add the writer to the writers HashMap 

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
  

    public  static void writeTag(TagNode node) throws XMLStreamException{
        HashMap<TagBound, XMLStreamWriter> writers = XMLWriter.writers;
        for (TagBound b : writers.keySet()) {
            if (Tag.isInBounds(node, b)){
                XMLStreamWriter writer = getStreamWriter(b);
                
                node.createXMLElement(writer);
                return;
            }
        }
    }
    
    public  static void writeTag(TagAddress address) throws XMLStreamException{
        for (TagBound b : writers.keySet()) {
            if (Tag.isInBounds(address, b)){
                XMLStreamWriter writer = getStreamWriter(b);
                address.createXMLElement(writer);
                writer.writeCharacters("\n"); // Add a newline character
                return;
            }
        }
    }

    public static void writeTag(TagWay way) throws XMLStreamException {
        for (TagBound b : writers.keySet()) {
            if (Tag.isInBounds(way.getRefs().get(0), b)){
                XMLStreamWriter writer = getStreamWriter(b);
                try {
                    way.createXMLElement(writer);
                    writer.writeCharacters("\n"); // Add a newline character
                    
                } catch (XMLStreamException e) {
                    // Handle the exception here
                    System.err.println("An error occurred while creating the XML element: " + e.getMessage());
                }
                return;
            }
        }
    }
    public static void writeToBinary (String filename, Object obj, boolean append){
        File file = new File (filename);
        ObjectOutputStream out = null;

        try{
            if (!file.exists () || !append) out = new ObjectOutputStream (new FileOutputStream (filename));
            else out = new AppendableObjectOutputStream (new FileOutputStream (filename, append));
            out.writeObject(obj);
            out.flush ();
        }catch (Exception e){
            e.printStackTrace ();
        }finally{
            try{
                if (out != null) out.close ();
            }catch (Exception e){
                e.printStackTrace ();
            }
        }
    }

    public static void readFromBinaryFile (String filename){
        File file = new File (filename);

        if (file.exists ()){
            ObjectInputStream ois = null;
            try{
                ois = new ObjectInputStream (new FileInputStream (filename));
                while (true){
                    Object s = ois.readObject ();
                    System.out.println (s);
                }
            }catch (EOFException e){

            }catch (Exception e){
                e.printStackTrace ();
            }finally{
                try{
                    if (ois != null) ois.close();
                }catch (IOException e){
                    e.printStackTrace ();
                }
            }
        }
    }

    private static class AppendableObjectOutputStream extends ObjectOutputStream {
          public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
          }

          @Override
          protected void writeStreamHeader() throws IOException {}
    }


    /**
     * Get the XMLStreamWriter for the a specific bound. The useage of this method is to write to a specific chunk file.
     * @param bound - The bound to get the writer for
     * @return XMLStreamWriter - The writer for the bound
     */
    private static XMLStreamWriter getStreamWriter(TagBound bound) {
        return writers.get(bound);
    }

    public static void closeWrtier(XMLStreamWriter writer) throws XMLStreamException{
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

    public static void closeAllWriters() throws XMLStreamException{
        for (XMLStreamWriter writer : writers.values()) {
            closeWrtier(writer);
        }
    }


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