package org;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.reporting.shadow.org.opentest4j.reporting.events.core.Tag;

import util.FileDistributer;
import util.MecatorProjection;
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
        TagNode expectedCenterPoint = MecatorProjection.project(new TagNode(55.6572100f, 12.4705640f));
    
        TagNode centerPoint = Chunk.centerPoint(XMLReader.getBound());

        assertEquals(centerPoint.getLat(), expectedCenterPoint.getLat(), 0.0001f);
        assertEquals(centerPoint.getLon(), expectedCenterPoint.getLon(), 0.0001f);
    }

    // TODO: Refactor test so that they calculate expected values based on their respective equation?
    @Test
    public void TestCenterPointsOfBound(){
        TagBound bound = XMLReader.getBound();
        TagNode centerPoint = Chunk.centerPoint(XMLReader.getBound());
        
        CompasPoints points = new CompasPoints(centerPoint, bound);
        
        // North
        assertEquals(points.getNorth().getLat(), bound.getMaxLat());
        assertEquals(points.getNorth().getLon(), MecatorProjection.projectLon(12.4705650f), 0.001f);
        // South
        assertEquals(points.getSouth().getLat(), bound.getMinLat());
        assertEquals(points.getSouth().getLon(), MecatorProjection.projectLon(12.4705650f), 0.001f);
        // East
        assertEquals(points.getEast().getLat(), MecatorProjection.projectLat(55.6572100f), 0.001f);
        assertEquals(points.getEast().getLon(), bound.getMaxLon());
        // West
        assertEquals(points.getWest().getLat(), MecatorProjection.projectLat(55.6572100f), 0.001f);
        assertEquals(points.getWest().getLon(), bound.getMinLon());
    }

    @Test
    public void testIsInBounds(){
        TagNode node = MecatorProjection.project(new TagNode(55.6581162f,12.4681259f));
        TagBound bound = XMLReader.getBound();
        TagBound q1 = this.chunck.getQuadrantOne();
        TagBound q2 = this.chunck.getQuadrantTwo();
        TagBound q3 = this.chunck.getQuadrantThree();
        TagBound q4 = this.chunck.getQuadrantFour();

        assertTrue(node.isInBounds(chunck.getQuadrantOne()));
        assertFalse(node.isInBounds(chunck.getQuadrantTwo()));
        assertFalse(node.isInBounds(chunck.getQuadrantThree()));
        assertFalse(node.isInBounds(chunck.getQuadrantFour()));
    }
    
    @Test
    public void testSplitArea() {     

        TagBound expectedQ1 = new TagBound(
        55.6572100f, 
        55.6581600f, 
        12.4677300f,  
        12.4705650f 
        );
        
        TagBound expectedQ2 = new TagBound(
        55.6572100f, 
        55.6581600f, 
        12.4705650f,  
        12.4734000f 
        );
        
        TagBound expectedQ3 = new TagBound(
        55.6562600f, 
        55.6572100f, 
        12.4677300f, 
        12.4705650f
        );

        TagBound expectedQ4 = new TagBound(
        55.6562600f, 
        55.6572100f, 
        12.4705650f,  
        12.4734000f 
        );


        assertEquals(expectedQ1, MecatorProjection.unproject(this.chunck.getQuadrantOne()));
        assertEquals(expectedQ2, MecatorProjection.unproject(this.chunck.getQuadrantTwo()));
        assertEquals(expectedQ3, MecatorProjection.unproject(this.chunck.getQuadrantThree()));
        assertEquals(expectedQ4, MecatorProjection.unproject(this.chunck.getQuadrantFour()));
        
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