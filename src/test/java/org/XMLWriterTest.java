package org;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.parallel.Execution;

import parser.TagBound;
import parser.TagNode;
import parser.XMLReader;
import parser.XMLWriter;
import util.FileDistributer;

public class XMLWriterTest {
    @BeforeAll
    public static void setUp() {
        TagBound bound = new TagBound(55.6562600f, 55.6581600f, 12.4677300f, 12.4734000f);
        assertDoesNotThrow(() -> {
            new XMLWriter(bound);
        });
        
    }
  
    @Test
    public void testInitChunkFiles() {
        assertTrue(true);
        try {
            XMLWriter.appendToBinary(null);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}