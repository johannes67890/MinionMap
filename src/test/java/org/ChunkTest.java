package org;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.FileDistributer;
import parser.Chunk;
import parser.XMLReader;
import parser.TagBound;
import parser.TagNode;
import parser.Chunk.CompasPoints;

public class ChunkTest {
    private Chunk chunck;

    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> {
            new XMLReader(FileDistributer.input.getFilePath());
        });
        this.chunck = new Chunk(new XMLReader(FileDistributer.input.getFilePath()).getBound());
    }


    @Test
    public void testCenterPoint() {
        TagNode expectedCenterPoint = new TagNode(55.6572100d, 12.4705650d);
    
        TagNode centerPoint = Chunk.centerPoint(XMLReader.getBound());

        assertEquals(centerPoint.getLat(), expectedCenterPoint.getLat());
        assertEquals(centerPoint.getLon(), expectedCenterPoint.getLon());
    }

    // TODO: Refactor test so that they calculate expected values based on their respective equation?
    @Test
    public void TestCenterPointsOfBound(){
        TagBound bound = XMLReader.getBound();
        TagNode centerPoint = Chunk.centerPoint(XMLReader.getBound());
        
        CompasPoints points = new CompasPoints(centerPoint, bound);
        
        // North
        assertEquals(points.getNorth().getLat(), bound.getMaxLat());
        assertEquals(points.getNorth().getLon(), 12.4705650d);
        // South
        assertEquals(points.getSouth().getLat(), bound.getMinLat());
        assertEquals(points.getSouth().getLon(), 12.4705650d);
        // East
        assertEquals(points.getEast().getLat(), 55.6572100d);
        assertEquals(points.getEast().getLon(), bound.getMaxLon());
        // West
        assertEquals(points.getWest().getLat(), 55.6572100d);
        assertEquals(points.getWest().getLon(), bound.getMinLon());
    }

    @Test
    public void testIsInBounds(){
        TagNode node = new TagNode(55.6581162d,12.4681259d);

        assertTrue(node.isInBounds(chunck.getQuadrantOne()));
        assertFalse(node.isInBounds(chunck.getQuadrantTwo()));
        assertFalse(node.isInBounds(chunck.getQuadrantThree()));
        assertFalse(node.isInBounds(chunck.getQuadrantFour()));
    }
    
    @Test
    public void testSplitArea() {     

        TagBound expectedQ1 = new TagBound(
        55.6572100d, 
        55.6581600d, 
        12.4677300d,  
        12.4705650d 
        );
        
        TagBound expectedQ2 = new TagBound(
        55.6572100d, 
        55.6581600d, 
        12.4705650d,  
        12.4734000d 
        );
        
        TagBound expectedQ3 = new TagBound(
        55.6562600d, 
        55.6572100d, 
        12.4677300d, 
        12.4705650d
        );

        TagBound expectedQ4 = new TagBound(
        55.6562600d, 
        55.6572100d, 
        12.4705650d,  
        12.4734000d 
        );


        assertEquals(expectedQ1, this.chunck.getQuadrantOne());
        assertEquals(expectedQ2, this.chunck.getQuadrantTwo());
        assertEquals(expectedQ3, this.chunck.getQuadrantThree());
        assertEquals(expectedQ4, this.chunck.getQuadrantFour());
        
        for (int i = 0; i < 4; i++) {
            double minLat = this.chunck.getQuadrant(i).getMinLat();
            double maxLat = this.chunck.getQuadrant(i).getMaxLat();
            double minLon = this.chunck.getQuadrant(i).getMinLon();
            double maxLon = this.chunck.getQuadrant(i).getMaxLon();

            assertTrue(maxLat > minLat);
            assertTrue(maxLon > minLon);   
        }
    }
}
