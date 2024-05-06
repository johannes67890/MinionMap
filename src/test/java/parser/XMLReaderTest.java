package parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.FileDistributer;
import util.FileHandler;

public class XMLReaderTest {
    private XMLReader reader;

    @BeforeEach
    void testXMLReaderStartup() {
        synchronized(this) {
            reader = new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
            assertNotNull(reader);
            assertDoesNotThrow(() -> this.reader);
        }
    }
    
    @Test
    void testCount(){
        //assertEquals(4, XMLReader.getBound().size());
        assertEquals(1235, XMLReader.getNodes().valueCollection().size());
        assertEquals(46, XMLReader.getAddresses().size());
        assertEquals(7, XMLReader.getRelations().size());
        assertEquals(211, XMLReader.getWays().size());
    }


    @Test
    void testSetBounds() {
        //Bounds in file:   <bounds minlat="55.6562600" minlon="12.4677300" maxlat="55.6581600" maxlon="12.4734000"/>
        
        assertInstanceOf(TagBound.class, XMLReader.getBound());
        assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMinLat(), 55.6562600f);
        assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMinLon(), 12.467729f);
        assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMaxLat(), 55.6581600f);
        assertEquals(MecatorProjection.unproject(XMLReader.getBound()).getMaxLon(), 12.4734000f);
    }

    @Test
    void getTagByIdTest() {
        TagWay way = XMLReader.getWayById(27806594l);
        assertNotNull(way);
        assertTrue(way.getRefNodes().size() == 28);
        for (TagNode n : way.getRefNodes()) {
            assertNotNull(n);
            if(n.equals(way.getRefNodes().getLast()) && n.getNext() == null) break;
        }

        assertTrue(way.getRefNodes().getFirst().getId() == 286405308l);
        assertTrue(way.getRefNodes().getLast().getId() == 21816077l);

    }
}


