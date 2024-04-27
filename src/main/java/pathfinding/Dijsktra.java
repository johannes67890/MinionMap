package pathfinding;

import parser.TagNode;
import parser.TagWay;
import parser.Type;
import parser.XMLReader;
import util.FileDistributer;
import util.KdTree;
import util.MecatorProjection;
import util.Tree;

import java.util.*;

import edu.princeton.cs.algs4.Point2D;
import parser.Tag;
import parser.TagAddress;

public class Dijsktra {
    private HashMap<Tag, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private HashMap<Tag, DirectedEdge> edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
       

    Dijsktra(Tag start, Tag finish) {
        Digraph G = new Digraph();




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

        for (int t = 0; t < G.V(); t++) {
            if (this.hasPathTo(finish)) {
                System.out.println("Path exists" + start.toString() + finish.toString());
                for (DirectedEdge edge : this.pathTo(finish)) {
                    System.out.println(edge + "   ");
                }
                
            }
            else {
                System.out.println("%d to %d         no path\n" + start + finish);
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
    public boolean hasPathTo(Tag v) {
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
    public Iterable<DirectedEdge> pathTo(Tag v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo.get(v); e != null; e = edgeTo.get(e.from())) {
            path.push(e);
        }
        return path;
    }



    public static void main(String[] args) {
        
        

        new XMLReader(FileDistributer.input.getFilePath());


        TagAddress start = XMLReader.getAddressById(1447913335l);
        TagAddress finish = XMLReader.getAddressById(1447911290l);
  
        Tree.initialize(new ArrayList<Tag>(XMLReader.getWays().valueCollection()));;
        Tree.insertTagInTree(start);
        Tree.insertTagInTree(finish);
        KdTree tree = Tree.getKDTree();
        TagWay t = tree.nearestOfType(new Point2D(finish.getLon(), finish.getLat()), Type.RESIDENTIAL_ROAD, 10);
        TagNode test =  finish.shortestDistanceAndIntersection(t);
        System.out.println("test: " + MecatorProjection.unproject(test));
        

        System.out.println(t);
        // new Dijsktra(a, f);

        
         
    }
}
