package pathfinding;

import static org.junit.Assert.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import parser.TagNode;

public class EdgeTest {

    private TagNode node0;
    private TagNode node1;
    private TagNode node2;
    private TagNode node3;

    private DirectedEdge edge0;
    private DirectedEdge edge1;
    private DirectedEdge edge2;
    private DirectedEdge edge3;
    

    @BeforeEach
    void setUp(){
        node0 = new TagNode(0, 0f, 0f);
        node1 = new TagNode(1, 0f, 1f);
        node2 = new TagNode(2, 1f, 1f);
        node3 = new TagNode(3, 3f, 2f);
        
        edge0 = new DirectedEdge(node0, node1, 1d);
        edge1 = new DirectedEdge(node1, node2, 1d);
        edge2 = new DirectedEdge(node2, node3, 1d);
        edge3 = new DirectedEdge(node0, node2, 1d);
    }


    @Test
    void IllegalEdgesTest(){
        assertThrows(IllegalArgumentException.class,() -> new DirectedEdge(null, null, Double.NaN)); // NaN
        assertThrows(IllegalArgumentException.class,() -> new DirectedEdge(node0, node0, 1d));
    }

    @Test
    void initEdgeTest(){
        assertThrows(IllegalArgumentException.class,() -> new DirectedEdge(node0, node0, 1d));
        assertThrows(IllegalArgumentException.class,() -> new DirectedEdge(node0, node1, Double.NaN));
    }

    @Test
    void getWeightTest(){
        assertTrue(edge0.weight() == 1d);
        assertTrue(edge1.weight() == 1d);
        assertTrue(edge2.weight() == 1d);
        assertTrue(edge3.weight() == 1d);
    }

    @Test
    void getFromTest(){
        assertEquals(node0, edge0.from());
        assertEquals(node1, edge1.from());
        assertEquals(node2, edge2.from());
        assertEquals(node0, edge3.from());
    }
}
