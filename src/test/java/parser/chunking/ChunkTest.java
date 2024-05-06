package parser.chunking;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Tag;

import util.FileDistributer;
import parser.XMLReader;
import parser.chunking.Chunk;
import parser.chunking.Chunk.CompasPoints;
import parser.TagBound;
import parser.TagNode;

import java.io.File;

public class ChunkTest {
    private Chunk chunck;

    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> {
            new XMLReader(FileDistributer.testMap.getFilePath());
        });       
        chunck = new Chunk(XMLReader.getBound()); 
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
    public void testCenterPoint() {
        TagNode expectedCenterPoint = MecatorProjection.project(new TagNode(55.6572100f, 12.4705640f));
    
        TagNode centerPoint = Chunk.centerPoint(XMLReader.getBound());

        assertEquals(centerPoint.getLat(), expectedCenterPoint.getLat(), 1f);
        assertEquals(centerPoint.getLon(), expectedCenterPoint.getLon(), 1f);
    }

    // TODO: Refactor test so that they calculate expected values based on their respective equation?
    @Test
    public void TestCenterPointsOfBound(){
        TagBound bound = XMLReader.getBound();
        TagNode centerPoint = Chunk.centerPoint(XMLReader.getBound());
        
        CompasPoints points = new CompasPoints(centerPoint, bound);
        
        // North
        assertEquals(points.getNorth().getLat(), bound.getMaxLat());
        assertEquals(points.getNorth().getLon(), MecatorProjection.projectLon(12.4705650f), 1f);
        // South
        assertEquals(points.getSouth().getLat(), bound.getMinLat());
        assertEquals(points.getSouth().getLon(), MecatorProjection.projectLon(12.4705650f), 1f);
        // East
        assertEquals(points.getEast().getLat(), MecatorProjection.projectLat(55.6572100f), 1f);
        assertEquals(points.getEast().getLon(), bound.getMaxLon());
        // West
        assertEquals(points.getWest().getLat(), MecatorProjection.projectLat(55.6572100f), 1f);
        assertEquals(points.getWest().getLon(), bound.getMinLon());
    }

    @Test
    public void testIsInBounds(){
        TagNode node = MecatorProjection.project(new TagNode(55.6581162f,12.4681259f));
        
        assertTrue(node.isInBounds(chunck.getQuadrantOne()));
        assertFalse(node.isInBounds(chunck.getQuadrantTwo()));
        assertFalse(node.isInBounds(chunck.getQuadrantThree()));
        assertFalse(node.isInBounds(chunck.getQuadrantFour()));

        TagNode node2 = MecatorProjection.project(new TagNode(55.6581520f,12.4679991f));
        
        assertTrue(node2.isInBounds(chunck.getQuadrantOne()));
        assertFalse(node2.isInBounds(chunck.getQuadrantTwo()));
        assertFalse(node2.isInBounds(chunck.getQuadrantThree()));
        assertFalse(node2.isInBounds(chunck.getQuadrantFour()));
    }

    @Test
    public void testBoundPos(){
        for (int i = 0; i < 4; i++) {
            float minLat = this.chunck.getQuadrant(i).getMinLat();
            float maxLat = this.chunck.getQuadrant(i).getMaxLat();
            float minLon = this.chunck.getQuadrant(i).getMinLon();
            float maxLon = this.chunck.getQuadrant(i).getMaxLon();

            assertTrue(Float.compare(minLat, maxLat) < 0, "Did not pass for Quadrant: " + (i+1));   
            assertTrue(Float.compare(minLon, maxLon) < 0, "Did not pass for Quadrant: " + (i+1));
        }
    }
    
    @Test
    public void testSplitArea() {     

        TagBound expectedQ1 = new TagBound(
        55.6572150f, 
        55.6581600f, 
        12.4677290f,  
        12.4705660f 
        );
        
        TagBound expectedQ2 = new TagBound(
        55.6572150f, 
        55.6581600f, 
        12.4705660f,  
        12.4734000f 
        );
        
        TagBound expectedQ3 = new TagBound(
        55.6562600f, 
        55.6572150f, 
        12.4677290f, 
        12.4705660f
        );

        TagBound expectedQ4 = new TagBound(
        55.6562600f, 
        55.6572150f, 
        12.4705660f,  
        12.4734000f 
        );


        assertEquals(expectedQ1, MecatorProjection.unproject(this.chunck.getQuadrantOne()));
        assertEquals(expectedQ2, MecatorProjection.unproject(this.chunck.getQuadrantTwo()));
        assertEquals(expectedQ3, MecatorProjection.unproject(this.chunck.getQuadrantThree()));
        assertEquals(expectedQ4, MecatorProjection.unproject(this.chunck.getQuadrantFour()));
    
    }
}