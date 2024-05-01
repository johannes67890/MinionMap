package parser;

import parser.Tag;
import parser.TagAddress;
import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.XMLReader;
import parser.XMLWriter;
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

    // @Test
    // public void testInitChunkFiles() {
    //     TagBound bound = XMLReader.getBound();

    //     int i = 0;

    //     for (int j = 0; j < directory.listFiles().length; j++) {
    //         assertTrue(directory.listFiles()[i].getName().matches("chunk_\\d+.bin"));
    //         assertEquals( "chunk_" + i + ".bin", directory.listFiles()[i].getName());
    //     }
        
    // }

    @Test
    public void testGetContentFromBinaryFile(){
        List<Tag> duplicates = XMLWriter.getAllTagsFromChunks();

        Set<Long> setOfWays = new HashSet<>();
        Set<Long> setOfAddress = new HashSet<>();

        /*  */
        List<Tag> tags = XMLWriter.getAllTagsFromChunks();
        assertNotNull(tags);

        for (Tag tag : tags) {
            if(tag instanceof TagNode){
                TagNode t = (TagNode) tag;
                if(!setOfWays.contains(t.getParent().getId())){
                    setOfWays.add(t.getParent().getId());
                }else{
                    duplicates.add(t.getParent());
                }
            } else if(tag instanceof TagAddress){
                TagAddress t = (TagAddress) tag;
                if(!setOfWays.contains(t.getId())){
                    setOfAddress.add(t.getId());
                }else {
                    duplicates.add(t);
                }
            }
        }

        assertEquals(1211, tags.size());
        assertEquals(211, setOfWays.size());             
    }


}