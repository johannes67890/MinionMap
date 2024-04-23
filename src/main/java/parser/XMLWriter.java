package parser;
import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.io.BufferedOutputStream;
import java.io.BufferedOutputStream;

public class XMLWriter {
    private String directoryPath = "src/main/resources/chunks/";
    public static ChunkFiles chunkFiles = new ChunkFiles();
    private static HashMap<TagBound, List<Tag>> tagList = new HashMap<TagBound, List<Tag>>();
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
            for (TagBound midChunk : Chunk.getQuadrants(parentChunk).values()) {
                    Chunk childChunk = new Chunk(midChunk); 
                    for (int j = 0; j < 4; j++) {
                        // Get one of the four quadrants in the chunk
                        TagBound child = childChunk.getQuadrant(j);
                        // Create the chunk file
                        createBinaryChunkFile(directoryPath + "chunk_" + chunkId + ".bin", child);
                        chunkId++;
                    }
                }
        }
    }

    private static void createBinaryChunkFile(String path, TagBound bound){
        chunkFiles.appendChunkFile(bound, path);
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

        for (TagBound bound : chunkFiles.getChunkFiles().keySet()) {
            if(node.isInBounds(bound)){
                tagList.computeIfAbsent(bound, k -> new ArrayList<>()).add(node);
            }
        }
    }

    public static void appendToBinary() {
        ForkJoinPool pool = new ForkJoinPool();
        long s = System.currentTimeMillis();
        for (Map.Entry<TagBound, List<Tag>> entry : tagList.entrySet()) {
            String path = chunkFiles.getChunkFilePath(entry.getKey());
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

        System.out.println("Time for append to binary: " + (System.currentTimeMillis() - s) + "ms");
    }
    public static long t = 0;

    private static class WriteTagAction extends RecursiveAction {
        private final List<? extends Tag> nodes;
        private final String path;
    
        public WriteTagAction(List<? extends Tag> nodes, String path) {
            this.nodes = nodes;
            this.path = path;
        }
    
        @Override
        protected void compute() {
            
            synchronized (path.intern()) {
                try (DataOutputStream oos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(new File(path), true)))) {
                    // TODO: if tags is written to bytes as array - do data get lost?
                    for (Tag tag : nodes) {
                        oos.write(tag.tagToBytes());
                    }    
                    /*
                     * Write the nodes to the file - is this better? 
                     * It is for sure faster, but does it work?
                     * oos.write(Tag.tagToBytes(nodes)); 
                     */
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static ArrayList<Tag> getContentFromBinaryFile(){
        ArrayList<Tag> objectList = new ArrayList<Tag>();
        String path = "src/main/resources/chunks/chunk_5.bin";
        File file = new File(path);

        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        
            while (true) {
                try {
                    Object o = ois.readObject();
                    if(o instanceof TagBound) continue;
                    if (o instanceof TagRelation) {
                        objectList.add((TagRelation) o);
                    }
                    if(o instanceof TagNode){
                        objectList.add((TagNode) o);
                    }
                    if(o instanceof TagAddress){
                        objectList.add((TagAddress) o);
                    }
                    if(o instanceof TagWay){
                        objectList.add((TagWay) o);
                    }
                
                } catch (EOFException e) {
                    ois.close();
                    break; // end of stream
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return objectList;
    }


  
    public static Tag readTagByIdFromBinaryFile(long id){
        String path = "src/main/resources/chunks/chunk_5.bin";
        File file = new File(path);

        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        
            while (true) {
                try {
                    Object o = ois.readObject();
                        if(o instanceof TagBound) continue;

                        if(((Tag) o).getId() == id){
                            return (Tag) o;
                        }
                    
                        // if (o instanceof TagWay) {
                        //     TagWay way = (TagWay) o;
                        //     if(way.getNodes().containsKey(id)){
                        //         return way.getNodeById(id);
                        //     } else continue;
                        // }
                        // if(o instanceof TagRelation){
                        //     TagRelation relation = (TagRelation) o;
                        //     if(relation.getMembers().containsKey(id)){
                        //         relation.getMemberById(id);
                        //     } else continue;
                        // }
                    
                } catch (EOFException e) {
                    ois.close();
                    throw new IllegalArgumentException("Tag with " + id + " not found");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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

        // public int getChunkId(TagBound bound){
        //     return Integer.parseInt(this.chunkFiles.get(bound).split(".")[0]);
        // }

        public HashMap<TagBound, String> getChunkFiles(){
            return this.chunkFiles;
        }

        @Override
        public String toString(){
            return this.chunkFiles.toString();
        }
    }
}