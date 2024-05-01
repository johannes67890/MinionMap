package parser;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import java.io.File;
import parser.XMLReader;
import util.FileDistributer;
import util.MecatorProjection;

public class TagTest {

    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> {
            new XMLReader(FileDistributer.testMap.getFilePath());
        });       
    }

    @AfterAll
    public static void tearDown() {
        final String directoryPath = "src/main/resources/chunks/";
        File directory = new File(directoryPath);
        for (File file : directory.listFiles()) {
            file.delete();
        }
    }
    @Test
    public void testIsInBounds(){
        TagBound bound = XMLReader.getBound();

        // testBound0 - A bound where one of the corners is in bounds
        TagBound testBound0 = MecatorProjection.project(new TagBound(55.65409f, 55.65733f, 12.47029f, 12.47875f));
        assertTrue(testBound0.isInBounds(bound));
        // testBound1 - A bound where all corners are larger that the bounds
        TagBound testBound1 = MecatorProjection.project(new TagBound(55.6555f, 55.6592f, 12.4657f, 12.4749f));
        assertTrue(testBound1.isInBounds(bound));
        // TestBound2 - A bound where all corners are smaller that the bounds
        TagBound testBound2 = MecatorProjection.project(new TagBound(55.65667f, 55.65719f, 12.47077f, 12.47201f));
        assertTrue(testBound2.isInBounds(bound));
        // TestBound3 - A bound where all corners are outside the bounds
        TagBound testBound3 = MecatorProjection.project(new TagBound(55.65601f, 55.65822f, 12.47536f, 12.47887f));
        assertFalse(testBound3.isInBounds(bound));

        /**
         * Address
         */
        assertTrue(XMLReader.getAddressById(1447912431l).isInBounds(bound));

        /*
         *  Way - first 2 nodes are not in bounds, but last nodes are
         */
        TagWay way = XMLReader.getWayById(26154396l);

        for (int i = 0; i < way.getRefNodes().size(); i++) { 
            // Check if the first 8 nodes are not in bounds, but the last 2 nodes are
            if(i <= 1){
                assertFalse(way.getRefNodes().get(i).isInBounds(bound));
            }
            else{
                assertTrue(way.getRefNodes().get(i).isInBounds(bound));
            }
        }
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
