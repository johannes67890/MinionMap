package pathfinding;

import parser.TagNode;

import java.util.HashMap;
import java.util.Stack;

public class Dijsktra {
    private HashMap<TagNode, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private HashMap<TagNode, DirectedEdge> edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    
       

    Dijsktra(Digraph G, TagNode start, TagNode f) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new HashMap<>();
        edgeTo = new HashMap<TagNode, DirectedEdge>(G.V());
       
        for (TagNode v : G.vertices()) {
            distTo.put(v, Double.POSITIVE_INFINITY);   
        }
        distTo.put(start, 0.0);

        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(start.getId(), distTo.get(start));
        while (!pq.isEmpty()) {
            TagNode v = G.getNode(pq.delMin());
            for (DirectedEdge e : G.adj(v)) {
                relax(e);
            }
        }
    }

      // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        TagNode v = e.from(), w = e.to();
        if(distTo.get(w) > distTo.get(v) + e.weight()) {
            distTo.put(w, distTo.get(v) + e.weight());
            edgeTo.put(w, e);
            if(pq.contains(w.getId())){
                pq.decreaseKey(w.getId(), distTo.get(w));
            } else {
                pq.insert(w.getId(), distTo.get(w));
            }
        }
    }

    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(TagNode v) {
        return distTo.get(v) < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> pathTo(TagNode v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo.get(v); e != null; e = edgeTo.get(e.from())) {
            path.push(e);
        }
        return path;
    }

    public static void main(String[] args) {
        Digraph G = new Digraph();
        TagNode a = new TagNode(1, 1, 1);
        TagNode b = new TagNode(2, 2, 2);
        TagNode c = new TagNode(3, 3, 3);
        TagNode d = new TagNode(4, 4, 4);
        TagNode e = new TagNode(5, 5, 5);
        TagNode f = new TagNode(6, 6, 6);

        G.addEdge(new DirectedEdge(a, b, 1));
        G.addEdge(new DirectedEdge(a, c, 2));
        G.addEdge(new DirectedEdge(b, d, 3));
        G.addEdge(new DirectedEdge(b, e, 4));
        G.addEdge(new DirectedEdge(c, f, 5));
        G.addEdge(new DirectedEdge(d, f, 6));
        G.addEdge(new DirectedEdge(e, f, 7));

        Dijsktra sp = new Dijsktra(G, a, f);

        
         // print shortest path
         for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(f)) {
                System.out.println("Path exists" + a.toString() + f.toString());
                for (DirectedEdge edge : sp.pathTo(f)) {
                    System.out.println(edge + "   ");
                }
                
            }
            else {
                System.out.println("%d to %d         no path\n" + a + f);
            }
        }
       // dijsktra.getDistanceTo(f);
    }
}
