package pathfinding;

import java.util.NoSuchElementException;
import java.util.TreeMap;
import parser.TagNode;
import java.util.ArrayList;


/**
 * <p>
 * <b>IMPORTANT NOTE</b>: This class is made from inspiration from the <a href="https://algs4.cs.princeton.edu/">Princeton University Algorithms Library</a>.
 * </p>
 * 
 *  The {@code Digraph} class represents a directed graph of vertices of type {@link TagNode}. The vertices are
 *  from 0 through <em>V</em> - 1 and support edges with weight.
 *  It supports the following two primary operations: add an edge to the digraph,
 *  iterate over all of the vertices adjacent from a given vertex.
 *  It also provides
 *  methods for returning the indegree or outdegree of a vertex,
 *  the number of vertices <em>V</em> in the digraph,
 *  the number of edges <em>E</em> in the digraph, and the reverse digraph.
 *  Parallel edges and self-loops are permitted.
 *  <p>
 *  This implementation uses an <em>adjacency-lists representation</em>, which
 *  is a {@link TreeMap} of {@link TagNode} as key and a {@link ArrayList} of {@link DirectedEdge} as values.
 *  It uses &Theta;(<em>E</em> + <em>V</em>) space, where <em>E</em> is
 *  the number of edges and <em>V</em> is the number of vertices.
 *  Constructing an empty digraph with <em>V</em> vertices takes
 *  &Theta;(<em>V</em>) time; constructing a digraph with <em>E</em> edges
 *  and <em>V</em> vertices takes &Theta;(<em>E</em> + <em>V</em>) time.
 *  <p>
 *  @see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *  @see {@link Digraph} The graph that supports this edge
 *  @see {@link Dijsktra} The algorithm that uses this graph
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class Digraph {
    private int V; // Number of vertices
    private int E; // Number of edges
    private TreeMap<TagNode, ArrayList<DirectedEdge>> adj;       // Adjacency list
    private TreeMap<TagNode, ArrayList<DirectedEdge>> indegree;  // Indegree list
    private TreeMap<TagNode, ArrayList<DirectedEdge>> outdegree; // Outdegree list

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
    * @return The number of vertices in this edge-weighted graph
    */
    public int V() {
        return V;
    }

    /**
     * @return the number of vertices in this edge-weighted graph
     */
    public Iterable<TagNode> vertices() {
        return adj.keySet();
    }

    public void remove(TagNode v){
        adj.remove(v);
    }

    /**
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

        if (e.weight() < 0){
            throw new IllegalArgumentException("Directed edge " + e + " has a negative weight");
        }

        // Add to adjacency list
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


    /**
     * Get a {@link TagNode} by its {@code id} from the vertices in the graph. 
     * @param id - The id of the node to get
     * @return The node with the given id
     */
    public TagNode getNode(long id){
        for (TagNode node : vertices()){
            if (node.getId() == id){
                return node;
            }
        }
        throw new NoSuchElementException("Node not found in graph!");
    }

    /**
     * Returns the edges that incident on a vertex {@code v}.
     *
     * @param  v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> adj(TagNode v) {
        return adj.get(v);
    }

    /**
     * Get the outdegree of a vertex {@code v}.
     * @param v - The vertex to get the outdegree of
     * @return The outdegree of the vertex
     */
    public int outdegree(TagNode v) {
        return outdegree.get(v).size();
    }

    /**
     * Get the indegree of a vertex {@code v}.
     * @param v - The vertex to get the indegree of
     * @return The indegree of the vertex
     */
    public int indegree(TagNode v) {
        return indegree.get(v).size();
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * <p>
     * {@code for (Edge e : G.edges())}.
     * </p>
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
