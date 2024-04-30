package pathfinding;

import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.Type;
import parser.XMLReader;
import util.FileDistributer;
import util.K3DTree;
import util.MecatorProjection;
import util.Tree;

import java.util.*;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import parser.Tag;
import parser.TagAddress;

public class Dijsktra {
    private HashMap<Long, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private HashMap<Long, DirectedEdge> edgeTo;    // edgeTo[v] = last edge on shortest s->v path
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
                add(XMLReader.getWayById(27806594l));
                add(XMLReader.getWayById(26154395l));
            }
        
        };

        // // for (TagWay way : getSurroundingRoads(start)) {
        for (TagWay way : XMLReader.getWays().valueCollection()) {
            addWayEdges(way);
        }

        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        distTo = new HashMap<>();
        edgeTo = new HashMap<Long, DirectedEdge>(G.V());
       
        for (TagNode v : G.vertices()) {
            if(v.getId() == start.getId()) {
                start = v;
                distTo.put(v.getId(), 0.0);
                continue;
            }
            if(v.getId() == finish.getId()) {
                finish = v;
                distTo.put(v.getId(), Double.POSITIVE_INFINITY);
                continue;
            }
            else {
                distTo.put(v.getId(), Double.POSITIVE_INFINITY);
                continue;
            }
        } 

        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(start.getId(), distTo.get(start.getId()));
        while (!pq.isEmpty()) {
            TagNode v = G.getNode(pq.delMin());
            
            for (DirectedEdge e : G.adj(v)) {
                relax(e);
            }
        }

        if (hasPathTo(finish)) {
            StdOut.printf("%d to %d (%.2f)  ", start.getId(), finish.getId(), distTo.get(finish.getId()));
            StdOut.println();
            for (DirectedEdge e : pathTo(finish)) {
                System.out.println(e + "       ");
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
            ArrayList<Tag> intersections = Tree.getTagFromPoint(node);
            if(intersections.size() > 1 && intersections != null){
               System.out.println("Intersections: " + node.getId());
                for (Tag tag : intersections) {
                    if(tag instanceof TagWay){
                        TagWay w = (TagWay) tag;
                        if(tag.equals(way)) continue;
                        for (TagNode tag2 : w.getRefNodes()) {
                            if(tag2.getNext() == null) break;
                            addTwoWayEdges(tag2, w);
                        }
                       System.out.println("intersection done");
                    }
                }
            }   
          
            if(node.getNext() == null) break;
            addTwoWayEdges(node, way);
            System.out.println("Added edge from " + node.getId() + " to " + node.getNext().getId() + " with speed limit " + way.getSpeedLimit());
        }
    }

    private void addTwoWayEdges(TagNode node, TagWay way){
            G.addEdge(new DirectedEdge(node, node.getNext(), 1));
            G.addEdge(new DirectedEdge(node.getNext(), node, 1));
            System.out.println("Added edge from " + node.getId() + " to " + node.getNext().getId() + " with speed limit " + way.getSpeedLimit());
    }

    private void addOneWayEdge(TagNode node, TagWay way){
            G.addEdge(new DirectedEdge(node, node.getNext(), 1));
    }

    private TagNode getNearestRoadPoint(Tag tag){
        ArrayList<Tag> tags = Tree.getNearestOfType(tag, Type.getAllRoads());
        double bestDistance = Double.MAX_VALUE;
        TagNode best = null;
        for (Tag tempTag : tags){
            if (tempTag instanceof TagWay){
                TagWay way = (TagWay) tempTag;
                if (Type.getAllRoads().contains(way.getType())){
                    for (TagNode node : way.getRefNodes()){
                        double distance = new Point2D(node.getLon(), node.getLat()).distanceSquaredTo(new Point2D(tag.getLon(), tag.getLat()));
                        if (distance < bestDistance){
                            best = node;
                            bestDistance = distance;
                        }
                    }
                }
            }
        }

        return best;
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
        long v = e.from().getId(), w = e.to().getId();

        // if(!distTo.containsKey(w)){
        //     distTo.put(w, Double.POSITIVE_INFINITY);
        // }
        // if(!distTo.containsKey(v)){
        //     distTo.put(v, Double.POSITIVE_INFINITY);
        // }
        if(distTo.get(w) > distTo.get(v) + e.weight()) {
            distTo.put(w, distTo.get(v) + e.weight());
            edgeTo.put(w, e);
            if(pq.contains(w)){
                pq.decreaseKey(w, distTo.get(w));
            } else {
                pq.insert(w, distTo.get(w));
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
        return distTo.get(v.getId()) < Double.POSITIVE_INFINITY;
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
        for (DirectedEdge e = edgeTo.get(v.getId()); e != null; e = edgeTo.get(e.from().getId())) {
            path.push(e);
        }
        return path;
    }



    public static void main(String[] args) {
        
        

        new XMLReader(FileDistributer.input.getFilePath());
        Tree.initialize(new ArrayList<Tag>(XMLReader.getWays().valueCollection()));;
        

        // TagNode start = XMLReader.getNodeById(286405539l);
        // TagNode finish = XMLReader.getNodeById(286405326l);
        TagNode start = XMLReader.getNodeById(6760379519l);
        TagNode finish = XMLReader.getNodeById(286405326l);
      
        

         new Dijsktra(start, finish);
        System.out.println(Dijsktra.getShortestPathofTags());
       
    }
}
