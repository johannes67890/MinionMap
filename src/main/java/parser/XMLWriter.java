package parser;
import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.util.HashSet;

import java.io.IOException;

public class XMLWriter {
    private String directoryPath = "src/main/resources/chunks/";
    public static ChunkFiles chunkFiles = new ChunkFiles();
    // TODO: tagList - do we lose data if we use a hashset instead of a list?
    private static HashMap<TagBound, HashSet<Tag>> tagList = new HashMap<TagBound, HashSet<Tag>>();
    private static int chunkId = 0;

    public XMLWriter(TagBound bounds) {
        // Check if the directoryPath folder exists, if not, create it
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if(directory.listFiles().length > 256){
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }

        initChunkFiles(bounds);    
    }

    public void initChunkFiles(TagBound bounds) {   
        for (TagBound parentChunk : Chunk.getQuadrants(bounds).values()) {
        //     for (TagBound midChunk : Chunk.getQuadrants(parentChunk).values()) {
        //         for (TagBound childChunk : Chunk.getQuadrants(midChunk).values()) {
                    Chunk chunk = new Chunk(parentChunk); 
                    for (int j = 0; j < 4; j++) {
                        // Get one of the four quadrants in the chunk
                        TagBound child = chunk.getQuadrant(j);
                        // Create the chunk file
                        createBinaryChunkFile(directoryPath + "chunk_" + chunkId + ".bin", child);
                        chunkId++;
                    }
        //         }
        //     }
        }
    }

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

    public static void appendToPool(Tag node){
        for (TagBound bound : ChunkFiles.getChunkFiles().keySet()) {
            if(node.isInBounds(bound)){
                tagList.computeIfAbsent(bound, k -> new HashSet<>()).add(node);
            }
        }
    }

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
     * A RecursiveAction that writes a list of tags to a binary file.
     * 
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
                    // TODO: if tags is written to bytes as array - do data get lost?
                    for (Tag tag : nodes) {
                        oos.writeObject(tag);
                    }    
                    /*
                     * Write the nodes to the file - is this better? 
                     * It is for sure faster, but does it work?
                     */
                    // oos.writeObject(nodes); 
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

    public static List<Tag> getAllTagsFromChunks(){
        List<Tag> objectList = new ArrayList<>();

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
                        e.printStackTrace();
                        break; // end of stream
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objectList;
    }

    public static List<Tag> getTagsFromChunk(TagBound bound){ {
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
            e.printStackTrace();
        }
        return objectList;
        }
    }

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
                        //  else if(o instanceof TagRelation){
                        //     for (TagWay w : ((TagRelation) o).getWays()) {
                        //         for (TagNode n : w.getNodes()) {
                        //             if(n.getId() == id){
                        //                 return n;
                        //             }
                        //         }
                        //     }
                        // }

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
     * 
     */
    public static class ChunkFiles {
        private static HashMap<TagBound, String> chunkFiles = new HashMap<TagBound, String>();

        public static void appendChunkFile(TagBound bound, String path){
            chunkFiles.put(bound, path);
        }

        public static TagBound getBoundFromTag(Tag tag){
            for (Map.Entry<TagBound, String> entry : chunkFiles.entrySet()) {
                if (tag.isInBounds(entry.getKey())) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public static String getChunkFromTag(Tag tag){
            for (Map.Entry<TagBound, String> entry : chunkFiles.entrySet()) {
                if(tag.isInBounds(entry.getKey())){
                    return entry.getValue();
                }
            }
            return null;
        }

        public static TagBound getBound(String path){
            for (Map.Entry<TagBound, String> entry : chunkFiles.entrySet()) {
                if (entry.getValue().equals(path)) {
                    return entry.getKey();
                }
            }
            return null;
        }
        
        public static String getChunkFilePath(TagBound bound){
            if(chunkFiles.containsKey(bound)){
                return chunkFiles.get(bound);
            }else throw new IllegalArgumentException("The bound: " + bound + " does not exist in the chunkFiles");
        }

        public static Collection<String> getChunkFilePaths(){
            return chunkFiles.values();
        }


        // public int getChunkId(TagBound bound){
        //     return Integer.parseInt(this.chunkFiles.get(bound).split(".")[0]);
        // }

        public static HashMap<TagBound, String> getChunkFiles(){
            return chunkFiles;
        }

        @Override
        public String toString(){
            return chunkFiles.toString();
        }
    }
}