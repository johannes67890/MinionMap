package parser;
import java.util.HashMap;


import gnu.trove.list.TLinkable;
import gnu.trove.list.linked.TLinkedList;
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
    private ArrayList<TagWay> parent = new ArrayList<>();
    private float lon;
    private float lat;
    private TagNode next;
    private TagNode prev;

    public TagNode(long id, float lat, float lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public TagNode(long id, float lat, float lon, ArrayList<TagWay> way) {
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
        throw new UnsupportedOperationException("TagNode does not have a type.");
    }

    public boolean equals(TagNode tN){

        if (this.id == tN.getId()){
            return true;
        }
        else{return false;}

    }
 
    @Override
    public int compareTo(TagNode o) {
        return Long.compare(this.id, o.getId());
    }

    public double distance(TagNode node){
        return Math.sqrt(Math.pow(node.getLat() - getLat(), 2) + (Math.pow(node.getLon() - getLon(), 2)));
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


    public ArrayList<TagWay> getParents() {
        return parent;
    }

    @Override
    public void setNext(TagNode linkable) {
        next = linkable;
    }

    @Override
    public void setPrevious(TagNode linkable) {
        prev = linkable;
    }
    
    public void setParent(TagWay linkable) {
        parent.add(linkable);
    }

    public boolean hasParent(TagWay way){
        return parent.contains(way);
    }

    public void clearLinks(){
        next = null;
        prev = null;
    }
}
