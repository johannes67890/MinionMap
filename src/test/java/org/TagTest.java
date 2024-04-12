package org;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import parser.TagBound;
import parser.XMLReader;

public class TagTest {
    private XMLReader reader;

    @BeforeEach
    void testXMLReaderStartup() {
        this.reader = new XMLReader("src/test/java/org/ressources/map.osm");
        assertNotNull(reader);
        assertDoesNotThrow(() -> this.reader);
    }
    @Test
    public void testIsInBounds(){
        TagBound bound = XMLReader.getBound();
        
        assertTrue(XMLReader.getNodeById(5660536753l).isInBounds(bound));
        assertTrue(XMLReader.getAddressById(340826416l).isInBounds(bound));
    }
}
