package pathfinding;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import edu.princeton.cs.algs4.Point2D;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import parser.TagAddress;
import parser.TagWay;
import parser.Type;
import parser.XMLReader;
import pathfinding.Dijsktra;
import util.FileDistributer;
import util.Tree;
import parser.Tag;
import parser.TagAddress;

public class DijstraTest {
    private XMLReader reader;
    @BeforeEach
    void testXMLReaderStartup() {
        this.reader = new XMLReader("src/test/java/org/ressources/testMap.osm");
        assertNotNull(reader);
        assertDoesNotThrow(() -> this.reader);
        Tree.initialize(new ArrayList<Tag>(XMLReader.getWays().valueCollection()));;
    }

    @Test
    void testGetNearestRoad(){
        TagAddress start = XMLReader.getAddressById(1447913335l);
        Tree.insertTagInTree(start);
        ArrayList<Tag> road = Tree.getNearestOfType(start, Arrays.asList(Type.RESIDENTIAL_ROAD));
        assertNotNull(road);
        assertTrue(road.get(0) instanceof TagWay);
        assertEquals(27806594, road.get(0).getId());

        road = Tree.getNearestOfType(start, Type.getAllRoads());
        assertNotNull(road);
        assertTrue(road.get(0) instanceof TagWay);
        assertEquals(27806594, road.get(0).getId());
    }

    @Test
    void testAdj(){
        TagWay way = XMLReader.getWayById(27806594l);
        assertNotNull(way);
        // Tree.insertTagInTree(way);
        // assertEquals(2, adj.size());
        // assertEquals(26154395, adj.get(0).getId());
        // assertEquals(27806594, adj.get(1).getId());
    }
}
