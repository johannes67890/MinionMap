package parser;
import java.io.*;
import java.util.*;

public class XMLWriter {
    private String directoryPath = "src/main/resources/chunks/";
    private static ChunkFiles chunkFiles = new ChunkFiles();
    private static int chunkId = 0;

    public XMLWriter(TagBound bounds) {
        initChunkFiles(bounds);    

    }

    public void initChunkFiles(TagBound bounds) {   
        for (TagBound parentChunk : Chunk.getQuadrants(bounds).values()) {
            Chunk childChunk = new Chunk(parentChunk);
            
            for (int j = 0; j < 4; j++) {
                TagBound child = childChunk.getQuadrant(j);
                //System.out.println("parent chunk " + parentChunk + " -> " + childChunk.getBoundQuadrant(childChunk.getQuadrant(j)).toString() + " - " + child);
                chunkFiles.appendChunkFile(child, directoryPath + "chunk_" + chunkId + ".bin");
                createBinaryChunkFile(directoryPath + "chunk_" + chunkId + ".bin", child);
                chunkId++;
            }
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


    public static void writeToBinary(Tag<?> node){
        ObjectOutputStream out = null;
        
        for (TagBound bound : chunkFiles.getChunkFiles().keySet()) {
            String path = chunkFiles.getChunkFilePath(bound);
                if (node.isInBounds(bound)){
                    File file = new File(path);
                    try{
                        if (!file.exists()) out = new ObjectOutputStream(new FileOutputStream (path));
                        else out = new AppendableObjectOutputStream (new FileOutputStream (path, true));
                        out.writeObject(node);
                        out.flush();
                    }catch (Exception e){
                        e.printStackTrace ();
                    }finally{
                        try{
                            if (out != null) out.close();
                        }catch (Exception e){
                            e.printStackTrace ();
                        }
                    }

                }            
            }
    }
  
    public static Tag<?> getTagByIdFromBinaryFile(long id){
        try {
        for (String path : chunkFiles.getChunkFilePaths()) {
            FileInputStream fstream = new FileInputStream(path);
            ObjectInputStream ostream = new ObjectInputStream(fstream);
            while (true) {
                Tag<?> s = (Tag<?>) ostream.readObject();
                    if (s.getId() == id) return s;
            }

        }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }
        return null;
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