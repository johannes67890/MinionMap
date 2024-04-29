package org;




import parser.Tag;
import parser.TagAddress;
import parser.TagBound;
import parser.TagNode;
import parser.TagWay;
import parser.XMLReader;
import parser.XMLWriter;
import parser.XMLWriter.ChunkFiles;
import util.FileDistributer;
import util.MecatorProjection;
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
            new XMLReader(FileDistributer.input.getFilePath());
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
        ArrayList<Tag> dup = new ArrayList<Tag>();

        long total_Addreses = XMLReader.getAddresses().size();
        long total_Relations = XMLReader.getRelations().size();
        long total_nodes = XMLReader.getNodes().size();
        long total_ways = XMLReader.getWays().size();
        Set<Tag> set = new HashSet<>();

        assertDoesNotThrow(() -> {
            XMLWriter.getAllTagsFromChunks();
        });
        List<Tag> tags = XMLWriter.getAllTagsFromChunks();
        assertNotNull(tags);
        List<Tag> test = new ArrayList<>();

        for (Tag tag : tags) {
            if (tag instanceof TagAddress) {
                total_Addreses--;
                continue;
            }
            if (tag instanceof TagNode) {
                TagNode t = (TagNode) tag;
                if(t.getId() == 6760379515l){
                    test.add(t);
                }
                if(!set.contains(t)){
                    set.add(t);
                    total_nodes--;
                }else{
                    dup.add(t);
                }
                for (TagWay p : t.getParents()) {
                    if(!set.contains(p)){
                        set.add(p);
                        total_ways--;
                    }else{
                        dup.add(p);
                    }
                }
            }
        }
        TagNode t = XMLReader.getNodeById(1682443859l);
        assertEquals(0, dup.size());
        assertEquals(0, total_ways);
        assertEquals(0, total_nodes);
        assertEquals(0, total_Addreses);
    }

    // @Test
    // public void testGetTagsFromChunk() {
    //     // parser.Tag tag = XMLWriter.getTagByIdFromBinaryFile(286405322l);
    //     // assertNotNull(tag);

    // }

}