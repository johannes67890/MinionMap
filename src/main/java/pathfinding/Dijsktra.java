package pathfinding;

import parser.TagNode;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;


public class Dijsktra {
    private HashMap<TagNode, Double> distTo = new HashMap<TagNode, Double>();
    private HashMap<TagNode, TagNode> edgeTo = new HashMap<TagNode, TagNode>();
    private Set<TagNode> visited = new HashSet<TagNode>();

    Digraph G;

    public Dijsktra(Digraph G, TagNode start, TagNode end) {
        visited.add(start);
        distTo.put(start, 0.0);
        this.G = G;

        G.edges().forEach(e -> {
            TagNode v = e.either();
            TagNode w = e.other(v);
            setInfinity(v);
            setInfinity(w);
        });

        findShortestPath();

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

    private void relax(TagNode v, TagNode w, double weight) {
        if (!distTo.containsKey(w)) {
            distTo.put(w, Double.POSITIVE_INFINITY);
        }
        if (distTo.get(w) > distTo.get(v) + weight) {
            distTo.put(w, distTo.get(v) + weight);
            edgeTo.put(w, v);
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

    public void findShortestPath() {
        while (visited.size() < G.V()) {
            TagNode v = minDistance();
            visited.add(v);
            for (Edge e : G.adj(v)) {
                TagNode w = e.other(v);
                double weight = e.weight();
                relax(v, w, weight);
            }
        }
    }

    public double getDistanceTo(TagNode v) {
        return distTo.get(v);
    }

    public Iterable<TagNode> pathTo(TagNode v) {
        if (!visited.contains(v)) {
            return null;
        }
        Stack<TagNode> path = new Stack<TagNode>();
        for (TagNode x = v; x != null; x = edgeTo.get(x)) {
            path.push(x);
        }
        return path;
    }
}
