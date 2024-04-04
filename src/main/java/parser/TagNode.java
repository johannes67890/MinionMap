package parser;
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
public class TagNode extends HashMap<Node,Number> {

    public TagNode(long id, BigDecimal lat, BigDecimal lon) {
        super(new HashMap<Node, Number>(){
                {
                    put(Node.ID, id);
                    put(Node.LAT, lat);
                    put(Node.LON, lon);
                }
            }
        );
    }
    public TagNode(long id, double lat, double lon) {
        super(new HashMap<Node, Number>(){
                {
                    put(Node.ID, id);
                    put(Node.LAT, lat);
                    put(Node.LON, lon);
                }
            }
        );
    }

    public TagNode(XMLReader.Builder builder) {
        super(new HashMap<Node, Number>(){
            {
                put(Node.ID, builder.getId());
                put(Node.LAT, builder.getLat());
                put(Node.LON, builder.getLon());
            }
        });
    }

    public Long getId(){
        return (Long) this.get(Node.ID);
    }
    
    public BigDecimal getLat(){
        return (BigDecimal) this.get(Node.LAT);
    }

    public BigDecimal getLon(){
        return (BigDecimal) this.get(Node.LON);
    }
    public Double getLatDouble(){
        return (double) -this.get(Node.LAT).doubleValue();
    }

    public Double getLonDouble(){
        return (double) 0.56 * this.get(Node.LON).doubleValue();
    }


}
