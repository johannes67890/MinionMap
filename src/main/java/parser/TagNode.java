package parser;
import java.util.HashMap;

/**
 * Class for storing a {@link HashMap} of a single node.
 * Contains the following tags:
 * <p>
 * {@link Node#ID}, {@link Node#LAT}, {@link Node#LON}
 * </p>
*/
public class TagNode extends Tag implements Comparable<TagNode> {

    long id;
    float lon;
    float lat;

    public TagNode(long id, float lat, float lon) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
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

}
