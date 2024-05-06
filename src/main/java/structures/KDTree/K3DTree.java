package structures.KDTree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Stack;
import parser.Tag;
import parser.TagRelation;
import parser.TagWay;
import util.Type;

/**
 * 
 * This datastructure is made for finding points within a Rect3D.
 * The datastructure sorts the Point3D into dimensions, throught the Node class.
 * Great for maps!
 * 
 */
public class K3DTree {
    private Node root;
    private int size;
    private HashMap<Point2D, ArrayList<Tag>> pointToTag;
    public float[] bounds = new float[6];

    /**
     * Construct an empty set of points.
     */
    public K3DTree() {
        size = 0;
        pointToTag = new HashMap<>();
    }
    
    /**
     * Checks to see if there is any root node.
     * If not then there havent been inserted any nodes
     * 
     * @return {@code true} if this set is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }
    
    /**
     * @return the number of Point3Ds in the set.
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
     * This is a recursive function.
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
        
        putMap(p, node);
    }
    
    private Node insert(Node n, Point3D p, int xyz, float[] coords) {
        if (n == null) {
            size++;
            return new Node(p, coords);
        }
        
        double cmp = comparePoints(p, n, xyz);

        int xyztemp = xyz + 1;
        if (xyztemp > 2){
            xyztemp = 0;
        }
        
        /**
         * Traverse down the K3DTree.
         * 
         * Every level of the tree is a different dimension. First x-levels,
         * next y-levels, last z-levels. This means there are 2 sides of every dimension.
         * A lower or a higher number.
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
         */
        else if (!n.p.equals(p))
            n.rtf = insert(n.rtf, p, xyztemp, coords);
        
        /**
         * If the Point3D already exists in the K3DTree then do nothing.
         */
        return n;
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
        
        LinkedList<Tag> returnList = new LinkedList<>();
        
        // Handle KdTree without a root node yet
        if (root == null) return new HashSet<>(returnList);
        
        Stack<Node> nodes = new Stack<>();
        nodes.push(root);
        while (!nodes.isEmpty()) {
            
            // Examine the next Node
            Node tmp = nodes.pop();
            
            // Add contained points to our points stack
            if (rect.contains(tmp.p)){
                //ArrayList<Tag> temp = getMap(tmp.p);
                returnList.addAll(getMap(tmp.p));
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
        return new HashSet<>(returnList);
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
        Point3D point = nearest(root, p, root.p, 0);
        return point;
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
            if (champion.distanceSquaredTo(p) >= toPartitionLine * toPartitionLine) {
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
            if (champion.distanceSquaredTo(p) <= toPartitionLine * toPartitionLine) {
                champion = nearest(n.lbb, p, champion, temp);
            }
        }
        
        return champion;
    }

    /**
     * Finds the nearest neighbor of the Point3D. 
     * 
     * This does not WORK!
     * 
     * @param p the point from which to search for a neighbor
     * @return the nearest neighbor to the given point p,
     *         {@code null} otherwise.
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public Point3D nearest(Point3D p, List<Type> searchTypes) {
        if (p == null) throw new java.lang.NullPointerException(
                "called contains() with a null Point2D");
        if (isEmpty()) return null;
        return nearest(root, p, root.p, 0, searchTypes);
    }

    private Point3D nearest(Node n, Point3D p, Point3D champion, int xyz, List<Type> types) {
        

        // Handle reaching the end of the tree
        if (n == null){
            return champion;
        } 
        
        // Handle the given point exactly overlapping a point in the BST
        if (n.p.equals(p) && isPointOfTypes(p, types)){
            return p;
        }
        
        // Determine if the current Node's point beats the existing champion
        if (n.p.distance2DTo(p) < champion.distance2DTo(p) && isPointOfTypes(n.p, types)){
            champion = n.p;
        }
        
        /**
         * Calculate the distance from the search point to the current
         * Node's partition line. Also calculates the next dimension to look at.
         */
        double toPartitionLine = comparePoints(p, n, xyz);
        int temp = xyz + 1;
        if (xyz > 2){
            //toPartitionLine = 0;
            temp = 0;
        }
        /**
         * Handle the search point being to the left of or below
         * the current Node's point.
         */
        if (toPartitionLine < 0) {
            champion = nearest(n.lbb, p, champion, temp, types);
            // Since champion may have changed, recalculate distance
            if (champion.distanceSquaredTo(p) >= toPartitionLine * toPartitionLine) {
                champion = nearest(n.rtf, p, champion, temp, types);
            }
        }
        
        /**
         * Handle the search point being to the right of or above
         * the current Node's point.
         */
        else {
            champion = nearest(n.rtf, p, champion, temp, types);
            // Since champion may have changed, recalculate distance
            if (champion.distanceSquaredTo(p) >= toPartitionLine * toPartitionLine) {
                champion = nearest(n.lbb, p, champion, temp, types);
            }
        }
        
        return champion;
    }
    

    /**
     * A function to determine if a Point3D have a Way or Relation of specific
     * type associated with it.
     * @param p     The point in question
     * @param types The types in question
     * @return      Returns true if the Point3D have an associated tag to it of the specified type,
     * else return false.
     */
    private boolean isPointOfTypes(Point3D p, List<Type> types){
        for (Tag tag : getMap(p)){
            if (tag instanceof TagWay){
                if (types.contains(tag.getType())){
                    return true;
                }
            }else if (tag instanceof TagRelation){
                if (types.contains(tag.getType())){
                    
                    return true;
                }
            }
        }
        return false;
    }

    public Point3D nearestBruteForce(Point3D point){
        Point3D best = null;
        for (Point2D other : pointToTag.keySet()){
            Point3D other3D = new Point3D((float)other.x(), (float)other.y(), (byte)0);
            if (best == null){
                best = other3D;
                continue;
            }

            if (point.distance2DTo(other3D) < point.distance2DTo(best)){
                best = other3D;
            }
        }

        return best;
    }

    public Point3D nearestBruteForce(Point3D point, List<Type> types){
        Point3D best = null;
        for (Point2D other : pointToTag.keySet()){
            Point3D other3D = new Point3D((float)other.x(), (float)other.y(), (byte)0);
            boolean isType = isPointOfTypes(other3D, types);
            if (isType && best == null){
                best = other3D;
                continue;
            }

            if (isType && point.distance2DTo(other3D) < point.distance2DTo(best)){
                best = other3D;
            }
        }

        return best;
    }

    /**
     * Returns the neaerst Point3D through brute-force. Looking through all Points in the HashMap<Point2D, Tag>,
     * returning the Point3D with the shortest distance to the parameter point, with the Class type of classType.
     * @param point     The Point3D from which the nearest search should start
     * @param classType The type of class in question, needed for whitelist
     * @return          The nearest Point3D with a Tag of Class type classType
     */
    public Point3D nearestBruteForce(Point3D point, Class<?> classType){
        Point3D best = null;
        for (Point2D other : pointToTag.keySet()){
            boolean isOfClass = false;
            Point3D other3D = new Point3D((float)other.x(), (float)other.y(), (byte)0);
            for (Tag tag : getMap(other3D)){
                if (tag.getClass() == classType){
                    isOfClass = true;
                    break;
                }
            }

            if (isOfClass && best == null){
                best = other3D;
                continue;
            }

            if (isOfClass && point.distance2DTo(other3D) < point.distance2DTo(best)){
                best = other3D;
            }
        }

        return best;
    }
    
    

    /**
     * This method gets an ArrayList of tags (Tag<?>) which is associated with the nearest Point3D in relation to the {@link point}
     * in the parameters
     * @param point the point from where the search starts from
     * @return a list of Tags thats is connected to the the nearest Point3D in the KDTree
     */
    public ArrayList<Tag> nearestTags(Point3D point){
        return getMap(nearest(point));
    }


    /**
     * This method gets an ArrayList of tags (Tag<?>) which is associated with the nearest Point2D in relation to the {@link point}
     * in the parameters
     * @param point the point from where the search starts from
     * @return a list of Tags thats is connected to the the nearest Point2D in the KDTree
     */
    public ArrayList<Tag> nearestTags(Point3D point, List<Type> searchClass){
        return getMap(nearest(point, searchClass));
    }
    
    /**
     * This method gets the Tags ({@link Tag}) related to the point given in the parameters
     * @param point is the point that you want the Tags related to
     * @return this return an ArrayList<Tag<?>> of all tags related to the given Point3D
     */
    public ArrayList<Tag> getTagsFromPoint(Point3D point){
        return getMap(point);
    }

    /**
     * A method for putting elements into the hashmap with a point3D even when the map is point2D
     * This is made because we don't care about the hierachy levels in the hashmap, only in the K3DTree
     * @param point The given key point
     * @param tag   The given tag that needs to be associated with the key
     */
    public void putMap(Point3D point, Tag tag){
        ArrayList<Tag> tempList = pointToTag.getOrDefault(new Point2D(point.x(), point.y()), new ArrayList<>());
        tempList.add(tag);
        pointToTag.put(new Point2D(point.x(), point.y()), tempList);
    }

    /**
     * A method for getting items from the map from a Point3D even when the map is Point2D based.
     * This is made because we don't care about the hierachy levels in the hashmap, only in the K3DTree.
     * @param point The given Point3D we need to find the element for
     * @return      Returns a ArrayList<Tag> from the hashmap
     */
    private ArrayList<Tag> getMap(Point3D point){
        return pointToTag.getOrDefault(new Point2D(point.x(), point.y()), new ArrayList<>(1));
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
     * The data structure from which a K3DTree is created. This is created for each Point3D in the K3DTree.
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
