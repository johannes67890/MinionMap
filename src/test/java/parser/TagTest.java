package parser;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.XMLReader;
import util.MecatorProjection;

public class TagTest {
    private XMLReader reader;

    @BeforeEach
    void testXMLReaderStartup() {
        this.reader = new XMLReader("src/test/java/org/ressources/testMap.osm");
        assertNotNull(reader);
        assertDoesNotThrow(() -> this.reader);
    }
    @Test
    public void testIsInBounds(){
        TagBound bound = XMLReader.getBound();
        /**
         * Address
         */
        assertTrue(XMLReader.getAddressById(1447912431l).isInBounds(bound));

        /*
         *  Way - first 2 nodes are not in bounds, but last nodes are
         */
        TagWay way = XMLReader.getWayById(26154396l);
        assertTrue(way.isInBounds(bound));
        for (int i = 0; i < way.getRefNodes().size(); i++) { 
            // Check if the first 8 nodes are not in bounds, but the last 2 nodes are
            if(i <= 1){
                assertFalse(way.getRefNodes().get(i).isInBounds(bound));
            }
            else{
                assertTrue(way.getRefNodes().get(i).isInBounds(bound));
            }
        }

        /**
         * Relations
         */
        TagRelation relation = XMLReader.getRelationById(10343795l);
        // assertTrue(relation.isInBounds(bound));

    }
    @Test
    public void testDistance(){
        TagNode a0 = MecatorProjection.project(new TagNode(0, 55.6572603f, 12.4682648f));
        TagNode a1 = MecatorProjection.project(new TagNode(1, 55.6572610f, 12.4685185f));

        assertEquals(15.93, a0.distance(a1), 0.1);
    }
    
    @Test
    public void testGetParent(){
        // Test of node with no intersections
        TagNode node = XMLReader.getNodeById(3711179682l);
        assertNotNull(node);
        assertEquals(XMLReader.getWayById(27806594l), node.getParent());
    }
}
