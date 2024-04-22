package pathfinding;

import parser.TagNode;
import parser.TagWay;
import parser.XMLReader;
import util.FileDistributer;
import util.Tree;

import java.util.HashMap;
import java.util.Stack;


import parser.Tag;
import parser.TagAddress;

public class Dijsktra {
    private HashMap<Tag, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private HashMap<Tag, DirectedEdge> edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    
       

    Dijsktra(Digraph G, Tag start, Tag f) {

        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }



        distTo = new HashMap<>();
        edgeTo = new HashMap<Tag, DirectedEdge>(G.V());
       
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
        
        

        new XMLReader(FileDistributer.input.getFilePath());

        

        TagNode a = XMLReader.getNodeById(248419951l);
        TagNode f = XMLReader.getNodeById(6339967586l);



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
    }
}
