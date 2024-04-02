package org;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import javax.xml.stream.events.XMLEvent;

import org.junit.jupiter.api.*;

import parser.XMLReader;
import util.FileDistributer;

public class XMLReaderTest {
    public XMLReader reader;
    public XMLEvent event;
    @BeforeEach
    void setUp() {
        // this.reader = new XMLReader(FileDistributer.test_input);
    }

    @Test
    void testSetBounds() {
        //TODO: 
    }

    // TODO: improve this test
    // @Test
    // void testIsTag() {
    //     if(event.isStartElement()){
    //        String tag = event.asStartElement().getName().getLocalPart();
    //        assertEquals(reader.isTag(event, tag), true);
    //     }
    // }
    
}


