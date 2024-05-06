package pathfinding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.TagNode;

public class DigraphTest {
    private Digraph G = new Digraph();

    TagNode node0 = new TagNode(0, 0f, 0f);
    TagNode node1 = new TagNode(1, 0f, 1f);
    TagNode node2 = new TagNode(2, 1f, 1f);
    TagNode node3 = new TagNode(3, 3f, 2f);
    TagNode node4 = new TagNode(4, 4f, 0f);

    DirectedEdge edge0 = new DirectedEdge(node0, node1, 1f);
    DirectedEdge edge1 = new DirectedEdge(node1, node2, 1f);
    DirectedEdge edge2 = new DirectedEdge(node2, node3, 1f);
    DirectedEdge edge3 = new DirectedEdge(node0, node2, 1f);
    DirectedEdge edge4 = new DirectedEdge(node4, node1, 1f);

    @BeforeEach
    void setUp(){
        G.addEdge(edge0);
        G.addEdge(edge1);
        G.addEdge(edge2);
        G.addEdge(edge3);
        G.addEdge(edge4);
    }

    @Test
    void TestLoopEdges(){
        assertThrows(IllegalArgumentException.class, () -> 
        {G.addEdge(new DirectedEdge(node0, node0, 0d));}, 
        "Expected IllegalArgumentException to be thrown. Loop edges are not allowed.");
    }

    @Test
    void verticesTest(){
        // Get nodes
        assertEquals(0, G.getNode(0).getId());
        assertEquals(1, G.getNode(1).getId());
        assertEquals(2, G.getNode(2).getId());
        assertEquals(3, G.getNode(3).getId());
        assertEquals(4, G.getNode(4).getId());

        // Get adjacents
        for (DirectedEdge adjEdge : G.adj(node1)) {
            assertTrue(adjEdge == edge0 || adjEdge == edge1 || adjEdge == edge4);
        }
        for (DirectedEdge adjEdge : G.adj(node4)) {
            assertTrue(adjEdge == edge4);
        }
    }

    @Test
    void getDegreeTest(){
        assertEquals(2, G.outdegree(node0));
        assertEquals(2, G.indegree(node2));
        assertEquals(1, G.outdegree(node1));
    }

    @Test
    void addEdgeTest(){
        assertEquals(5, G.V());
        assertEquals(5, G.E());
    }
}
