package org;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import javax.xml.stream.events.XMLEvent;

import org.junit.jupiter.api.*;
import org.parser.FileDistributer;
import org.parser.XMLReader;

public class XMLReaderTest {
    public XMLReader reader;
    public XMLEvent event;
    @BeforeEach
    void setUp() {
        this.reader = new XMLReader(FileDistributer.test_input);
        this.event = reader.getEvent();
    }

    @Test
    void testSetBounds() {
        HashMap<Tags.Bounds, Float> bounds = new HashMap<Tags.Bounds, Float>(){
            {
                put(Tags.Bounds.MINLAT, 55.0000000f);
                put(Tags.Bounds.MAXLAT, 55.0000000f);
                put(Tags.Bounds.MINLON, 12.0000000f);
                put(Tags.Bounds.MAXLON, 12.0000000f);
            }
        };
        // assertEquals(bounds, reader.getBounds());
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


