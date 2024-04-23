package org;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;


import parser.TagBound;
import parser.TagNode;
import parser.XMLReader;
import parser.XMLWriter;
import util.FileDistributer;
import util.MecatorProjection;
import java.io.File;

public class XMLWriterTest {
    final static String directoryPath = "src/main/resources/chunks/";
    static File directory = new File(directoryPath);

    @BeforeAll
    public static void setUp() {
        assertDoesNotThrow(() -> {
            new XMLReader(FileDistributer.input.getFilePath());
        });
        new XMLWriter(XMLReader.getBound());
    }
    
    @AfterAll
    public static void tearDown() {
        for (File file : directory.listFiles()) {
            file.delete();
        }
    }

    @Test
    public void testInitChunkFiles() {
        TagBound bound = XMLReader.getBound();
        XMLWriter xmlWriter = new XMLWriter(bound);
        xmlWriter.initChunkFiles(bound);

        int i = 0;

        for (int j = 0; j < directory.listFiles().length; j++) {
            assertTrue(directory.listFiles()[i].getName().matches("chunk_\\d+.bin"));
            assertEquals( "chunk_" + i + ".bin", directory.listFiles()[i].getName());
        }
        // TODO: test if the right TagBound is in the file
        
    }

    @Test
    public void testGetContentFromBinaryFile(){
        
    }

    @Test
    public void testGetTagsFromChunk() {
        parser.Tag tag = XMLWriter.readTagByIdFromBinaryFile(286405322l);
        assertNotNull(tag);

    }

}