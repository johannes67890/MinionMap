package pathfinding;

import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.TreeMap;

import parser.TagNode;

import java.util.ArrayList;

import pathfinding.DirectedEdge;

// Note: This Class is from https://algs4.cs.princeton.edu/44sp/EdgeWeightedGraph.java.html

/**
 *  The {@code EdgeWeightedGraph} class represents an edge-weighted
 *  graph of vertices named 0 through <em>V</em> – 1, where each
 *  undirected edge is of type {@link Edge} and has a real-valued weight.
 *  It supports the following two primary operations: add an edge to the graph,
 *  iterate over all of the edges incident to a vertex. It also provides
 *  methods for returning the degree of a vertex, the number of vertices
 *  <em>V</em> in the graph, and the number of edges <em>E</em> in the graph.
 *  Parallel edges and self-loops are permitted.
 *  By convention, a self-loop <em>v</em>-<em>v</em> appears in the
 *  adjacency list of <em>v</em> twice and contributes two to the degree
 *  of <em>v</em>.
 *  <p>
 *  It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 *  the number of edges and <em>V</em> is the number of vertices.
 *  All instance methods take &Theta;(1) time. (Though, iterating over
 *  the edges returned by {@link #adj(int)} takes time proportional
 *  to the degree of the vertex.)
 *  Constructing an empty edge-weighted graph with <em>V</em> vertices takes
 *  &Theta;(<em>V</em>) time; constructing an edge-weighted graph with
 *  <em>E</em> edges and <em>V</em> vertices takes
 *  &Theta;(<em>E</em> + <em>V</em>) time.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/43mst">Section 4.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Digraph {
    private int V;
    private int E;
    private TreeMap<TagNode, ArrayList<DirectedEdge>> adj;
    private TreeMap<TagNode, ArrayList<DirectedEdge>> indegree; // indegree[v] = indegree of vertex v
    private TreeMap<TagNode, ArrayList<DirectedEdge>> outdegree; 
    /**
     * Initializes an empty edge-weighted graph with 0 edges.
     */
    public Digraph() {
        this.V = 0;
        this.E = 0;
        adj = new TreeMap<>(); 
        indegree = new TreeMap<>();
        outdegree = new TreeMap<>();
    }

    /**
    * Returns the number of vertices in this edge-weighted graph.
    *
    * @return the number of vertices in this edge-weighted graph
    */
    public int V() {
        return V;
    }

    public Iterable<TagNode> vertices() {
        return adj.keySet();
    }

    /**
     * Returns the number of edges in this edge-weighted graph.
     *
     * @return the number of edges in this edge-weighted graph
     */
    public int E() {
        return E;
    }  

    /**
     * Adds the undirected edge {@code e} to this edge-weighted graph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless both endpoints are between {@code 0} and {@code V-1}
     */
    public void addEdge(DirectedEdge e) {
        TagNode v = e.from();
        TagNode w = e.to();

        if (adj.containsKey(v)) {
            adj.get(v).add(e);
        } else {
            ArrayList<DirectedEdge> list = new ArrayList<>();
            list.add(e);
            adj.put(v, list);
        }
        if (adj.containsKey(w)) {
            adj.get(w).add(e);
        } else {
            ArrayList<DirectedEdge> list = new ArrayList<>();
            list.add(e);
            adj.put(w, list);
        }

        // Add to indegree
        if (indegree.containsKey(w)) {
            indegree.get(w).add(e);
        } else {
            ArrayList<DirectedEdge> list = new ArrayList<>();
            list.add(e);
            indegree.put(w, list);
        }

        // Add to outdegree
        if (outdegree.containsKey(v)) {
            outdegree.get(v).add(e);
        } else {
            ArrayList<DirectedEdge> list = new ArrayList<>();
            list.add(e);
            outdegree.put(v, list);
        }

        E++;
        V = adj.size();
    }

    

    public TagNode getNode(long id){
        for(DirectedEdge node : edges()){
            //System.out.println("edges:" + node.either().getId() + " " + node.other(node.either()).getId() + " " + id);
            if(node.from().getId() == id){
                return node.from();
            } else if(node.to().getId() == id){
                return node.to();
            }

        }
        throw new NoSuchElementException("Node not found in graph");
    }

    /**
     * Returns the edges incident on vertex {@code v}.
     *
     * @param  v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> adj(TagNode v) {
        return adj.get(v);
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(TagNode v) {
        return outdegree.get(v).size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int indegree(TagNode v) {
        return indegree.get(v).size();
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    public Iterable<DirectedEdge> edges() {
        ArrayList<DirectedEdge> list = new ArrayList<>();
        for (TagNode v : adj.keySet()) {
            for (DirectedEdge e : adj.get(v)) {
                list.add(e);
            }
        }
        return list;
    }
}
