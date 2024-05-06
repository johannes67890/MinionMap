package pathfinding;

import parser.TagNode;
/**
 * <p>
 *  <b>IMPORTANT NOTE</b>: This class is made from inspiration from the <a href="https://algs4.cs.princeton.edu/">Princeton University Algorithms Library</a>.
 * </p>
 * 
 *  The {@code DirectedEdge} class represents a weighted edge in an
 *  {@link Digraph}. 
 *  <p>
 *   Each edge consists of two vertices (named {@code from} and {@code to}) of the type {@link TagNode}, and a {@code weight} 
 *   represented as the distance between {@code from} and {@code to}.
 *  </p>
 *  @see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *  @see {@link Digraph} The graph that supports this edge
 *  @see {@link Dijsktra} The algorithm that uses this graph
 * 
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
 public class DirectedEdge {
    private final TagNode v; // The "from" vertex
    private final TagNode w; // The "to" vertex
    private final double weight; // The weight of the edge (distance between v and w)

    /**
     * Initializes a directed edge from vertex {@code v} to vertex {@code w} with
     * the given {@code weight}.
     * @param v the tail vertex
     * @param w the head vertex
     * @param distance the weight of the directed edge
     * @throws IllegalArgumentException if either {@code v} or {@code w}
     *    is a negative integer
     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
     */
    public DirectedEdge(TagNode v, TagNode w, double distance) {
        if (Double.isNaN(distance)) throw new IllegalArgumentException("Speed is NaN");
        if (v.equals(w)){
            System.out.println("V: " + v.getId() + " | W: " + w.getId());
            throw new IllegalArgumentException("Edges cannot be self loops");
        } 
        this.v = v;
        this.w = w;
        this.weight = distance;
    }

    /**
     * @return the tail vertex of the directed edge
     */
    public TagNode from() {
        return v;
    }

    /**
     * @return the head vertex of the directed edge
     */
    public TagNode to() {
        return w;
    }

    /**
     * @return the weight of the directed edge
     */
    public double weight() {
        return weight;
    }

    /**
     * @return a string representation of the directed edge
     */
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", weight);
    }
}