package org;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import parser.TagBound;
import parser.XMLReader;
import parser.XMLWriter;
import util.FileDistributer;

public class XMLWriterTest {
    XMLWriter writer;
    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> {
            new XMLReader(FileDistributer.input.getFilePath());
        });
    }
    @Test
    public void testWrite() {
        this.writer = new XMLWriter(XMLReader.getBound());
            
    }
}
