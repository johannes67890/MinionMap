package parser;
import java.util.ArrayList;
import java.util.HashMap;

import gnu.trove.list.TLinkable;
import gnu.trove.list.linked.TLinkedList;
import parser.chunking.XMLWriter.ChunkFiles;
import structures.KDTree.Tree;
import util.Type;

import java.util.*;

/**
 * Class for storing a {@link HashMap} of a single node.
 * Contains the following tags:
 * <p>
 * {@link Node#ID}, {@link Node#LAT}, {@link Node#LON}
 * </p>
*/
public class TagNode extends Tag implements TLinkable<TagNode>, Comparable<TagNode>  {

    private long id;
    private TagWay parent;
    private Type type;
    private float lon;
    private float lat;
    private TagNode next;
    private TagNode prev;

    public TagNode(long id, float lat, float lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public TagNode(long id, float lat, float lon, TagWay way) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.parent = way;
    }

    public TagNode(float lat, float lon) {
        this.lon = lon;
        this.lat = lat;
    }

    public TagNode(XMLBuilder builder) {
        this.id = builder.getId();
        this.lon = builder.getLon();
        this.lat = builder.getLat();
    }
    
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

    public boolean equals(TagNode tN){

        if (this.id == tN.getId()){
            return true;
        }
        else{return false;}

    }
 
    public boolean hasIntersection(){
        return Tree.getTagFromPoint(this).size() > 1;
    }

    public ArrayList<Tag> getIntersectionTags(){
        // System.out.println(Tree.getTagFromPoint(this));
        return Tree.getTagFromPoint(this);
    }

    @Override
    public int compareTo(TagNode o) {
        return Long.compare(this.id, o.getId());
    }


    @Override
    public String toString() {
        return "TagNode{" +
                "id=" + id +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }

    @Override
    public TagNode getNext() {
        return next;
    }

    @Override
    public TagNode getPrevious() {
        return prev;
    }

    @Override
    public void setNext(TagNode linkable) {
        next = linkable;
    }

    @Override
    public void setPrevious(TagNode linkable) {
        prev = linkable;
    }

    public void revertNextPrev(){
        TagNode temp = getNext();


        setNext(getPrevious());
        setPrevious(temp);
        
    
    }
 

    public void setParent(TagWay linkable) {
        parent = linkable;
    }

    public boolean hasParent(){
        return this.parent != null;
    }

    public boolean hasParent(TagWay way){
        return this.parent.equals(way);
    }
    
    /**
     * Clears the links of the node.
     * <p>
     * Sets the next and previous nodes to null.
     * </p>
     * @see TLinkable
     */
    public void clearLinks(){
        next = null;
        prev = null;
    }

    /**
     * Iterates backwards through the linked list of nodes to find the parent way.
     * @return The {@link TagWay} parent of the node.
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
}
