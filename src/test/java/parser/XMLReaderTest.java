// package parser;

// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertInstanceOf;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import java.io.FileNotFoundException;


// import org.junit.jupiter.api.*;
// import org.opentest4j.AssertionFailedError;

// import util.FileDistributer;
// import parser.TagAddress;
// import parser.TagBound;
// import parser.TagNode;
// import parser.TagWay;
// import parser.XMLReader;

// public class XMLReaderTest {
//     private XMLReader reader;

//     @BeforeEach
//     void testXMLReaderStartup() {
//         this.reader = new XMLReader(FileDistributer.testMap.getFilePath());
//         assertNotNull(reader);
//         assertDoesNotThrow(() -> this.reader);
//     }
    
//     @Test
//     void testCount(){
//         //assertEquals(4, XMLReader.getBound().size());
//         // assertEquals(391, XMLReader.getNodes().valueCollection().size());
//         // assertEquals(21, XMLReader.getAddresses().size());
//         // assertEquals(1, XMLReader.getRelations().size());
//         // assertEquals(50, XMLReader.getWays().size());
//     }


//     @Test
//     void testSetBounds() {
//         // Bounds in file: <bounds minlat="55.4411300" minlon="12.1600100" maxlat="55.4421100" maxlon="12.1631900"/>
        
//         // assertInstanceOf(TagBound.class, XMLReader.getBound());
//         // assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMinLat(), 55.4411300f);
//         // assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMinLon(), 12.1600100f);
//         // assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMaxLat(), 55.4421100f);
//         // assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMaxLon(), 12.1631900f);
//     }

//     @Test
//     void getTagByIdTest() {
//         TagWay way = XMLReader.getWayById(27806594l);
//         assertNotNull(way);
//         assertTrue(way.getRefNodes().size() == 28);
//         for (TagNode n : way.getRefNodes()) {
//             assertNotNull(n);
//             if(n.equals(way.getRefNodes().getLast()) && n.getNext() == null) break;
//         }

//         assertTrue(way.getRefNodes().getFirst().getId() == 286405308l);
//         assertTrue(way.getRefNodes().getLast().getId() == 21816077l);

//     }
// }


