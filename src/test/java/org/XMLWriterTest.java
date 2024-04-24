package org;




import parser.Tag;
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
        long totalAmountOfTags =  
          XMLReader.getWays().valueCollection().size() 
        + XMLReader.getRelations().valueCollection().size() 
        + XMLReader.getAddresses().valueCollection().size();


    }

    // @Test
    // public void testGetTagsFromChunk() {
    //     // parser.Tag tag = XMLWriter.getTagByIdFromBinaryFile(286405322l);
    //     // assertNotNull(tag);

    // }

}