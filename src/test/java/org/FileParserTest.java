package org;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.parser.FileDistributer;
import org.parser.FileParser;
import java.util.HashMap;
import org.parser.OSMReader;
import org.parser.Tag;
import org.parser.TagBound;
import org.parser.TagNode;

import java.math.BigDecimal;
public class FileParserTest {
    private FileDistributer testFile = FileDistributer.input;
    private FileParser parser;
    private OSMReader reader;

    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> {
            this.reader = new OSMReader(this.testFile);
        });
        this.parser = new FileParser(this.reader);
    }

    @Test
    public void testCenterPoint() {
        TagBound bound = this.reader.getBound();
        TagNode expectedCenterPoint = new TagNode(new HashMap<Tags.Node, Number>() {{
            put(Tags.Node.LAT, new BigDecimal("55.6572100"));
            put(Tags.Node.LON, new BigDecimal("12.4705650"));
        }});

        TagNode centerPoint = parser.centerPoint(bound);

        assertEquals(centerPoint.getLat(), expectedCenterPoint.getLat());
        assertEquals(centerPoint.getLon(), expectedCenterPoint.getLon());
    }

    @Test
    public void testSplitArea() {
        TagBound bound = this.reader.getBound();
        parser.splitArea(bound);

        // Q1 - top left area
        TagBound expectedQ1 = new TagBound(new HashMap<Tags.Bounds, BigDecimal>() {{
            put(Tags.Bounds.MINLAT, new BigDecimal("55.6572100"));
            put(Tags.Bounds.MAXLAT, new BigDecimal("55.6572100"));
            put(Tags.Bounds.MINLON, new BigDecimal("12.4705650"));
            put(Tags.Bounds.MAXLON, new BigDecimal("12.4705650"));
        }});

        
        // Q2 - top right area
        // Q3 - bottom left area
        // Q4 - bottom right area

    }
}
