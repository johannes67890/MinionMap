package pathfinding;

import parser.TagNode;
import parser.TagWay;
import structures.IndexMinPQ;
import structures.KDTree.Tree;
import util.MathUtil;
import util.TransportType;
import util.Type;

import java.util.*;


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Stack;
import gnu.trove.list.linked.TLinkedList;
import parser.Tag;

/**
 * An implementation of the Dijkstra algorithm, with the addition of A-Star. This implementation
 * is based of Edsger Dijkstra original paper, and Kevin Wayne and Robert Sedgewicks interpretation of the algorithm.
 * 
 * The algorithm is to find the shortest path in a directed weighted graph, where this graph is based off of 
 * the road-network on a given map.
 */
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

    /**
     * A contructor for the dijkstra algorithm. Where the class costructs the road network into a digraph and 
     * travels through the graph based on Dijkstra algorithm, where it relaxes the edges of the graph every time
     * an edge is added to the Priority Queue. When relaxing it calculates the individual cost to get to the 
     * individual edge. 
     * 
     * When the algorithm is finished. The TagNode finish should contain a cost, that proves that the algorithm
     * was succesfully done.
     * 
     * @param _start        Starting TagNode of the path
     * @param _finish       Finishing TagNode of the path
     * @param transportType The type of transportation used
     * @param shortest      The type of pathfinding. If true then speed is ignored. If false then the weight is minutes of travel
     */
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

    /**
     * Returns the directed weighted graph ({@link Digraph})
     * @return the directed weighted graph ({@link Digraph})
     */
    public Digraph getGraph(){
        return G;
    }

    /**
     * This function adds the roads to the directed graph {@link Digraph}, until it reached the finish {@link TagNode}.
     * It is a recursive function that calls itself everytime it encounters an intersection. The function also
     * pools the {@link TagNode}s together between intersection to make the amount of edges and vertices as small
     * as possible.
     * 
     * @param startTag      the starting {@link TagNode} of the path
     * @param finish        the finish {@link TagNode} of the path
     * @param transportType the type of transportation {@link TransportType} used on the path
     */
    private void addSurroundingRoads(TagNode startTag, TagNode finish, TransportType transportType){
        if (foundFinish) return;
        for (Tag tag : startTag.getIntersectionTags()) {
            if(tag instanceof TagWay){
                TagWay way = (TagWay) tag;
                ArrayList<TagNode> list = new ArrayList<>();
                if(!transportType.getRoadTypes().contains(tag.getType()) || tag.getType() == null) continue;
                if(surroundingTags.contains(way.getId())) continue;
                surroundingTags.add(way.getId());

                for (int i = 0; i < way.getRefNodes().size(); i++) {
                    TagNode tagNode = new TagNode(way.getRefNodes().get(i));
                    tagNode.clearLinks();
                    tagNode.setParent(way);
                    if(tagNode.getId() == finish.getId()) {
                        foundFinish = true;
                        distTo.put(tagNode.getId(), Double.POSITIVE_INFINITY);
                    }

                    list.add(tagNode);

                    if(tagNode.hasIntersection() || i == way.getRefNodes().size() || tagNode.getId() == finish.getId()){
                        addRoad(list, way);
                        list = new ArrayList<>();
                        list.add(tagNode);
                        addSurroundingRoads(tagNode, finish,transportType);
                    }
                } 
            }
        }
    }

    /**
     * A given list of {@link TagNode}s that needs to be pooled together to make one weighted edge
     * and add it to the directed weighted graph {@link Digraph}
     * 
     * @param list  the list of {@link TagNode}s that needs to be made into an edge
     * @param way   the parent {@link TagWay} of the {@link TagNode}s
     */
    private void addRoad(List<TagNode> list, TagWay way){
        
        if (list.size() == 1) return;
        //if (list.get(0).equals(list.get(list.size()-1)) && list.size() == 2) return;
        if (list.get(0).equals(list.get(list.size()-1)) && list.size() > 2){
            //List<TagNode> list2 = list.subList(list.size()/2, list.size());
            for (int i = 1; i < list.size(); i++){
                addRoad(list.subList(i-1, i+1), way);
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

    /**
     * Gets the nearest {@link TagNode} from a given {@link Tag} by a specific {@link TransportType}.
     * 
     * @param tag           the {@link TagNode} from which the searches origin point
     * @param transportType the {@link TransportType} is what type of transport the returned {@link TagNode} needs to be
     * @return              the nearest {@link TagNode} of {@link TransportType}
     */
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
     * Relaxes the given edge if its changed
     * @param e the given {@link DirectedEdge} that needs to be relaxed
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

    /**
     * Returns the chosen path from the Djikstra algorithm in a {@link TLinkedList} of {@link TagNode}s
     * 
     * @return the chosen path from the Djikstra algorithm in a {@link TLinkedList} of {@link TagNode}s
     */
    public TLinkedList<TagNode> shortestPath(){
        TLinkedList<TagNode> nodes = new TLinkedList<>();
        if (!hasPathTo(this.finish)) throw new IllegalArgumentException("No path to finish");

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
     * Returns a recreated path from the simplified {@link Digraph} edges.
     * 
     * @return a recreated path from the simplified {@link Digraph} edges.
     */
    public TLinkedList<TagNode> shortestPathDetailed(){

        TLinkedList<TagNode> returnList = new TLinkedList<>();
        try{
            TLinkedList<TagNode> nodes = shortestPath();
    
            for (TagNode n : nodes) {
                for (TagNode nWay : n.getParent().getRefNodes()) {
                    TagNode a = new TagNode(n);
                    a.clearLinks();
                    returnList.add(a);
                    if(nWay.getNext() != null || nodes.contains(nWay.getNext())) break;
                }
                TagNode a = new TagNode(n);
                a.clearLinks();
                returnList.add(a);
                if(n.getNext() == null) break;
            }
    
            
        }catch (Exception e){
            System.err.println("Error happened when pathfinding!");
            e.printStackTrace();
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

    /**
     * Returns the total distance of the path to the finish point
     * @return the total distance of the path to the finish point
     */
    public String getDistanceOfPath(){
        if (takeShortestRoute){
            return "" + MathUtil.round(distTo.getOrDefault(this.finish.getId(), 0.0), 2) + "m";
        }
        return "" + MathUtil.round(costTo.getOrDefault(this.finish.getId(), 0.0), 2) + "m";
    }

    /**
     * Returns the estimated time it takes to traverse the path in minutes
     * @return the estimated time it takes to traverse the path in minutes
     */
    public String getMinutesOfPath(){
        if (takeShortestRoute){
            return "" + MathUtil.round(costTo.getOrDefault(this.finish.getId(), 0.0), 2) + "min";
        }
        return "" + MathUtil.round(distTo.getOrDefault(this.finish.getId(), 0.0), 2) + "min";
    }

    /**
     * Returns a {@link LinkedHashMap} of type {@link TagWay} and {@link Double}. 
     * Where the key is the way used and the value is the distance traveled
     * @return a {@link LinkedHashMap} of type {@link TagWay} and {@link Double}. 
     */
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
}
