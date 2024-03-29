package org;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.parser.FileDistributer;
import org.parser.FileParser;
import org.parser.Tag;

import java.util.HashMap;
import org.parser.XMLReader;
import org.parser.TagBound;
import org.parser.TagNode;
import org.parser.FileParser.Chunck;
import org.parser.FileParser.CompasPoints;

import java.math.BigDecimal;
public class FileParserTest {
    
    private FileDistributer testFile = FileDistributer.input;
    private FileParser FileParser;
    private XMLReader reader;

    private Chunck chunck;
    private TagNode centerPoint;

    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> {
            this.reader = new XMLReader(this.testFile);
        });
        this.FileParser = new FileParser(this.reader.getBound());
        this.chunck = this.FileParser.getChunck();
        this.centerPoint = this.FileParser.centerPoint(this.reader.getBound());

    }


    @Test
    public void testCenterPoint() {
        TagBound bound = this.reader.getBound();

        TagNode expectedCenterPoint = new TagNode(new BigDecimal("55.6572100"), new BigDecimal("12.4705650"));
    
        TagNode centerPoint = FileParser.centerPoint(bound);

        assertEquals(centerPoint.getLat(), expectedCenterPoint.getLat());
        assertEquals(centerPoint.getLon(), expectedCenterPoint.getLon());
    }

    // TODO: Refactor test so that they calculate expected values based on their respective equation?
    @Test
    public void TestCenterPointsOfBound(){
        TagBound bound = this.reader.getBound();
        TagNode centerPoint = FileParser.centerPoint(bound);
        
        CompasPoints points = new CompasPoints(centerPoint, bound);
        
        // North
        assertEquals(points.getNorth().getLat(), bound.getMaxLat());
        assertEquals(points.getNorth().getLon(), new BigDecimal("12.4705650"));
        // South
        assertEquals(points.getSouth().getLat(), bound.getMinLat());
        assertEquals(points.getSouth().getLon(), new BigDecimal("12.4705650"));
        // East
        assertEquals(points.getEast().getLat(), new BigDecimal("55.6572100"));
        assertEquals(points.getEast().getLon(), bound.getMaxLon());
        // West
        assertEquals(points.getWest().getLat(), new BigDecimal("55.6572100"));
        assertEquals(points.getWest().getLon(), bound.getMinLon());
    }

    @Test
    public void testIsInBounds(){
        TagNode node = new TagNode(new BigDecimal("55.6581162"),new BigDecimal("12.4681259"));

        assertTrue(TagNode.isInBounds(node, FileParser.getChunck().getQuadrantOne()));
        assertFalse(TagNode.isInBounds(node, FileParser.getChunck().getQuadrantTwo()));
        assertFalse(TagNode.isInBounds(node, FileParser.getChunck().getQuadrantThree()));
        assertFalse(TagNode.isInBounds(node, FileParser.getChunck().getQuadrantFour()));
    }

    
    @Test
    public void testSplitArea() {     

        TagBound expectedQ1 = new TagBound(
        new BigDecimal("55.6581600"), // MaxLat
        new BigDecimal("55.6572100"), // MinLat
        new BigDecimal("12.4705650"), // MaxLon
        new BigDecimal("12.4677300")  // MinLon
        );
        
        TagBound expectedQ2 = new TagBound(
        new BigDecimal("55.6581600"), // MaxLat
        new BigDecimal("55.6572100"), // MinLat
        new BigDecimal("12.4734000"), // MaxLon 
        new BigDecimal("12.4705650")  // MinLon
        );
        
        TagBound expectedQ3 = new TagBound(
        new BigDecimal("55.6572100"), // MaxLat
        new BigDecimal("55.6562600"), // MinLat
        new BigDecimal("12.4705650"), // MaxLon
        new BigDecimal("12.4677300")  // MinLon
        );

        TagBound expectedQ4 = new TagBound(
        new BigDecimal("55.6572100"), // MaxLat
        new BigDecimal("55.6562600"), // MinLat
        new BigDecimal("12.4734000"), // MaxLon
        new BigDecimal("12.4705650")  // MinLon
        );


        assertEquals(expectedQ1, this.chunck.getQuadrantOne());
        assertEquals(expectedQ2, this.chunck.getQuadrantTwo());
        assertEquals(expectedQ3, this.chunck.getQuadrantThree());
        assertEquals(expectedQ4, this.chunck.getQuadrantFour());
        
        for (int i = 0; i < 4; i++) {
            BigDecimal minLat = this.chunck.getQuadrant(i).getMinLat();
            BigDecimal maxLat = this.chunck.getQuadrant(i).getMaxLat();
            BigDecimal minLon = this.chunck.getQuadrant(i).getMinLon();
            BigDecimal maxLon = this.chunck.getQuadrant(i).getMaxLon();

            assertTrue(maxLat.compareTo(minLat) == 1);
            assertTrue(maxLon.compareTo(minLon) == 1);   
        }
    }
}
