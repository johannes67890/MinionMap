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
import parser.XMLReader;
import pathfinding.Dijkstra;
import structures.KDTree.Tree;
import util.FileDistributer;
import util.FileHandler;
import util.TransportType;
import util.Type;
import parser.Tag;
import parser.TagAddress;

public class DijkstraTest {
    private XMLReader reader;
    private Dijkstra d;
    @BeforeEach
    void testXMLReaderStartup() {
        reader = new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
        assertNotNull(reader);
        assertDoesNotThrow(() -> this.reader);
        Tree.initializeTree();
        for (TagWay way : reader.getWays().valueCollection()){
            Tree.insertTagWayInTree(way);
        }

        d = new Dijkstra(reader.getNodeById(3711179713l), reader.getNodeById(7798538748l), TransportType.CAR, true);
    }


    @Test
    void testDjikstraInit(){
        
        assertNotNull(d);
        Digraph g = d.getGraph();
        assertNotNull(g);
        assertEquals(36, g.V());
        assertEquals(73, g.E());
    }

    @Test
    void testHasPathTo(){
        assertTrue(d.hasPathTo(reader.getNodeById(7798538748l)));
    }

    @Test
    void testPathTo(){
        assertEquals(3, d.shortestPath().size());
        assertEquals(6, d.shortestPathDetailed().size());
        assertEquals(5, d.allVisitedPaths().size());
    }

    @Test
    void testTravelTimeDistance(){
        assertEquals("105.33m", d.getDistanceOfPath());
        assertEquals("0.16min", d.getMinutesOfPath());
    }

    @Test
    void testPrintPath(){
        assertEquals(2, d.printPath().size());
    }

    
}
