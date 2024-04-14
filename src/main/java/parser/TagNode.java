package parser;
import java.util.HashMap;

import gnu.trove.map.hash.TObjectDoubleHashMap;
/**
 * Class for storing a {@link HashMap} of a single node.
 * Contains the following tags:
 * <p>
 * {@link Node#ID}, {@link Node#LAT}, {@link Node#LON}
 * </p>
*/
public class TagNode extends Tag<Node> {

    private TObjectDoubleHashMap<Node> node = new TObjectDoubleHashMap<Node>();

    public TagNode(long id, double lat, double lon) {
        node = new TObjectDoubleHashMap<Node>(){
            {
                put(Node.ID, id);
                put(Node.LAT, lat);
                put(Node.LON, lon);
            }
        };
    }

    public TagNode(double lat, double lon) {
        node = new TObjectDoubleHashMap<Node>(){
            {
                put(Node.LAT, lat);
                put(Node.LON, lon);
            }
        };
    }

    public TagNode(XMLBuilder builder) {
        node = new TObjectDoubleHashMap<Node>(){
            {
                put(Node.ID, builder.getId());
                put(Node.LAT, builder.getLat());
                put(Node.LON, builder.getLon());
            }
        };
    }

    @Override
    public long getId(){
        return (long) node.get(Node.ID);
    }
    @Override
    public double getLat(){
        return node.get(Node.LAT);
    }
    @Override
    public double getLon(){
        return node.get(Node.LON);
    }
}
