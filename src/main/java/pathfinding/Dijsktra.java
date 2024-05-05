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
    private boolean takeShortestRoute = false; // This can be enabled to get a more precise shortest path, but at the cost of perfomance
    private double distanceBetweenEndPoints; // Distance between start and finish

    private boolean foundFinish = false;

    private TransportType type;
    private TagNode start;
    private TagNode finish;

    private Digraph G = new Digraph();
    private Stack<TagNode> shortestPath = new Stack<TagNode>();


    public Dijsktra(Tag _start, Tag _finish, TransportType transportType, boolean shortest) {
        distTo = new HashMap<>();
        edgeTo = new HashMap<Long, DirectedEdge>(G.V());
        costTo = new HashMap<>();
        this.takeShortestRoute = shortest;
        type = transportType;

        this.start = getNearestRoadPoint(_start, transportType);
        this.finish = getNearestRoadPoint(_finish, transportType);
        if(this.start.getId() == this.finish.getId()) throw new IllegalArgumentException("Start and finish are the same");
        //start timer
        long startTime = System.currentTimeMillis();
        addSurroundingRoadsFancy(start, finish, transportType);
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
        endTime = 0;
        System.out.println("Size of graph: " + G.V() + " vertices and " + G.E() + " edges");
        pq = new IndexMinPQ<Double>();
        pq.insert(start.getId(), distTo.get(start.getId()));
        long timet = System.currentTimeMillis();
        while (!pq.isEmpty()) {
            TagNode v = G.getNode(pq.delMin());
            
            for (DirectedEdge e : G.adj(v)) {
                relax(e);
            }
            if (hasPathTo(finish)){
                System.out.println("Found path!");
                break;
            }
        }
        
        System.out.println("Time to find shortest path: " + (System.currentTimeMillis() - timet) + "ms");
        //printPath();
    }   

    public Digraph getGraph(){
        return G;
    }

    /**
     * This can be enabled on the cost of perfomance, but you get a shorter route. 
     * It needs to be set before finding route, after creating object.
     * @param bool
     */
    public void setPrecisionMode(boolean bool){
        takeShortestRoute = bool;
    }

    public Stack<TagNode> getShortestPathofTags(){
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
                    for (TagNode tagNode : way.getRefNodes()) {
                        if(tagNode.getId() == finish.getId()) {
                            System.out.println("Found finish");
                            this.finish = tagNode;
                            distTo.put(tagNode.getId(), Double.POSITIVE_INFINITY);
                            return;
                        }
                        if(tagNode.hasIntersection()) addSurroundingRoads(tagNode, finish,transportType);
                        if(tagNode.getNext() == null) continue;
                    } 
                }
            }
        } else{
            addRoad(startTag.getParent());
            for (TagNode tagNode : startTag.getParent().getRefNodes()) {
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
    }

    private void addSurroundingRoadsFancy(TagNode startTag, TagNode finish, TransportType transportType){
        if (foundFinish) return;
        for (Tag tag : startTag.getIntersectionTags()) {
            if(tag instanceof TagWay){
                TagWay way = (TagWay) tag;
                ArrayList<TagNode> list = new ArrayList<>();
                if(!transportType.getRoadTypes().contains(tag.getType()) || tag.getType() == null) continue;
                if(surroundingTags.contains(way.getId())) continue;
                surroundingTags.add(way.getId());
                //addRoad(way);
                for (int i = 0; i < way.getRefNodes().size(); i++) {
                    TagNode tagNode = new TagNode(way.getRefNodes().get(i));
                    tagNode.clearLinks();
                    tagNode.setParent(way);
                    if(tagNode.getId() == finish.getId()) {
                        System.out.println("Found finish: " + tagNode.getId()); 
                        foundFinish = true;
                        distTo.put(tagNode.getId(), Double.POSITIVE_INFINITY);
                    }

                    list.add(tagNode);

                    if(tagNode.hasIntersection() || i == way.getRefNodes().size() || tagNode.getId() == finish.getId()){
                        addRoadFancy(list, way);
                        list = new ArrayList<>();
                        list.add(tagNode);
                        addSurroundingRoadsFancy(tagNode, finish,transportType);
                    }
                } 
            }
        }
    }

    private void addRoadFancy(List<TagNode> list, TagWay way){
        
        if (list.size() == 1) return;
        //if (list.get(0).equals(list.get(list.size()-1)) && list.size() == 2) return;
        if (list.get(0).equals(list.get(list.size()-1)) && list.size() > 2){
            //List<TagNode> list2 = list.subList(list.size()/2, list.size());
            for (int i = 1; i < list.size(); i++){
                addRoadFancy(list.subList(i-1, i+1), way);
            }
            return;
        }
        double distance = 0;
        for (int i = 1; i < list.size(); i++){
            distance += list.get(i-1).distance(list.get(i));
        }
        //distance = (distance / 1000) / way.getSpeedLimit();
        G.addEdge(new DirectedEdge(list.get(0), list.get(list.size()-1), distance));
        if (!way.isOneWay()){
            G.addEdge(new DirectedEdge(list.get(list.size()-1), list.get(0), distance));
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
        ArrayList<Tag> tags = Tree.getNearestOfTypeBruteForce(tag, transportType.getRoadTypes());
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
    
    /**
     * Relax edge if its changed
     * @param e The fortunate edge
     */
    private void relax(DirectedEdge e){
        long v = e.from().getId(), w = e.to().getId();
        double minutes = 1;
        double weight = 0;
        double aStarRatio = 0;
        double distanceToDestination = e.to().distance(finish);
        double distance = e.weight(); // Length of edge in meters
        if (type.equals(TransportType.CAR)){
            minutes = ((distance / 1000) / e.from().getParent().getSpeedLimit()) * 60; // Minutes to drive the edge
        }else if (type.equals(TransportType.FOOT)){
            minutes = ((distance / 1000) / 5) * 60; // Minutes to walk the edge with default speed 5km/t
        }else if (type.equals(TransportType.BIKE)){
            minutes = ((distance / 1000) / 20) * 60; // Minutes to cycle the edge with default speed 20km/t
        }
        aStarRatio = type.getAStarRatio(!takeShortestRoute);
        if (takeShortestRoute){
            weight = distance;
        }else{
            weight = minutes;
        }
        
        if(distTo.get(w) > distTo.get(v) + weight) {
            if (!takeShortestRoute){
                distTo.put(w, distTo.get(v) + weight);
                costTo.put(w, costTo.get(v) + distance);
            }else{
                distTo.put(w, distTo.get(v) + weight);
                costTo.put(w, costTo.get(v) + minutes);
            }
            edgeTo.put(w, e);
            if (pq.contains(w)) {
                pq.decreaseKey(w, (distTo.get(w)*aStarRatio) + distanceToDestination);
            } else {
                pq.insert(w, (distTo.get(w)*aStarRatio) + distanceToDestination);
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
        //System.out.println("Weight: " + distTo.get(finish.getId()));
        //System.out.println("Cost: " + costTo.get(finish.getId()));

        for (DirectedEdge e : pathTo(this.finish)) {
            if(e.to() == null) break;
            TagNode node = new TagNode(e.from());
            node.clearLinks();
            node.setParent(e.from().getParent());
            nodes.add(node);
        }
        return nodes;
    }

    public TLinkedList<TagNode> shortestPathDetailed(){
        System.out.println(G.getNode(finish.getId()));
        TLinkedList<TagNode> nodes = shortestPath();
        TLinkedList<TagNode> returnList = new TLinkedList<>();
        for (int i = 1; i < nodes.size(); i++){
            boolean onPath = false;
            for (TagNode node : nodes.get(i-1).getParent().getRefNodes()){
                if (node.equals(nodes.get(i-1))){
                    onPath = !onPath;
                }
                if (onPath){
                    returnList.add(node);
                }
                if (node.equals(nodes.get(i))){
                    onPath = !onPath;
                }
            }
        }

        return returnList;
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

    public String getTotalDistance(){
        Double totalDistance = 0.0;
        for (Double length : printPath().values()) {
            totalDistance += length;
        }

        if (totalDistance / 1000 > 1.0){
            totalDistance = MathUtil.round(totalDistance / 1000, 2);
            return Double.toString(totalDistance) + " km";
        }

        totalDistance = MathUtil.round(totalDistance, 2);
        return Double.toString(totalDistance) + " m";
    }

    public String getDistanceOfPath(){
        if (takeShortestRoute){
            return "" + MathUtil.round(distTo.getOrDefault(this.finish.getId(), 0.0), 2) + "m";
        }
        return "" + MathUtil.round(costTo.getOrDefault(this.finish.getId(), 0.0), 2) + "m";
    }

    public String getMinutesOfPath(){
        if (takeShortestRoute){
            return "" + MathUtil.round(costTo.getOrDefault(this.finish.getId(), 0.0), 2) + "min";
        }
        return "" + MathUtil.round(distTo.getOrDefault(this.finish.getId(), 0.0), 2) + "min";
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
      

        Dijsktra d = new Dijsktra(start, finish, TransportType.CAR, false);
        System.out.println(d.shortestPath().toString() + "\n");
        d.printPath().forEach((k, v) -> System.out.println(k.getId() + " " + v));
        System.out.println("Total distance: " + d.getTotalDistance());
       
    }
}
