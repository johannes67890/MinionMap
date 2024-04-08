package parser;
import javax.xml.stream.*;

import java.io.*;
import java.util.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

class XMLWriter {
    private String directoryPath = "src/main/resources/chunks/";
    private static ChunkFiles chunkFiles = new ChunkFiles();
    public XMLWriter(){}

    public XMLWriter(TagBound bounds) {
        initChunkFiles(new FileParser(bounds));
    }

    public void initChunkFiles(FileParser fileParser) {   
        int chunkId = 0;
        for (int i = 0; i < 4; i++) {
            // Create a new file for each chunk
            String localChunkPath = directoryPath + "chunk_" + chunkId + ".bin";

            final int index = i; // Declare a final variable to use in the lambda expression
            // Create the directory if it doesn't exist

            TagBound ChunkBound = fileParser.getChunck().getQuadrant(index);
            chunkFiles.appendChunkFile(ChunkBound, localChunkPath);
            createBinaryChunkFile(localChunkPath, ChunkBound);
            chunkId++; // Increment the chunkId for the next chunkFile. So each chunkFile has a unique id
        }
    }

    private static void createBinaryChunkFile(String path, TagBound bound){
        try{
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            ObjectOutputStream oos = new AppendableObjectOutputStream (new FileOutputStream (path, true));
            oos.writeObject(bound);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
  
    public static void readNodeFromBinaryFile(String filename){
        File file = new File(filename);
        if (file.exists()){
            ObjectInputStream ois = null;
            try{
                ois = new ObjectInputStream (new FileInputStream (filename));
                while (true){
                    Object s =  ois.readObject();
                    System.out.println(s);
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

    public static void writeToBinary(TagNode node, boolean append){
        ObjectOutputStream out = null;
        
        for (TagBound bound : chunkFiles.getChunkFiles().keySet()) {
            String path = chunkFiles.getChunkFilePath(bound);
                if (node.isInBounds(bound)){
                    File file = new File(path);
                    try{
                        if (!file.exists () || !append) out = new ObjectOutputStream(new FileOutputStream (path));
                        else out = new AppendableObjectOutputStream (new FileOutputStream (path, append));
                        out.writeObject(node);
                        out.flush();
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
            }
    }

    

    public static void readFromBinaryFile (String filename){
        File file = new File (filename);

        if (file.exists()){
            ObjectInputStream ois = null;
            try{
                ois = new ObjectInputStream (new FileInputStream (filename));
                while (true){
                    Object s =  ois.readObject();
                    System.out.println(s);
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


    
    // public  static void writeTag(TagNode node) throws XMLStreamException{
    //     HashMap<TagBound, XMLStreamWriter> writers = XMLWriter.writers;
    //     for (TagBound b : writers.keySet()) {
    //         if (Tag.isInBounds(node, b)){
    //             XMLStreamWriter writer = getStreamWriter(b);
                
    //             node.createXMLElement(writer);
    //             return;
    //         }
    //     }
    // }
    
    // public  static void writeTag(TagAddress address) throws XMLStreamException{
    //     for (TagBound b : writers.keySet()) {
    //         if (Tag.isInBounds(address, b)){
    //             XMLStreamWriter writer = getStreamWriter(b);
    //             address.createXMLElement(writer);
    //             writer.writeCharacters("\n"); // Add a newline character
    //             return;
    //         }
    //     }
    // }

    // public static void writeTag(TagWay way) throws XMLStreamException {
    //     for (TagBound b : writers.keySet()) {
    //         if (Tag.isInBounds(way.getRefs().get(0), b)){
    //             XMLStreamWriter writer = getStreamWriter(b);
    //             try {
    //                 way.createXMLElement(writer);
    //                 writer.writeCharacters("\n"); // Add a newline character
                    
    //             } catch (XMLStreamException e) {
    //                 // Handle the exception here
    //                 System.err.println("An error occurred while creating the XML element: " + e.getMessage());
    //             }
    //             return;
    //         }
    //     }
    // }

    // /**
    //  * Get the XMLStreamWriter for the a specific bound. The useage of this method is to write to a specific chunk file.
    //  * @param bound - The bound to get the writer for
    //  * @return XMLStreamWriter - The writer for the bound
    //  */
    // private static XMLStreamWriter getStreamWriter(TagBound bound) {
    //     return writers.get(bound);
    // }

    // public static void closeWrtier(XMLStreamWriter writer) throws XMLStreamException{
    //     writer.writeEndDocument();
    //     writer.flush();
    //     writer.close();
    // }

    // public static void closeAllWriters() throws XMLStreamException{
    //     for (XMLStreamWriter writer : writers.values()) {
    //         closeWrtier(writer);
    //     }
    // }

    // public void initChunkFiles(FileParser fileParser) throws XMLStreamException{   
    //     String localChunkPath = directoryPath + this.chunkId + ".xml";
        
    //     for (int i = 0; i < 4; i++) {
    //         // Create a new file for each chunk
    //         final int index = i; // Declare a final variable to use in the lambda expression
    //         // Create the directory if it doesn't exist
    //         File directory = new File(directoryPath);
    //         if (!directory.exists()) {
    //             directory.mkdirs(); // mkdirs() creates parent directories if they don't exist
    //         }
            
    //         TagBound ChunkBound = fileParser.getChunck().getQuadrant(index);

    //         XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(createChunkFile());
    //         writer.writeStartDocument();
    //             writer.writeStartElement("osm");
    //             writer.writeAttribute("version", "0.6");
    //             writer.writeAttribute("ChunkId", Integer.toString(this.chunkId));
    //             ChunkBound.createXMLElement(writer);
    //             writer.writeCharacters("\n"); // Add a newline character

    //         chunkFiles.appendChunkFile(ChunkBound, localChunkPath);
    //         writers.put(ChunkBound, writer); // Add the writer to the writers HashMap 

    //         this.chunkId++; // Increment the chunkId for the next chunkFile. So each chunkFile has a unique id
    //     }
    // }

    /**
     * A class to control the chunk files' paths and the bounds of the chunks
     * 
     * <p>
     *  This class does not have any relation to the content within the chunk files.
     * </p>
     * 
     */
    private static class ChunkFiles {
        private HashMap<TagBound, String> chunkFiles = new HashMap<TagBound, String>();

        public void appendChunkFile(TagBound bound, String path){
            this.chunkFiles.put(bound, path);
        }

        public String getChunkFilePath(TagBound bound){
            return this.chunkFiles.get(bound);
        }

        public Collection<String> getChunkFilePaths(){
            return this.chunkFiles.values();
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