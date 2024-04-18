package parser;
import java.util.HashMap;

/**
 * Class for storing a {@link HashMap} of a single node.
 * Contains the following tags:
 * <p>
 * {@link Node#ID}, {@link Node#LAT}, {@link Node#LON}
 * </p>
*/
public class TagNode extends Tag<Node> {

    long id;
    double lon;
    double lat;

    public TagNode(long id, double lat, double lon) {
        super(new HashMap<Node, Object>(){
            {
                //put(Node.ID, id);
                //put(Node.LAT, lat);
               // put(Node.LON, lon);
            }
        });

        this.id = id;
        this.lon = lon;
        this.lat = lat;

    }

    public TagNode(XMLBuilder builder) {
        super(new HashMap<Node, Object>(){
            {
                put(Node.ID, builder.getId());
                //put(Node.LAT, builder.getLat());
                //put(Node.LON, builder.getLon());
            }
        });

        this.id = builder.getId();
        this.lon = builder.getLon();
        this.lat = builder.getLat();
    }

    @Override
    public long getId(){
        return id;
    }
    @Override
    public double getLat(){
        return this.lat;
    }
    @Override
    public double getLon(){
        return this.lon;
    }


    public double distance(TagNode node){
        return Math.sqrt(Math.pow(node.getLat() - getLat(), 2) + (Math.pow(node.getLon() - getLon(), 2)));
    }


 
}
