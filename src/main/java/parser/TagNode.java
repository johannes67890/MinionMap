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

    public TagNode(long id, double lat, double lon) {
        super(new HashMap<Node, Object>(){
            {
                put(Node.ID, id);
                put(Node.LAT, lat);
                put(Node.LON, lon);
            }
        });
    }

    public TagNode(XMLBuilder builder) {
        super(new HashMap<Node, Object>(){
            {
                put(Node.ID, builder.getId());
                put(Node.LAT, builder.getLat());
                put(Node.LON, builder.getLon());
            }
        });
    }

    @Override
    public long getId(){
        return ((long) this.get(Node.ID));
    }
    @Override
    public double getLat(){
        return ((double) this.get(Node.LAT));
    }
    @Override
    public double getLon(){
        return ((double) this.get(Node.LON));
    }


    public double distance(TagNode node){
        return Math.sqrt(Math.pow(node.getLat() - getLat(), 2) + (Math.pow(node.getLon() - getLon(), 2)));
    }


 
}
