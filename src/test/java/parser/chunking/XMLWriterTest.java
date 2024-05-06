// package parser;

// import util.FileDistributer;

// import java.io.File;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;

// import org.junit.jupiter.api.AfterAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import parser.chunking.XMLWriter;
// import parser.chunking.XMLWriter.ChunkFiles;

// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// /**
//  * Warning: These test should be run one at a time, 
//  * else the tearDown cannot keep up with the creation of files.
//  */
// public class XMLWriterTest {
//     final static String directoryPath = "src/main/resources/chunks/";
//     static File directory = new File(directoryPath);

//     @BeforeEach
//     public synchronized void setUp() {
//         assertDoesNotThrow(() -> {
//             new XMLReader(FileDistributer.testMap.getFilePath());
//         });

//     }
    
   
//     @AfterAll
//     public static void tearDown() {
//         File directory = new File(directoryPath);
//         for (File file : directory.listFiles()) {
//             file.delete();
//         }
//     }
    

//     @Test
//     public void testInitChunkFiles() {
//         int i = 0;

//         for (int j = 0; j < directory.listFiles().length; j++) {
//             assertTrue(directory.listFiles()[i].getName().matches("chunk_\\d+.bin"));
//             assertEquals( "chunk_" + i + ".bin", directory.listFiles()[i].getName());
//         }
        
//     }

//     @Test
//     public synchronized void testGetContentFromBinaryFile(){

//         Set<Long> setOfWays = new HashSet<>();
//         Set<Long> setOfAddress = new HashSet<>();
//         Set<Long> setOfRelations = new HashSet<>();

//         /*  */
//         Set<Tag> tags = XMLWriter.getAllTagsFromChunks();
//         assertNotNull(tags);

//         for (Tag tag : tags) {
//             if(tag instanceof TagNode){
//                 TagNode t = (TagNode) tag;
//                 setOfWays.add(t.getParent().getId());
//                 if(t.getParent().getRelationParent() != null){
//                     setOfRelations.add(t.getParent().getRelationParent().getId());
//                 }
//             } else if(tag instanceof TagAddress){
//                 TagAddress t = (TagAddress) tag;
//                 setOfAddress.add(t.getId());
//             } 
//         }

//         assertEquals(1167, tags.size(), 2);
//         assertEquals(211, setOfWays.size(), 1);   
//         assertEquals(46, setOfAddress.size(), 1);  
//         assertEquals(1, setOfRelations.size());        
//     }

//     @Test
//     public synchronized void testGetChunksWithinBoundaries(){
//         /**
//          * A bound where one of the corners only just hit the edge of the original bounds
//          */
//         TagBound viewBound = MecatorProjection.project(new TagBound(55.65786f, 55.65868f, 12.46626f, 12.46807f));
//         assertTrue(viewBound.isInBounds(XMLReader.getBound()));
//         List<String> tags = ChunkFiles.getChunksFilesWithinBounds(viewBound);
//         assertEquals(1, tags.size(), 1);
//         TagBound bound = ChunkFiles.getBound(tags.get(0));
//         assertTrue(viewBound.isInBounds(bound));
//         // test 2
//         viewBound = MecatorProjection.project(new TagBound(55.65799f, 55.65840f, 12.46738f, 12.46821f));
//         assertTrue(viewBound.isInBounds(XMLReader.getBound()));
//         tags = ChunkFiles.getChunksFilesWithinBounds(viewBound);
//         for (String string : tags) {
//             bound = ChunkFiles.getBound(string);
//             assertTrue(viewBound.isInBounds(bound));
//         }

//         /**
//          *  A bound where all of the corners are outside the original bounds
//          */
//         viewBound = MecatorProjection.project(new TagBound(55.65549f, 55.65879f, 12.46590f, 12.47558f));
//         assertTrue(viewBound.isInBounds(XMLReader.getBound()));
//         tags = ChunkFiles.getChunksFilesWithinBounds(viewBound);
//         assertEquals(256, tags.size());
//         for (String string : tags) {
//             bound = ChunkFiles.getBound(string);
//             assertTrue(viewBound.isInBounds(bound));
//         }

//         /**
//          * A bound where all of the corners are outside the original bounds
//          */
//         viewBound = MecatorProjection.project(new TagBound(55.65664f, 55.65811f, 12.46968f, 12.47234f));
//         assertTrue(viewBound.isInBounds(XMLReader.getBound()));
//         tags = ChunkFiles.getChunksFilesWithinBounds(viewBound);
//         assertTrue(tags.size() > 5); // should be around 70 chunks in this area
//         for (String string : tags) {
//             bound = ChunkFiles.getBound(string);
//             assertTrue(viewBound.isInBounds(bound));
//         }
//     }
// }