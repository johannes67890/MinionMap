package parser;
import java.util.ArrayList;

import gnu.trove.list.TLinkable;
import structures.KDTree.Tree;
import util.Type;

/**
    * Class representing a node in the OSM XML file.
    * <p>
    * 
    * <ul>
    * <li>{@link #id} - The id of the node.</li>
    * <li>{@link #lat} - The latitude of the node.</li>
    * <li>{@link #lon} - The longitude of the node.</li>
    * <li>{@link #parent} - The parent way of the node.</li>
    * <li>{@link #next} - The next node in the list.</li>
    * <li>{@link #prev} - The previous node in the list.</li>
    * </ul>
    * </p>
    * @implNote This class implements the {@link TLinkable} interface to allow for linking of nodes in a list.
*/
public class TagNode extends Tag implements TLinkable<TagNode> {

    private long id;
    private TagWay parent;
    private Type type;
    private float lon;
    private float lat;
    private TagNode next;
    private TagNode prev;

    /**
     * Create a new TagNode from the {@link XMLBuilder}
     * @param builder
     */
    public TagNode(XMLBuilder builder) {
        this.id = builder.getId();
        this.lon = builder.getLon();
        this.lat = builder.getLat();
    }

    /**
     * Create a new TagNode with the given values.
     * @param id - The id of the node.
     * @param lat - The latitude of the node.
     * @param lon - The longitude of the node.
     * @param way - The parent way of the node.
     */
    public TagNode(long id, float lat, float lon, TagWay way) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.parent = way;
    }

    /**
     * Create a new TagNode with the given values.
     * @param id - The id of the node.
     * @param lat - The latitude of the node.
     * @param lon - The longitude of the node.
     */
    public TagNode(long id, float lat, float lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Create a new TagNode with the given values.
     * @param lat - The latitude of the node.
     * @param lon - The longitude of the node.
     */
    public TagNode(float lat, float lon) {
        this.lon = lon;
        this.lat = lat;
    }

    /**
     * Copy a {@link TagNode} to a new, with the same field values.
     */
    public TagNode(TagNode other) {
        this.id = other.id;
        this.lat = other.lat;
        this.lon = other.lon;
        this.next = other.next;
        this.prev = other.prev;
    }

    @Override
    public long getId(){
        return id;
    }
    @Override
    public float getLat(){
        return this.lat;
    }
    @Override
    public float getLon(){
        return this.lon;
    }

    @Override
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Check if the node is equal to another node, by comparing the {@link TagNode#id}.
     * @param tN - The node to compare to.
     * @return {@code true} if the nodes are equal by {@link TagNode#id}, {@code false} otherwise.
     */
    public boolean equals(TagNode tN){
        if (this.id == tN.getId()) return true;
        else return false;
    }

    @Override
    public String toString() {
        return "TagNode{" +
                "id=" + id +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }

    /**
     * Get the next node in the list.
     * @see TLinkable - The interface used to link nodes in a list.
     * @return The next {@link TagNode}
     */
    @Override
    public TagNode getNext() {
        return next;
    }

    /**
     * Get the previous node in the list.
     * @see TLinkable - The interface used to link nodes in a list.
     * @return The previous {@link TagNode}
     */
    @Override
    public TagNode getPrevious() {
        return prev;
    }

    /**
     * Set the next node in the list.
     * @see TLinkable - The interface used to link nodes in a list.
     * @param linkable - The next {@link TagNode} in the list.
     */
    @Override
    public void setNext(TagNode linkable) {
        next = linkable;
    }

    /**
     * Set the previous node in the list.
     * @see TLinkable - The interface used to link nodes in a list.
     * @param linkable - The previous {@link TagNode} in the list.
     */
    @Override
    public void setPrevious(TagNode linkable) {
        prev = linkable;
    }

    /**
     * Clears the links of the node.
     * <p>
     * Sets the {@link TagNode#next} and {@link TagNode#prev} nodes to {@code null}.
     * </p>
     * @see TLinkable - The interface used to link nodes in a list.
     */
    public void clearLinks(){
        next = null;
        prev = null;
    }

    /**
     * Reverts the next and previous nodes in the list.
     * <p>
     * Swaps the {@link TagNode#next} and {@link TagNode#prev} nodes.
     * </p>
     */
    public void revertNextPrev(){
        TagNode temp = getNext();

        setNext(getPrevious());
        setPrevious(temp);
    }

    /**
     * Set the parent way of the node.
     * <p>
     * Sets a {@link TagWay} to the {@link TagNode#parent} of the node.
     * </p>
     */
    public void setParent(TagWay linkable) {
        parent = linkable;
    }

    /**
     * Check if the node has a parent {@link TagWay}.
     * @return {@code true} if the node has a parent way, {@code false} otherwise.
     */
    public boolean hasParent(){
        return this.parent != null;
    }

    /**
     * Check if the node has a parent that is the equal to the object to the given {@link TagWay}.
     * @param way
     * @return
     */
    public boolean hasParent(TagWay way){
        return this.parent.equals(way);
    }

    /**
     * Iterates backwards through the linked list of nodes to find the parent way.
     * @return The {@link TagWay} parent of the node. {@code null} if no parent is found.
     */
    public TagWay getParent(){
        TagWay p = null;
        TagNode currN = this;

        if(currN.hasParent()){
            return currN.parent;
        }else{
            while(!currN.hasParent()){
                currN = currN.getPrevious();
                if(currN.hasParent()){
                    p = currN.getParent();
                    break;
                }
            }
        }
        return p;
    }

    /**
     * Check the node for intersections with other nodes.
     * <p>
     * With the use of the {@link Tree}, checks if the node has more than one in the same location.
     * </p>
     * @see Tree - The KDTree used to store and search for nodes.
     * @return {@code true} if the node has intersections, {@code false} otherwise.
     */
    public boolean hasIntersection(){
        return Tree.getTagFromPoint(this).size() > 1;
    }

    /**
     * Get the {@link Tag}s of the node that intersect with the node.
     * <p>
     * With the use of the {@link Tree}, gets the tags of the nodes that intersect with the node.
     * </p>
     * @see Tree - The KDTree used to store and search for nodes.
     * @return The {@link ArrayList} of {@link Tag} that intersect with the node.
     */
    public ArrayList<Tag> getIntersectionTags(){
        return Tree.getTagFromPoint(this);
    }
}
