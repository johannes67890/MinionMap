package org;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pathfinding.Digraph;
import pathfinding.DirectedEdge;
import parser.TagNode;

public class DigraphTest {
    private Digraph G = new Digraph();

    TagNode node0 = new TagNode(0, 0d, 0d);
    TagNode node1 = new TagNode(1, 0d, 1d);
    TagNode node2 = new TagNode(2, 1d, 1d);
    TagNode node3 = new TagNode(3, 3d, 2d);
    TagNode node4 = new TagNode(4, 4d, 0d);

    DirectedEdge edge0 = new DirectedEdge(node0, node1, 1d);
    DirectedEdge edge1 = new DirectedEdge(node1, node2, 1d);
    DirectedEdge edge2 = new DirectedEdge(node2, node3, 1d);
    DirectedEdge edge3 = new DirectedEdge(node0, node2, 1d);
    DirectedEdge edge4 = new DirectedEdge(node4, node1, 1d);

    DirectedEdge edgeLoop = new DirectedEdge(node0, node0, 0d);

    @BeforeEach
    void setUp(){
        G.addEdge(edge0);
        G.addEdge(edge1);
        G.addEdge(edge2);
        G.addEdge(edge3);
        G.addEdge(edge4);

        G.addEdge(edgeLoop);
    }

    @Test
    void verticesTest(){
        // Get nodes
        assertEquals(G.getNode(0).getId(), 0);
        assertEquals(G.getNode(1).getId(), 1);
        assertEquals(G.getNode(2).getId(), 2);
        assertEquals(G.getNode(3).getId(), 3);
        assertEquals(G.getNode(4).getId(), 4);

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
        assertEquals(G.outdegree(node0), 2);
        assertEquals(G.indegree(node1), 2);
        assertEquals(G.outdegree(node1), 1);
    }

    @Test
    void addEdgeTest(){
        assertEquals(G.V(), 5);
        assertEquals(G.E(), 6);
    }
}
