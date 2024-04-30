package org;




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

        TLongObjectHashMap<TagAddress> total_Addreses = XMLReader.getAddresses();
        TLongObjectHashMap<TagRelation> total_Relations = XMLReader.getRelations();
        TLongObjectHashMap<TagNode> total_nodes = XMLReader.getNodes();

        long total_ways = 0;
        Set<Tag> set = new HashSet<>();

        List<Tag> tags = XMLWriter.getAllTagsFromChunks();
        assertNotNull(tags);

        for (Tag tag : tags) {
            if (tag instanceof TagAddress) {
                total_Addreses.remove(tag.getId());
                continue;
            }
            if (tag instanceof TagNode) {
                TagNode t = (TagNode) tag;
                if(!set.contains(t)){
                    if(t.getParent() != null) total_ways++;
                    set.add(t);
                    total_nodes.remove(t.getId());
                }else{
                    dup.add(t);
                }
                   
            }
        }
        assertEquals(0, dup.size());
        assertTrue(total_Addreses.isEmpty());
        assertTrue(total_nodes.isEmpty());
        assertEquals(211, total_ways);
                
    }

    @Test
    public void testGetTagsFromChunk() {
        // Get tagNode and see its parent
        Tag a = XMLWriter.getTagByIdFromBinaryFile(286405342l);
        assertNotNull(a);
        if(a instanceof TagNode){
               TagNode t = (TagNode) a;
               assertEquals(26154396,t.getParent().getId());
        }
        // Get tagWay directly from the chunk
        Tag b = XMLWriter.getTagByIdFromBinaryFile(26154396l);
        assertNotNull(b);
        if(b instanceof TagWay){
            TagWay t = (TagWay) b;
            assertEquals(26154396, t.getId());
       }
    }
}