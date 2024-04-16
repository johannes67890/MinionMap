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
        return Long.parseLong(this.get(Node.ID).toString());
    }
    @Override
    public double getLat(){
        return Double.parseDouble(this.get(Node.LAT).toString());
    }
    @Override
    public double getLon(){
        return Double.parseDouble(this.get(Node.LON).toString());
    }


    public double distance(TagNode node){
        return Math.sqrt(Math.pow(node.getLat() - getLat(), 2) + (Math.pow(node.getLon() - getLon(), 2)));
    }


 
}
