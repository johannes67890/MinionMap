package pathfinding;

import parser.TagNode;
import parser.TagWay;
import parser.XMLReader;
import structures.IndexMinPQ;
import structures.KDTree.Tree;
import util.FileDistributer;
import util.MathUtil;
import util.TransportType;
import util.Type;

import java.text.DecimalFormat;
import java.util.*;


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import gnu.trove.list.linked.TLinkedList;
import parser.Tag;
import parser.TagAddress;

public class Dijsktra {
    private HashMap<Long, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private HashMap<Long, Double> costTo;
    private HashMap<Long, DirectedEdge> edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private HashSet<Long> surroundingTags = new HashSet<Long>();

    private double distanceBetweenEndPoints;

    private TagNode start;
    private TagNode finish;

    private Digraph G = new Digraph();
    private static Stack<TagNode> shortestPath = new Stack<TagNode>();


    public Dijsktra(Tag _start, Tag _finish, TransportType transportType) {
        distTo = new HashMap<>();
        edgeTo = new HashMap<Long, DirectedEdge>(G.V());
        costTo = new HashMap<>();

        this.start = getNearestRoadPoint(_start, transportType);
        this.finish = getNearestRoadPoint(_finish, transportType);
        if(this.start.getId() == this.finish.getId()) throw new IllegalArgumentException("Start and finish are the same");
        //start timer
        long startTime = System.currentTimeMillis();
        addSurroundingRoads(start, finish, transportType);
        long endTime = System.currentTimeMillis();
        System.out.println("Time to add surrounding roads: " + (endTime - startTime) + "ms");
        distanceBetweenEndPoints = start.distance(finish);
       
        for (TagNode v : G.vertices()) {
            if(v.getId() == start.getId()) {
                this.start = v;
                distTo.put(v.getId(), 0.0);
                costTo.put(v.getId(), 0.0);
                continue;
            }
            if(v.getId() == finish.getId()) {
                this.finish = v;
                distTo.put(v.getId(), Double.POSITIVE_INFINITY);
                costTo.put(v.getId(), distanceBetweenEndPoints);
                continue;
            }
            else {
                distTo.put(v.getId(), Double.POSITIVE_INFINITY);
                costTo.put(v.getId(), distanceBetweenEndPoints);
                continue;
            }
        } 

        System.out.println("Size of graph: " + G.V() + " vertices and " + G.E() + " edges");
        pq = new IndexMinPQ<Double>();
        pq.insert(start.getId(), distTo.get(start.getId()));
        startTime = System.currentTimeMillis();
        while (!pq.isEmpty()) {
            TagNode v = G.getNode(pq.delMin());
            
            for (DirectedEdge e : G.adj(v)) {
                relax(e);
            }
            if (hasPathTo(finish)){
                break;
            }
        }
        
        // timer end
        endTime = System.currentTimeMillis();
        System.out.println("Time to find shortest path: " + (endTime - startTime) + "ms");
        //printPath();
    }   

    public Digraph getGraph(){
        return G;
    }

    public static Stack<TagNode> getShortestPathofTags(){
        return shortestPath;
    }

    private void addSurroundingRoads(TagNode startTag, TagNode finish, TransportType transportType){
        if(startTag.hasIntersection()){
            for (Tag tag : startTag.getIntersectionTags()) {
                if(tag instanceof TagWay){
                    TagWay way = (TagWay) tag;
                    if(!transportType.getRoadTypes().contains(tag.getType()) || tag.getType() == null) continue;
                    if(surroundingTags.contains(way.getId())) continue;
                    surroundingTags.add(way.getId());
                    addRoad(way);
                    checkIntersections(startTag, transportType);
                }
            }
        } else{
            addRoad(startTag.getParent());
            checkIntersections(startTag, transportType);
        }
    }    

    private void checkIntersections(TagNode t, TransportType transportType){
        for (TagNode tagNode : t.getParent().getRefNodes()) {
            if(tagNode.getId() == finish.getId()) {
                System.out.println("Found finish");
                this.finish = tagNode;
                distTo.put(tagNode.getId(), Double.POSITIVE_INFINITY);
                return;
            }
            
            if(tagNode.hasIntersection()) addSurroundingRoads(tagNode,finish, transportType);
            if(tagNode.getNext() == null) continue;
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
    }

    private void addOneWayEdge(TagNode node, TagWay way){
            G.addEdge(new DirectedEdge(node, node.getNext(), way.getSpeedLimit()));
    }

    public TagNode getNearestRoadPoint(Tag tag, TransportType transportType){
        if(tag instanceof TagNode) return (TagNode) tag;
        // TODO: getNearestOfType is wrong in some edge cases
        ArrayList<Tag> tags = Tree.getNearestOfType(tag, transportType.getRoadTypes());
        double bestDistance = Double.MAX_VALUE;
        TagNode best = null;
        for (Tag tempTag : tags){
            if (tempTag instanceof TagWay){
                TagWay way = (TagWay) tempTag;
                if(!transportType.getRoadTypes().contains(way.getType()) || way.getType() == null) continue;
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

        double distance = (G.getNode(w).distance(G.getNode(v))); // Length of the edge in meters

        if(distTo.get(w) > distTo.get(v) + distance) {
            distTo.put(w, distTo.get(v) + distance);
            edgeTo.put(w, e);
            if(pq.contains(w)){
                pq.decreaseKey(w, distTo.get(w));
            } else {
                pq.insert(w, distTo.get(w));
            }
        }
    }

    private void relax2(DirectedEdge e){
        long v = e.from().getId(), w = e.to().getId();

        double distance = (G.getNode(w).distance(G.getNode(v))); // Length of the edge in meters

        if(distTo.get(w) + costTo.get(w) > distTo.get(v) + distance + costTo.get(v)) {
            distTo.put(w, distTo.get(v) + distance);
            edgeTo.put(w, e);
            System.out.println(e);
            if(pq.contains(w)){
                pq.decreaseKey(w, distTo.get(w) + costTo.get(w));
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

    public TLinkedList<TagNode> shortestPath(){
        TLinkedList<TagNode> nodes = new TLinkedList<>();
        if (!hasPathTo(this.finish)) return null;
        System.out.println("Minutes: " + distTo.get(finish.getId()));

        for (DirectedEdge e : pathTo(this.finish)) {
            if(e.to() == null) break;
            TagNode node = new TagNode(e.from());
            node.clearLinks();
            node.setParent(e.from().getParent());
            nodes.add(node);
        }
        return nodes;
    }
    /**
     * This method is for debugging purposes only. It returns all TagWays visited through pathfinding
     * @return All TagWays visited through pathfinding
     */
    public ArrayList<Tag> allVisitedPaths(){
        ArrayList<Tag> tags = new ArrayList<>();

        for (long key : distTo.keySet()){
            if (distTo.get(key) < Double.POSITIVE_INFINITY){
                DirectedEdge edge = edgeTo.get(key);
                if (edge == null){
                    continue;
                }
                TLinkedList<TagNode> temp = new TLinkedList<>();
                TagNode from = new TagNode(edge.from());
                TagNode to = new TagNode(edge.to());
                from.clearLinks();
                to.clearLinks();
                temp.add(from);
                temp.add(to);
                tags.add(new TagWay(0, "PartOfRoute", temp, (short) 0, Type.PATHGRID));
            }
        }
        return tags;
    }

    /**
     * This method is for debugging purposes only. It returns all edges in the graph
     * @return All TagWays in the graph
     */
    public ArrayList<Tag> allPathsInGraph(){
        ArrayList<Tag> tags = new ArrayList<>();

        for (DirectedEdge edge : G.edges()){
            if (edge == null || edge.from() == null || edge.to() == null){
                //continue;
            }
            TLinkedList<TagNode> temp = new TLinkedList<>();
            temp.add(edge.from());
            temp.add(edge.to());
            tags.add(new TagWay(0, "PartOfRoute", temp, (short) 0, Type.PATHGRID));
        }

        return tags;
    }

    public double getTotalDistance(){
        Double totalDistance = 0.0;
        for (Double length : printPath().values()) {
            totalDistance += length;
        }

        return MathUtil.round(totalDistance, 2);
    }

    public LinkedHashMap<TagWay, Double> printPath(){        
        LinkedHashMap<TagWay, Double> distToWay = new LinkedHashMap<>();
        double distance = 0;
        for (TagNode e : shortestPath()) {
            if(e.getNext() == null) {
                distance += e.distance(finish);
                distToWay.put(e.getParent(), distance);
                break;
            }
            if(e.getParent() != e.getNext().getParent()){
                distance += e.distance(e.getNext());
                distToWay.put(e.getParent(), distance);
                distance = 0;
                continue;
            }else {
                distance += e.distance(e.getNext());
            }
        }
        
        return distToWay;
    }



    public static void main(String[] args) {
        
        

        new XMLReader(FileDistributer.testMap.getFilePath());
        Tree.initialize(new ArrayList<Tag>(XMLReader.getWays().valueCollection()));;
        
        // default

        TagNode start = XMLReader.getNodeById(248419951l);
        TagNode finish = XMLReader.getNodeById(24343192l);
        // Default addresses
        // TagAddress start = XMLReader.getAddressById(1447913335l);
        // TagNode finish = XMLReader.getNodeById(1447911293l);
      
        // bolmholm
        // TagNode finish = XMLReader.getNodeById(5351948945l);
        // TagNode start = XMLReader.getNodeById(379686625l);
      

       Dijsktra d = new Dijsktra(start, finish, TransportType.CAR);
       System.out.println(d.shortestPath().toString() + "\n");
        d.printPath().forEach((k, v) -> System.out.println(k.getId() + " " + v));
        System.out.println("Total distance: " + d.getTotalDistance());
       
    }
}
