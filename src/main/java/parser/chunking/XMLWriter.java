package parser.chunking;
import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

import parser.Tag;
import parser.TagBound;
import parser.TagNode;

/**
 * WARNING: This class class is not used in the current implementation of the project.
 * 
 * <p>
 * The XMLWriter class is used to write the tags to binary files.
 * </p>
 * The XMLWriter creates 256 binary files of chunks, each representing a quadrant of the previous chunk.
 * <p>
 * Each chunk file contains the tags that are within the bounds of the chunk. Each tag is added to a hashset that upoon completion is written to the binary file. 
 * The tags are written to binary in a parallel stream by the use of {@link ForkJoinPool}. This is to increase the performance of the writing process.
 * </p>
 * @see {@link ForkJoinPool} For parallel streams
 */
public class XMLWriter {
    private String directoryPath = "src/main/resources/chunks/";
    public static ChunkFiles chunkFiles = new ChunkFiles();
    private static HashMap<TagBound, HashSet<Tag>> tagList = new HashMap<TagBound, HashSet<Tag>>();
    private static int chunkId = 0;

    public XMLWriter(TagBound bounds) {
        // Check if the directoryPath folder exists, if not, create it
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if(directory.listFiles().length  > 0){
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }

        initChunkFiles(bounds);    
    }

    /**
     * Initialize the chunk files
     * Create the chunk files and write the bounds to the files
     * <p>
     * There is created 256 chunk files that is created in the directoryPath folder. 
     * @param bounds - The bounds of the root chunk
     */
    public void initChunkFiles(TagBound bounds) {   
        for (TagBound parentChunk : Chunk.getQuadrants(bounds).values()) {
            for (TagBound midChunk : Chunk.getQuadrants(parentChunk).values()) {
                for (TagBound childChunk : Chunk.getQuadrants(midChunk).values()) {
                    Chunk chunk = new Chunk(childChunk); 
                    for (int j = 0; j < 4; j++) {
                        // Get one of the four quadrants in the chunk
                        TagBound child = chunk.getQuadrant(j);
                        // Create the chunk file
                        createBinaryChunkFile(directoryPath + "chunk_" + chunkId + ".bin", child);
                        chunkId++;
                    }
                }
            }
        }
    }

    /**
     * Create a binary chunk file and write the corresponding bounds of the chunk to a file-path.
     * @param path - The path to the file
     * @param bound - The bounds of the chunk
     */
    private static void createBinaryChunkFile(String path, TagBound bound){
        ChunkFiles.appendChunkFile(bound, path);
        try{
            File file = new File(path);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(bound);
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }

    /**
     * Append a tag to the pool of tags that will be written to the binary files.
     * <p>
     * The "pool" is a hashmap that contains the bounds of the chunk files as keys and a hashset of tags as values.
     * </p>
     * @param node - The tag to be added to the pool
     */
    public static void appendToPool(Tag node){
        for (TagBound bound : ChunkFiles.getChunkFiles().keySet()) {
            if(node.isInBounds(bound)){
                tagList.computeIfAbsent(bound, k -> new HashSet<>()).add(node);
            }
        }
    }

    /**
     * Appends all the tags in the pool to the binary files.
     * @param node - The tag to be added to the binary file
     */
    public static void appendToBinary() {
        ForkJoinPool pool = new ForkJoinPool();

        for (Map.Entry<TagBound, HashSet<Tag>> entry : tagList.entrySet()) {
            String path = ChunkFiles.getChunkFilePath(entry.getKey());
            pool.submit(new WriteTagAction(entry.getValue(), path));
        }

        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            tagList.clear();
        }
    }

    /**
     * A RecursiveAction that writes the tags to the binary files.
     */
    private static class WriteTagAction extends RecursiveAction {
        private final HashSet<? extends Tag> nodes;
        private final String path;
    
        public WriteTagAction(HashSet<? extends Tag> nodes, String path) {
            this.nodes = nodes;
            this.path = path;
        }
    
        @Override
        protected void compute() {
            
            synchronized (path.intern()) {
                try (AppendingObjectOutputStream oos = new AppendingObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(path), true)))) {
                    for (Tag tag : nodes) {
                        oos.writeObject(tag);
                    } 
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * An ObjectOutputStream that does not write the stream header every time an object is written.
     * This is done to avoid the stream header to be written multiple times in the same file.
     * 
     * See {@link ObjectOutputStream#writeStreamHeader()} for more information.
     * or {@link <a href="https://www.geeksforgeeks.org/how-to-fix-java-io-streamcorruptedexception-invalid-type-code-in-java/">geeksforgeeks.org/java.io.StreamCorruptedException</a>} 
     */
    public static class AppendingObjectOutputStream extends ObjectOutputStream {
        public AppendingObjectOutputStream(OutputStream out) throws IOException {
          super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
          reset();
        }
      }

    /**
     * Get all the tags from the all the chunks files.
     * @return A set of all the tags from the chunk files
     */
    public static Set<Tag> getAllTagsFromChunks(){
        Set<Tag> objectList = new HashSet<>();

        if(ChunkFiles.getChunkFilePaths().isEmpty()) throw new IllegalArgumentException("No chunk files found");

        for (String path : ChunkFiles.getChunkFilePaths()) {
            try (ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)))) {
                while (true) {
                    try {
                        Object o = stream.readObject();
                        if(o instanceof TagBound) continue;
                        else{
                            objectList.add((Tag) o);
                        }
                    } catch (EOFException e) {
                        stream.close();
                        break; // end of stream
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }

    /**
     * Get all the tags from a specific chunk file.
     * @param bound - The bounds of the chunk file
     * @return A list of all the tags from the chunk file
     */
    public static List<Tag> getTagsFromChunkByBounds(TagBound bound){ {
        String path = ChunkFiles.getChunkFilePath(bound);
        List<Tag> objectList = new ArrayList<>();

        try (ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)))) {
            while (true) {
                try {
                    Object o = stream.readObject();
                    if(o instanceof TagBound) continue;
                    if (o instanceof Tag) {
                        objectList.add((Tag) o);
                    }
                } catch (EOFException e) {
                    stream.close();
                    break; // end of stream
                }
            }
        }catch (Exception e){
            System.out.println("ooof");
           e.printStackTrace();
        }
        return objectList;
        }
    }

    /**
     * Get a tag by its id from the chunk files.
     * @param id - The id of the tag
     * @return The tag with the specified id
     */
    public static Tag getTagByIdFromBinaryFile(long id) {
        for (String path : ChunkFiles.getChunkFilePaths()) {
            try (ObjectInputStream stream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)))) {
                while (true) {
                    try {
                        Object o = stream.readObject();
                        if(o instanceof TagBound) continue;
                        if(o instanceof Tag && ((Tag) o).getId() == id){
                            return (Tag) o;
                        } 
                        else if(o instanceof TagNode){
                            TagNode n = (TagNode) o;
                            if(n.getParent().getId() == id){
                                return n.getParent();
                            }
                        }

                    } catch (EOFException e) {
                        stream.close();
                        break; // end of stream
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }   
        return null;
    }
    
    /**
     * A class to control the chunk files' paths and the bounds of the chunks
     * 
     * <p>
     *  This class does not have any relation to the content within the chunk files.
     * </p>
     */
    public static class ChunkFiles {
        private static HashMap<TagBound, String> chunkFiles = new HashMap<TagBound, String>();

        /**
         * Append a chunk file to the chunkFiles hashmap
         * @param bound - The bounds of the chunk
         * @param path - The path to the chunk file
         */
        public static void appendChunkFile(TagBound bound, String path){
            chunkFiles.put(bound, path);
        }

        /**
         * Get the bounds of the chunk file that the tag is in.
         * @param tag - The tag to get the bounds from
         * @return The bounds of the chunk file, null if the tag is not in any chunk file
         */
        public static TagBound getBoundFromTag(Tag tag){
            for (Map.Entry<TagBound, String> entry : chunkFiles.entrySet()) {
                if (tag.isInBounds(entry.getKey())) {
                    return entry.getKey();
                }
            }
            return null;
        }

        /**
         * Get the chunk file path from the tag
         * @param tag - The tag to get the chunk file path from
         * @return The path to the chunk file, null if the tag is not in any chunk file
         */
        public static String getChunkFromTag(Tag tag){
            for (Map.Entry<TagBound, String> entry : chunkFiles.entrySet()) {
                if(tag.isInBounds(entry.getKey())){
                    return entry.getValue();
                }
            }
            return null;
        }

        /**
         * Get the bounds of the chunk file from the path
         * @param path - The path to the chunk file
         * @return The bounds of the chunk file, null if the path does not exist
         */
        public static TagBound getBound(String path){
            for (Map.Entry<TagBound, String> entry : chunkFiles.entrySet()) {
                if (entry.getValue().equals(path)) {
                    return entry.getKey();
                }
            }
            return null;
        }
        
        /**
         * Return all the chunk files within the bounds
         * 
         * @param bound
         * @return
         */
        public static List<String> getChunksFilesWithinBounds(TagBound bound){
            List<String> paths = new ArrayList<String>();
            
            // for each chunk bound, is the bound within the bounds?
            for (TagBound chunkBound : ChunkFiles.getChunkFiles().keySet()) {
                if(bound.isInBounds(chunkBound)){
                    paths.add(ChunkFiles.getChunkFilePath(chunkBound));
                }  
            }
            return paths;
        }

        /**
         * Get the path to the chunk file from the bounds
         * @param bound - The bounds of the chunk file
         * @return The path to the chunk file
         */
        public static String getChunkFilePath(TagBound bound){
            if(chunkFiles.containsKey(bound)){
                return chunkFiles.get(bound);
            }else throw new IllegalArgumentException("The bound: " + bound + " does not exist in the chunkFiles");
        }

        /**
         * Get all the paths to the chunk files
         * @return A collection of all the paths to the chunk files
         */
        public static Collection<String> getChunkFilePaths(){
            return chunkFiles.values();
        }

        /**
         * Get all the chunk files
         * @return A hashmap of all the chunk files
         */
        public static HashMap<TagBound, String> getChunkFiles(){
            return chunkFiles;
        }

        @Override
        public String toString(){
            return chunkFiles.toString();
        }
    }
}