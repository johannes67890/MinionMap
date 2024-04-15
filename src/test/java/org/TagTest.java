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
        // Node with id 5660536753 is in bounds and address with id 340826416 is in bounds
        assertTrue(XMLReader.getNodeById(5660536753l).isInBounds(bound));
        assertTrue(XMLReader.getAddressById(340826416l).isInBounds(bound));
        // Way with id 32225595 is in bounds, but node with id 7888377655 is not
        assertTrue(XMLReader.getWayById(32225595l).isInBounds(bound));
        assertFalse(XMLReader.getNodeById(7888377655l).isInBounds(bound));
        // Address with id 340826416 is in bounds, but node from address with id 5275255096 is not
        assertTrue(XMLReader.getAddressById(340826416l).isInBounds(bound));
        assertFalse(XMLReader.getNodeById(5275255096l).isInBounds(bound));
    }
}
