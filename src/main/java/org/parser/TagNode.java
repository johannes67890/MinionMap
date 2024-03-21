package org.parser;
import java.math.BigDecimal;
import java.util.HashMap;

enum Node {
    ID, LAT, LON;
}
/**
 * Class for storing a {@link HashMap} of a single node.
 * Contains the following tags:
 * <p>
 * {@link Node#ID}, {@link Node#LAT}, {@link Node#LON}
 * </p>
*/
public class TagNode extends Tag<Node> {
    public TagNode(XMLReader.Builder builder) {
        super(new HashMap<Node, Number>(){
            {
                put(Node.ID, builder.getID());
                put(Node.LAT, builder.getLat());
                put(Node.LON, builder.getLon());
            }
        });
    }
    public TagNode(BigDecimal lat, BigDecimal lon) {
        super(new HashMap<Node, Number>(){
            {
                put(Node.LAT, lat);
                put(Node.LON, lon);
            }
        });
    }

    public BigDecimal getLat() {
        return (BigDecimal) this.get(Node.LAT);
    }
    public BigDecimal getLon() {
        return (BigDecimal) this.get(Node.LON);
    }

}
