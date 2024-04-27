package parser;
import java.util.HashMap;

import gnu.trove.list.TLinkable;
import gnu.trove.list.linked.TLinkedList;
import java.util.ArrayList;

/**
 * Class for storing a {@link HashMap} of a single node.
 * Contains the following tags:
 * <p>
 * {@link Node#ID}, {@link Node#LAT}, {@link Node#LON}
 * </p>
*/
public class TagNode extends Tag implements TLinkable<TagNode>  {

    private long id;
    private ArrayList<TagWay> parent = new ArrayList<>();
    private float lon;
    private float lat;
    private ArrayList<TagNode> next = new ArrayList<>();
    private ArrayList<TagNode> prev = new ArrayList<>();

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

    public boolean equals(TagNode tN){

        if (this.id == tN.getId()){
            return true;
        }
        else{return false;}

    }

    public double distance(TagNode node){
        return Math.sqrt(Math.pow(node.getLat() - getLat(), 2) + (Math.pow(node.getLon() - getLon(), 2)));
    }

    @Override
    public TagNode getNext() {
        if(next.isEmpty()) return null;
        return next.get(0);
       
    }

    @Override
    public TagNode getPrevious() {
        if(prev.isEmpty()) return null;
        return prev.get(0);
       
    }

    public ArrayList<TagWay> getParents() {
        return parent;
    }

    @Override
    public void setNext(TagNode linkable) {
        next.add(linkable);
    }

    @Override
    public void setPrevious(TagNode linkable) {
        prev.add(linkable);
    }
    
    public void setParent(TagWay linkable) {
        parent.add(linkable);
    }

}
