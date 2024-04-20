package org;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.FileNotFoundException;


import org.junit.jupiter.api.*;
import org.opentest4j.AssertionFailedError;
import util.MecatorProjection;
import parser.TagAddress;
import parser.TagBound;
import parser.XMLReader;

public class XMLReaderTest {
    private XMLReader reader;

    @BeforeEach
    void testXMLReaderStartup() {
        new XMLReader("src/test/java/org/ressources/testMap.osm");
        
    }
    
    @Test
    void testCount(){
        //assertEquals(4, XMLReader.getBound().size());
        // assertEquals(391, XMLReader.getNodes().valueCollection().size());
        assertEquals(21, XMLReader.getAddresses().size());
        assertEquals(1, XMLReader.getRelations().size());
        assertEquals(50, XMLReader.getWays().size());
    }


    @Test
    void testSetBounds() {
        // Bounds in file: <bounds minlat="55.4411300" minlon="12.1600100" maxlat="55.4421100" maxlon="12.1631900"/>
        
        assertInstanceOf(TagBound.class, XMLReader.getBound());
        assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMinLat(), 55.4411300f);
        assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMinLon(), 12.1600100f);
        assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMaxLat(), 55.4421100f);
        assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMaxLon(), 12.1631900f);
    }

    @Test
    void getTagByIdTest() {
        // Address with id 340820448 in file:
        // TODO: Should we be able to get individual nodes by id?
        // assertNotNull(XMLReader.getAddressById(340820448l));
        // assertNull(XMLReader.getNodeById(340820448l));
        // assertNull(XMLReader.getWayById(340820448l));
        // assertNull(XMLReader.getRelationById(340820448l));

        // Tags
        TagAddress address = XMLReader.getAddressById(340820448l);
        assertEquals("Køge", address.getCity());
        assertEquals("Køge", address.getMunicipality());
        assertEquals("4600", address.getPostcode());
        assertEquals("Bellmansvej", address.getStreet());
        assertEquals("1", address.getHouseNumber());
        assertEquals("DK", address.getCountry());
    }
}


