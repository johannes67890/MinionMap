package parser;

import parser.Tag;
import parser.TagAddress;
import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.XMLReader;
import parser.XMLWriter;
import parser.XMLWriter.ChunkFiles;
import util.FileDistributer;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.LSD;
import gnu.trove.map.hash.TLongObjectHashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMLWriterTest {
    final static String directoryPath = "src/main/resources/chunks/";
    static File directory = new File(directoryPath);

    @BeforeEach
    public void setUp() {
        tearDown();
        assertDoesNotThrow(() -> {
            new XMLReader(FileDistributer.testMap.getFilePath());
        });
    }
    
   
    public static void tearDown() {
        if(!directory.exists()){
            directory.mkdirs();
        }
        for (File file : directory.listFiles()) {
            file.delete();
        }
    }

    @Test
    public void testInitChunkFiles() {
        int i = 0;

        for (int j = 0; j < directory.listFiles().length; j++) {
            assertTrue(directory.listFiles()[i].getName().matches("chunk_\\d+.bin"));
            assertEquals( "chunk_" + i + ".bin", directory.listFiles()[i].getName());
        }
        
    }

    @Test
    public void testGetContentFromBinaryFile(){

        Set<Long> setOfWays = new HashSet<>();
        Set<Long> setOfAddress = new HashSet<>();
        Set<Long> setOfRelations = new HashSet<>();

        /*  */
        Set<Tag> tags = XMLWriter.getAllTagsFromChunks();
        assertNotNull(tags);

        for (Tag tag : tags) {
            if(tag instanceof TagNode){
                TagNode t = (TagNode) tag;
                setOfWays.add(t.getParent().getId());
                if(t.getParent().getRelationParent() != null){
                    setOfRelations.add(t.getParent().getRelationParent().getId());
                }
            } else if(tag instanceof TagAddress){
                TagAddress t = (TagAddress) tag;
                setOfAddress.add(t.getId());
            } 
        }

        assertEquals(1167, tags.size(), 2);
        assertEquals(211, setOfWays.size(), 1);   
        assertEquals(46, setOfAddress.size(), 1);  
        assertEquals(1, setOfRelations.size());        
    }
    @Test
    public void testGetChunksWithinBoundaries(){
        TagBound viewBound = new TagBound(55.65745f, 55.65916f, 12.46444f, 12.46918f);
        assertTrue(viewBound.isInBounds(XMLReader.getBound()));
        List<String> tags = ChunkFiles.getChunksFilesWithinBounds(viewBound);
        assertEquals(1, tags.size());
    }
}