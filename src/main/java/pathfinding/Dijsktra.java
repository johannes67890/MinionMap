package pathfinding;

import parser.TagNode;
import parser.TagWay;
import parser.Type;
import parser.XMLReader;
import util.FileDistributer;
import util.K3DTree;
import util.MecatorProjection;
import util.Tree;

import java.util.*;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import parser.Tag;
import parser.TagAddress;

public class Dijsktra {
    private HashMap<Tag, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private HashMap<Tag, DirectedEdge> edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    
    private Digraph G = new Digraph();
    private static Stack<TagNode> shortestPath = new Stack<TagNode>();

    Dijsktra(Tag start, Tag finish) {

        if(!(start instanceof TagNode)) {
            start = getNearestRoadPoint(start);
        }
        if(!(finish instanceof TagNode)){
            finish = getNearestRoadPoint(finish);
        }

        // TODO: getSurroundingRoads(start) is not working - get all roads surrounding start
        List<TagWay> list = new ArrayList<>(){
            {
                add(XMLReader.getWayById(27806594l));
                add(XMLReader.getWayById(26154395l));
            }
        
        };

        // for (TagWay way : getSurroundingRoads(start)) {
        for (TagWay way : list) {
            addWayEdges(way);
        }

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
        distTo.put(finish, Double.POSITIVE_INFINITY);

        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(start.getId(), distTo.get(start));
        while (!pq.isEmpty()) {
            TagNode v = G.getNode(pq.delMin());
            for (DirectedEdge e : G.adj(v)) {
                relax(e);
            }
        }

        if (hasPathTo(finish)) {
            StdOut.printf("%d to %d (%.2f)  ", start.getId(), finish.getId(), distTo.get(finish));
            for (DirectedEdge e : pathTo(finish)) {
                shortestPath.push(e.from());
            }
            StdOut.println();
        }
        else {
            StdOut.printf("%d to %d - no path\n", start.getId(), finish.getId());
        }
    }   

    public Digraph getGraph(){
        return G;
    }

    public static Stack<TagNode> getShortestPathofTags(){
        return shortestPath;
    }

    private void addWayEdges(TagWay way){
        for (TagNode node : way.getRefNodes()) {
            if(node.getNext() == null) break;
            if(!node.getParent().getRefNodes().isEmpty() && node.getParent() != way) {
                    if(node.getParent().equals(way)) break;
                    addWayEdges(node.getParent());
            }
            addTwoWayEdges(node, way);
            System.out.println("Added edge from " + node.getId() + " to " + node.getNext().getId() + " with speed limit " + way.getSpeedLimit());
        }
    }

    private void addTwoWayEdges(TagNode node, TagWay way){
            G.addEdge(new DirectedEdge(node, node.getNext(), way.getSpeedLimit()));
            G.addEdge(new DirectedEdge(node.getNext(), node, way.getSpeedLimit()));
    }

    private void addOneWayEdge(TagNode node, TagWay way){
            G.addEdge(new DirectedEdge(node, node.getNext(), way.getSpeedLimit()));
    }

    private Tag getNearestRoadPoint(Tag tag){
        Tag nearestTag = Tree.getNearestOfType(tag, Type.getAllRoads());
        if(nearestTag instanceof TagNode) return nearestTag;
        TagWay tagWay = (TagWay) nearestTag;

        TagNode closesNode = null;
        for (TagNode n : tagWay.getRefNodes()) {
            if (closesNode == null) {
                closesNode = n;
            } else {
                if (closesNode.distance(n) < n.distance(n)) {
                    closesNode = n;
                }
            }
            if(n.getNext() == null) break;
        } 
        return closesNode;
    }

    // TODO:
    // private List<TagWay> getSurroundingRoads(Tag startTag){
    //     List<TagWay> roads = new ArrayList<>();
    //     for (Tag tag : Tree.getTagsNearTag(startTag, Type.getAllRoads())) {
    //         if(tag instanceof TagWay) {
    //             roads.add((TagWay) tag);
    //         }
    //     }
    //     return roads;
    // }
    
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
        Tree.initialize(new ArrayList<Tag>(XMLReader.getWays().valueCollection()));;

        

        TagNode start = XMLReader.getNodeById(248419951l);
        TagNode finish = XMLReader.getNodeById(7798538748l);
  
      
        
        

         new Dijsktra(start, finish);
        System.out.println(Dijsktra.getShortestPathofTags());
       
    }
}
