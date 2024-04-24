package util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import util.Point3D;
import util.Rect3D;
import edu.princeton.cs.algs4.Stack;
import parser.Tag;

/*
 * Copyright (C) 2016 Michael <GrubenM@GMail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * 
 * This datastructure is made for finding points within a Rect3D.
 * Great for maps!
 * 
 */
public class K3DTree {
    private Node root;
    private int size;
    private HashMap<Point3D, ArrayList<Tag>> pointToTag;
    public float[] bounds = new float[6];

    /**
     * Construct an empty set of points.
     */
    public K3DTree() {
        size = 0;
        pointToTag = new HashMap<>();
    }
    
    /**
     * Checks if there is a node at the root, if not it
     * is safe to assume that the tree is empty.
     * 
     * @return {@code true} if this set is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }
    
    /**
     * @return the number of points in the set.
     */
    public int size() {
        return size;
    }

    /*
     * Set the bounds for the coordinates possible in the KdTree
     */
    public void setBound(float x_min, float y_min, float z_min, float x_max, float y_max, float z_max){
        bounds = new float[] {x_min, y_min, z_min, x_max, y_max, z_max};
    }

    /*
     * Getter function for the bounds
     */
    public float[] getBounds(){
        return bounds;
    }
    
    /**
     * Add the point to the set (if it is not already in the set).
     * 
     * When inserted it takes the x-coordinate into account, then
     * y-coordinates and then last z-coordinates. After that it goes
     * back to x-coordinates and so forth.
     * 
     * It also uses a Tag to bind the point3d to the tag within a hashmap.
     * 
     * @param p the point to add
     * @param node the tag ({@link Tag}) associated with the Point3D
     * @throws NullPointerException if {@code p} is {@code null}
     */
    
    public void insert(Point3D p, Tag node) {
        if (p == null) {
            throw new java.lang.NullPointerException("called insert() with a null Point3D");
        }
        
        // new double[] {x_min, y_min, x_max, y_max)
        if(bounds != null) {
            root = insert(root, p, 0, bounds.clone());
        } else {
            root = insert(root, p, 0, new float[] {-180, -180, 180, 180});
        }
        
        ArrayList<Tag> list = pointToTag.getOrDefault(node, new ArrayList<>());
        list.add(node);
        pointToTag.put(p, list);
    }
    
    private Node insert(Node n, Point3D p, int xyz, float[] coords) {
        if (n == null) {
            size++;
            // double xmin, double ymin, double xmax, double ymax
            //System.out.println("xmin: " + coords[0] + "| ymin: " + coords[1] + "| xmax: " + coords[2] + "| ymax: " + coords[3]);
            return new Node(p, coords);
        }
        
        double cmp = comparePoints(p, n, xyz);

        int xyztemp = xyz + 1;
        if (xyztemp > 2){
            xyztemp = 0;
        }
        
        /**
         * Traverse down the BST.
         * 
         * In subsequent levels, the orientation is orthogonal
         * to the current orientation.
         * 
         * Place the point in the left or right nodes accordingly.
         * 
         * If the comparison is not affirmatively left or right, then it could
         * be that we're considering literally the same point, in which case
         * the size shouldn't increase, or that we're considering a point
         * which lies on the same partition line, which would need to be added
         * to the BST and increase the size accordingly.
         */
        
        // Handle Nodes which should be inserted to the left
        if (cmp < 0 && xyz == 0) {
            coords[3] = n.p.x(); // lessen x_max
            n.lbb = insert(n.lbb, p, xyz+1, coords);
        }
        
        // Handle Nodes which should be inserted to the bottom
        else if (cmp < 0 && xyz == 1) {
            coords[4] = n.p.y(); // lessen y_max
            n.lbb = insert(n.lbb, p, xyz+1, coords);
        }

        // Handle Nodes which should be inserted to the back
        else if (cmp < 0 && xyz == 2){
            coords[5] = n.p.z();
            n.lbb = insert(n.lbb, p, 0, coords);
        }
        
        // Handle Nodes which should be inserted to the right
        else if (cmp > 0 && xyz == 0) {
            coords[0] = n.p.x(); // increase x_min
            n.rtf = insert(n.rtf, p, xyz+1, coords);
        }
        
        // Handle Nodes which should be inserted to the top
        else if (cmp > 0 && xyz == 1) {
            coords[1] = n.p.y(); // increase y_min
            n.rtf = insert(n.rtf, p, xyz+1, coords);
        }

        // Handle Nodes which should be inserted to the front
        else if (cmp > 0 && xyz == 2){
            coords[2] = n.p.z();
            n.rtf = insert(n.rtf, p, 0, coords);
        }
        
        /**
         * Handle Nodes which lie on the same partition line, 
         * but aren't the same point.
         * 
         * As per the checklist, these "ties" are resolved in favor of the
         * right subtree.
         * 
         * It is assumed that the Rect3D to be created cannot be shrunk
         * at all, and so none of coords[] values are updated here.
         */
        else if (!n.p.equals(p))
            n.rtf = insert(n.rtf, p, xyztemp, coords);
        
        /**
         * Do nothing for a point which is already in the BST.
         * This is because the BST contains a "set" of points.
         * Hence, duplicates are silently dropped, rather than
         * being added.
         */
        return n;
    }
    
    /**
     * Does the set contain point p?
     * 
     * In the worst case, this implementation takes time proportional to the
     * logarithm of the number of points in the set.
     * 
     * @param p the point to look for
     * @return {@code true} if the set contains point p;
     *         {@code false} otherwise
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public boolean contains(Point3D p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called contains() with a null Point3D");
        return contains(root, p, 0);
    }
    
    private boolean contains(Node n, Point3D p, int xyz) {

        // Handle reaching the end of the search
        if (n == null) return false;
        
        // Check whether the search point matches the current Node's point
        if (n.p.equals(p)) return true;
        
        double cmp = comparePoints(p, n, xyz);
        
        int temp = xyz + 1;
        if (xyz > 2){
            temp = 0;
        }
        // Traverse the left path when necessary
        if (cmp < 0) return contains(n.lbb, p, temp);
        
        // Traverse the right path when necessary, and as tie-breaker
        else return contains(n.rtf, p, temp); 
    }
    
    /**
     * All points that are inside the rectangle.
     * 
     * In the worst case, this implementation takes time
     * proportional to the number of points in the set.
     * 
     * However, unlike PointSET.range(), in the best case, this implementation
     * takes time proportional to the logarithm of the number of
     * points in the set.
     * 
     * @param rect the Rect3D within which to look for points
     * @return an iterator to all of the points within the given Rect3D
     * @throws NullPointerException if {@code rect} is {@code null}
     */
    

    public HashSet<Tag> rangeNode(Rect3D rect) {
        if (rect == null) throw new java.lang.NullPointerException(
                "called range() with a null Rect3D");
        
        //Stack<Point3D> points = new Stack<>();
        //ArrayList<Tag<?>> returnList = new ArrayList<>();
        HashSet<Tag> returnList = new HashSet<>();
        
        // Handle KdTree without a root node yet
        if (root == null) return returnList;
        
        Stack<Node> nodes = new Stack<>();
        nodes.push(root);
        while (!nodes.isEmpty()) {
            
            // Examine the next Node
            Node tmp = nodes.pop();
            
            // Add contained points to our points stack
            if (rect.contains(tmp.p)){
                //points.push(tmp.p);
                ArrayList<Tag> temp = pointToTag.get(tmp.p);
                returnList.addAll(temp);
            }
            /**
             * Add Nodes containing promising rectangles to our nodes stack.
             * 
             * Note that, since we don't push Nodes onto the stack unless
             * their rectangles intersect with the given Rect3D, we achieve
             * pruning as we traverse the BST.
             */
            if (tmp.lbb != null && rect.intersects(tmp.lbb.rect)) {
                nodes.push(tmp.lbb);
            }
            if (tmp.rtf != null && rect.intersects(tmp.rtf.rect)) {
                nodes.push(tmp.rtf);
            }
        }
        return returnList;
    }
    
    /**
     * A nearest neighbor in the set to point p; null if the set is empty.
     * 
     * In the worst case, this implementation takes time
     * proportional to the number of points in the set.
     * 
     * However, unlike PointSET.nearest(), in the best case, this
     * implementation takes time proportional to the logarithm of
     * the number of points in the set.
     * 
     * @param p the point from which to search for a neighbor
     * @return the nearest neighbor to the given point p,
     *         {@code null} otherwise.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public Point3D nearest(Point3D p) {
        if (p == null) throw new java.lang.NullPointerException(
                "called contains() with a null Point3D");
        if (isEmpty()) return null;
        return nearest(root, p, root.p, 0);
    }
    
    private Point3D nearest(Node n, Point3D p, Point3D champion, int xyz) {
        
        // Handle reaching the end of the tree
        if (n == null) return champion;
        
        // Handle the given point exactly overlapping a point in the BST
        if (n.p.equals(p)) return p;
        
        // Determine if the current Node's point beats the existing champion
        if (n.p.distanceSquaredTo(p) < champion.distanceSquaredTo(p))
            champion = n.p;
        
        /**
         * Calculate the distance from the search point to the current
         * Node's partition line.
         * 
         * Primarily, the sign of this calculation is useful in determining
         * which side of the Node to traverse next.
         * 
         * Additionally, the magnitude to toPartitionLine is useful for pruning.
         * 
         * Specifically, if we find a champion whose distance is shorter than
         * to a previous partition line, then we know we don't have to check any
         * of the points on the other side of that partition line, because none
         * can be closer.
         */
        double toPartitionLine = comparePoints(p, n, xyz);
        int temp = xyz + 1;
        if (xyz > 2){
            temp = 0;
        }
        /**
         * Handle the search point being to the left of or below
         * the current Node's point.
         */
        if (toPartitionLine < 0) {
            champion = nearest(n.lbb, p, champion, temp);
            
            // Since champion may have changed, recalculate distance
            if (champion.distanceSquaredTo(p) >=
                    toPartitionLine * toPartitionLine) {
                champion = nearest(n.rtf, p, champion, temp);
            }
        }
        
        /**
         * Handle the search point being to the right of or above
         * the current Node's point.
         * 
         * Note that, since insert() above breaks point comparison ties
         * by placing the inserted point on the right branch of the current
         * Node, traversal must also break ties by going to the right branch
         * of the current Node (i.e. to the right or top, depending on
         * the level of the current Node).
         */
        else {
            champion = nearest(n.rtf, p, champion, temp);
            
            // Since champion may have changed, recalculate distance
            if (champion.distanceSquaredTo(p) >=
                    toPartitionLine * toPartitionLine) {
                champion = nearest(n.lbb, p, champion, temp);
            }
        }
        
        return champion;
    }

    /**
     * This method gets an ArrayList of tags (Tag<?>) which is associated with the nearest Point3D in relation to the {@link point}
     * in the parameters
     * @param point the point from where the search starts from
     * @return a list of Tags thats is connected to the the nearest Point3D in the KDTree
     */
    public ArrayList<Tag> nearestTags(Point3D point){
        return pointToTag.get(nearest(point));
    }
    
    /**
     * This method gets the Tags ({@link Tag}) related to the point given in the parameters
     * @param point is the point that you want the Tags related to
     * @return this return an ArrayList<Tag<?>> of all tags related to the given Point3D
     */
    public ArrayList<Tag> getTagsFromPoint(Point3D point){
        return pointToTag.get(point);
    }
    
    /**
     * The distance and direction from the given point to the given Node's
     * partition line.
     * 
     * If the sign of the returned double is negative, then the given point
     * lies or should lie on the left branch of the given Node.
     * 
     * Otherwise (including where the difference is exactly 0), then the
     * given point lies or should lie on the right branch of the given Node.
     * 
     * @param p the point in question
     * @param n the Node in question
     * @param evenLevel is the current level even?  If so, then the Node's
     *        partition line is vertical, so the point will be to the left
     *        or right of the Node.  If not, then the Node's partition line
     *        is horizontal, so the point will be above or below the Node.
     * @return the distance and direction from p to n's partition line
     */
    private double comparePoints(Point3D p, Node n, int xyz) {
        if (xyz == 0) {
            return p.x() - n.p.x();
        }else if (xyz == 1){
            return p.y() - n.p.y();
        }else{
            return p.z() - n.p.z();
        }
    }
    
    /**
     * The data structure from which a KdTree is created.
     */
    private static class Node {
        
        // the point
        private final Point3D p;
        
        // the axis-aligned rectangle corresponding to this node
        private final Rect3D rect;
        
        // the left/bottom/back subtree
        private Node lbb;
        
        // the right/top/front subtree
        private Node rtf;
        
        private Node(Point3D p, float[] coords) {
            this.p = p;
            rect = new Rect3D(coords[0], coords[1], (int) coords[2], coords[3], coords[4], (int) coords[5]);
        }
    }
}
