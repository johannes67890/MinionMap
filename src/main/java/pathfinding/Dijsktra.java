package pathfinding;

import parser.TagNode;
import parser.TagWay;
import parser.Type;
import parser.XMLReader;
import util.FileDistributer;
import util.Tree;

import java.util.*;


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import parser.Tag;

enum TransportType {
    CAR(Type.getAllCarRoads()),
    FOOT(Type.getAllPedestrianRoads()),
    BIKE(Type.getAllBikeRoads());

    private final List<String> roadTypes;
    
    TransportType(List<String> roadTypes){
        this.roadTypes = roadTypes;
    }

    public List<String> getRoadTypes(){
        return roadTypes;
    }
}

public class Dijsktra {
    private HashMap<Long, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private HashMap<Long, DirectedEdge> edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    HashSet<Long> surroundingTags = new HashSet<Long>();

    private Digraph G = new Digraph();
    private static Stack<TagNode> shortestPath = new Stack<TagNode>();

    Dijsktra(Tag _start, Tag _finish, TransportType transportType) {
        System.out.println(_start);
        TagNode start = (TagNode) _start;
        TagNode finish = (TagNode) _finish;
        // TagNode start = getNearestRoadPoint(_start);
        // TagNode finish = getNearestRoadPoint(_finish);

        addSurroundingRoads(start, transportType);

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

    private void addSurroundingRoads(TagNode startTag, TransportType transportType){
            if(startTag.hasIntersection()){
                for (Tag tag : startTag.getIntersectionTags()) {
                if(tag instanceof TagWay){
                        TagWay way = (TagWay) tag;
                        if(surroundingTags.contains(way.getId())) continue;
                        surroundingTags.add(way.getId());
                        addRoad(way);
                        for (TagNode tagNode : way.getRefNodes()) {

                            if(tagNode.getNext() == null) continue;

                            if(tagNode.hasIntersection()) addSurroundingRoads(tagNode, transportType);
                        }
                }
                }
            } 
        }    

    private void addRoad(TagWay way){
        for (TagNode node : way.getRefNodes()) {
            if(node.getNext() == null) return;
            if(way.isOneWay()){
                addOneWayEdge(node, way);
            } else {
                addTwoWayEdges(node, way);
            }
        }

    }

    private void addTwoWayEdges(TagNode node, TagWay way){
            G.addEdge(new DirectedEdge(node, node.getNext(), way.getSpeedLimit()));
            G.addEdge(new DirectedEdge(node.getNext(), node, way.getSpeedLimit()));
            System.out.println("Added edge from " + node.getId() + " to " + node.getNext().getId());
    }

    private void addOneWayEdge(TagNode node, TagWay way){
            G.addEdge(new DirectedEdge(node, node.getNext(), way.getSpeedLimit()));
    }

    private TagNode getNearestRoadPoint(Tag tag){
        if(tag instanceof TagNode) return (TagNode) tag;
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
    
      // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        long v = e.from().getId(), w = e.to().getId();

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
        
        // default

        TagNode start = XMLReader.getNodeById(286405539l);
        TagNode finish = XMLReader.getNodeById(1682443740l);
        // Default addresses
        // TagAddress start = XMLReader.getAddressById(1447913335l);
        // TagNode finish = XMLReader.getNodeById(1447911293l);
      
        // bolmholm
        // TagNode finish = XMLReader.getNodeById(5351948945l);
        // TagNode start = XMLReader.getNodeById(379686625l);
      

        

        new Dijsktra(start, finish, TransportType.CAR);
        System.out.println(Dijsktra.getShortestPathofTags());
       
    }
}
