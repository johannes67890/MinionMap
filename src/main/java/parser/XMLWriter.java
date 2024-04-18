// package parser;
// import java.io.*;
// import java.util.*;

// public class XMLWriter {
//     private String directoryPath = "src/main/resources/chunks/";
//     public static ChunkFiles chunkFiles = new ChunkFiles();
//     private static int chunkId = 0;

//     public XMLWriter(TagBound bounds) {
//         initChunkFiles(bounds);    
//     }

//     public void initChunkFiles(TagBound bounds) {   
//         // Split the bounds into smaller chunks
//         for (TagBound parentChunk : Chunk.getQuadrants(bounds).values()) {
//             Chunk childChunk = new Chunk(parentChunk); 
            
//             for (int j = 0; j < 4; j++) {
//                 // Get one of the four quadrants in the chunk
//                 TagBound child = childChunk.getQuadrant(j);
//                 // Create the chunk file
//                 createBinaryChunkFile(directoryPath + "chunk_" + chunkId + ".bin", child);
//                 chunkId++;
//             }
//         }
//     }

//     private static void createBinaryChunkFile(String path, TagBound bound){
//         chunkFiles.appendChunkFile(bound, path);
//         try{
//             File file = new File(path);
//             ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
//             oos.writeObject(bound);
//             oos.close();
//         }catch (Exception e){
//             e.printStackTrace();
//         }
        
//     }

//     public static void appendToBinary(Tag<?> node) throws IOException {
//         ObjectOutputStream oos=null;
        
//         for (TagBound bound : chunkFiles.getChunkFiles().keySet()) {
//             String path = chunkFiles.getChunkFilePath(bound);
//             if(node.isInBounds(bound)){
//                 try{
//                     File file = new File(path);
//                     if(file.exists()){
//                         oos = new AppendingObjectOutputStream(new FileOutputStream(file, true));
//                     }else{
//                         oos = new ObjectOutputStream(new FileOutputStream(file));
//                     }
//                     oos.writeObject(node);
//                     oos.close();
//                 }catch (Exception e){
//                     e.printStackTrace();
//                 }
            
//             }
//         }
//     }

//     public static class AppendingObjectOutputStream extends ObjectOutputStream {

//         public AppendingObjectOutputStream(OutputStream out) throws IOException {
//           super(out);
//         }
      
//         @Override
//         protected void writeStreamHeader() throws IOException {
//           reset();
//         }
      
//       }

//     public static ArrayList<Tag<?>> getContentFromBinaryFile(){
//         ArrayList<Tag<?>> objectList = new ArrayList<Tag<?>>();
//         String path = "src/main/resources/chunks/chunk_5.bin";
//         File file = new File(path);

//         try{
//             ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        
//             while (true) {
//                 try {
//                     Object o = ois.readObject();
//                     if(o instanceof TagBound) continue;
//                     if (o instanceof TagRelation) {
//                         objectList.add((TagRelation) o);
//                     }
//                     if(o instanceof TagNode){
//                         objectList.add((TagNode) o);
//                     }
//                     if(o instanceof TagAddress){
//                         objectList.add((TagAddress) o);
//                     }
//                     if(o instanceof TagWay){
//                         objectList.add((TagWay) o);
//                     }
                
//                 } catch (EOFException e) {
//                     ois.close();
//                     break; // end of stream
//                 }
//             }
//         }catch (Exception e){
//             e.printStackTrace();
//         }
//         return objectList;
//     }


  
//     public static Tag<?> readTagByIdFromBinaryFile(long id){
//         String path = "src/main/resources/chunks/chunk_5.bin";
//         File file = new File(path);

//         try{
//             ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        
//             while (true) {
//                 try {
//                     Object o = ois.readObject();
//                         if(o instanceof TagBound) continue;

//                         if(((Tag<?>) o).getId() == id){
//                             return (Tag<?>) o;
//                         }
                    
//                         if (o instanceof TagWay) {
//                             TagWay way = (TagWay) o;
//                             if(way.getRefs().containsKey(id)){
//                                 return way.getNodeById(id);
//                             } else continue;
//                         }
//                         if(o instanceof TagRelation){
//                             TagRelation relation = (TagRelation) o;
//                             if(relation.getMembers().containsKey(id)){
//                                 relation.getMemberById(id);
//                             } else continue;
//                         }
                    
//                 } catch (EOFException e) {
//                     ois.close();
//                     throw new IllegalArgumentException("Tag with " + id + " not found");
//                 }
//             }
//         }catch (Exception e){
//             e.printStackTrace();
//         }
//         return null;
//     }


//     /**
//      * A class to control the chunk files' paths and the bounds of the chunks
//      * 
//      * <p>
//      *  This class does not have any relation to the content within the chunk files.
//      * </p>
//      * 
//      */
//     private static class ChunkFiles {
//         private HashMap<TagBound, String> chunkFiles = new HashMap<TagBound, String>();

//         public void appendChunkFile(TagBound bound, String path){
//             this.chunkFiles.put(bound, path);
//         }

//         public String getChunkFilePath(TagBound bound){
//             return this.chunkFiles.get(bound);
//         }

//         public Collection<String> getChunkFilePaths(){
//             return this.chunkFiles.values();
//         }

//         public TagBound getBound(String path){
//             for (Map.Entry<TagBound, String> entry : this.chunkFiles.entrySet()) {
//                 if (entry.getValue().equals(path)) {
//                     return entry.getKey();
//                 }
//             }
//             return null;
//         }

//         // public int getChunkId(TagBound bound){
//         //     return Integer.parseInt(this.chunkFiles.get(bound).split(".")[0]);
//         // }

//         public HashMap<TagBound, String> getChunkFiles(){
//             return this.chunkFiles;
//         }

//         @Override
//         public String toString(){
//             return this.chunkFiles.toString();
//         }
//     }
// }