package parser;
import java.util.HashMap;

import gnu.trove.list.TLinkable;
import gnu.trove.map.hash.TObjectDoubleHashMap;
/**
 * Class for storing a {@link HashMap} of a single node.
 * Contains the following tags:
 * <p>
 * {@link Node#ID}, {@link Node#LAT}, {@link Node#LON}
 * </p>
*/
public class TagNode extends Tag<Node, TObjectDoubleHashMap<Node>> {
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
    public TObjectDoubleHashMap<Node> getMap() {
        return this.node;
    }

    @Override
    public long getId() {
        return (long) this.node.get(Node.ID);
    }

    @Override
    public double getLat(){
        return this.node.get(Node.LAT);
    }
    @Override
    public double getLon(){
        return this.node.get(Node.LON);
    }

    @Override  
    public boolean isEmpty(){
        return this.node.isEmpty();
    }

    
    public double distance(TagNode node){
        return Math.sqrt(Math.pow(node.getLat() - getLat(), 2) + (Math.pow(node.getLon() - getLon(), 2)));
    }
}