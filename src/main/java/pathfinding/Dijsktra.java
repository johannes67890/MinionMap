package pathfinding;

import parser.TagNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;


public class Dijsktra {
    private HashMap<TagNode, Double> distTo = new HashMap<TagNode, Double>();
    private ArrayList<DirectedEdge> edgeTo = new ArrayList<DirectedEdge>();
    private Set<TagNode> visited = new HashSet<TagNode>();
    private IndexMinPQ<Double> pq;

    private Digraph G = new Digraph();

    public Dijsktra(Digraph G, TagNode start, TagNode end) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }
        
        distTo.put(start, 0.0);
            
        G.edges().forEach(e -> {
            TagNode v = e.from();
            TagNode w = e.to();
            setInfinity(v);
            setInfinity(w);
        });


        pq = new IndexMinPQ<Double>(G.V());
        pq.insert((int) start.getId(), distTo.get(start));

        while (!pq.isEmpty()) {
            TagNode v = G.getNode(pq.delMin());
            for (DirectedEdge e : G.adj(v)) {
                relax(e);                
            }
        }

        // assert check(G, start, end);

        if (visited.contains(end)) {
            System.out.println("Shortest path from " + start + " to " + end + " is " + distTo.get(end));
            for (TagNode node : pathTo(end)) {
                System.out.print(node + " -> ");
            }
            System.out.println();
        } else {
            System.out.println("No path from " + start + " to " + end);
        }
    }

    public void relax(DirectedEdge e) {
        TagNode v = e.from(), w = e.to();

        if(distTo.get(w) > distTo.get(v) + e.weight()) {
            distTo.put(w, distTo.get(v) + e.weight());
            edgeTo.add((int) w.getId(), e);
            if(pq.contains((int) w.getId())) {
                pq.decreaseKey((int) w.getId(), distTo.get(w));
            } else {
                pq.insert((int) w.getId(), distTo.get(w));
            }
        }
    }

    private void setInfinity(TagNode v) {
        if (!distTo.containsKey(v)) {
            distTo.put(v, Double.POSITIVE_INFINITY);
        }
    }

    private TagNode minDistance() {
        double min = Double.POSITIVE_INFINITY;
        TagNode minNode = null;
        for (TagNode node : distTo.keySet()) {
            if (!visited.contains(node) && distTo.get(node) < min) {
                min = distTo.get(node);
                minNode = node;
            }
        }
        return minNode;
    }

    public double getDistanceTo(TagNode v) {
        return distTo.get(v);
    }

    public boolean hasPathTo(TagNode v) {
        return visited.contains(v);
    }

    public Iterable<TagNode> pathTo(TagNode v) {
        if(!hasPathTo(v)) return null;
        Stack<TagNode> path = new Stack<TagNode>();
        for (DirectedEdge e = edgeTo.get((int) v.getId()); e != null; e = edgeTo.get((int) e.from().getId())) {
            path.push(e.from());
        }
        return path;
    }

    // private boolean check(Digraph G, TagNode start, TagNode end) {
    //     for (DirectedEdge e : G.edges()) {
    //         if(e.weight() < 0) {
    //             System.err.println("negative edge weight detected");
    //             return false;
    //         }
    //     }
    //      // check that distTo[v] and edgeTo[v] are consistent
    //     if (distTo.get(start) != 0.0 || edgeTo.get((int) start.getId()) != null) {
    //         System.err.println("distTo[s] and edgeTo[s] inconsistent");
    //         return false;
    //     }
    //     for (int i = 0; i < G.V(); i++) {
    //         if(start.equals(end)) continue;
    //         if( )
    //     }
    //     return true;
    // }

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

        new Dijsktra(G, a, f);
       // dijsktra.getDistanceTo(f);
    }
}
