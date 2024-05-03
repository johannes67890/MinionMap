package pathfinding;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import parser.TagNode;
import pathfinding.DirectedEdge;

public class EdgeTest {
    TagNode node0 = new TagNode(0, 0f, 0f);
    TagNode node1 = new TagNode(1, 0f, 1f);
    TagNode node2 = new TagNode(2, 1f, 1f);
    TagNode node3 = new TagNode(3, 3f, 2f);
    TagNode node4 = new TagNode(4, 4f, 0f);
    
    DirectedEdge edge0 = new DirectedEdge(node0, node1, 1d);
    DirectedEdge edge1 = new DirectedEdge(node1, node2, 1d);
    DirectedEdge edge2 = new DirectedEdge(node2, node3, 1d);
    DirectedEdge edge3 = new DirectedEdge(node0, node2, 1d);

    @Test
    void IlligalEdgesTest(){
        assertThrows(IllegalArgumentException.class,() -> new DirectedEdge(null, null, Double.NaN)); // NaN
        assertThrows(IllegalArgumentException.class,() -> new DirectedEdge(node0, null, 1d));
    }
}
