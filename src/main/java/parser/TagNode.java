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

    int id;
    float lon;
    float lat;

    public TagNode(int id, float lat, float lon) {
        
        /*super(new HashMap<Node, Object>(){
            {
                //put(Node.ID, id);
                //put(Node.LAT, lat);
               // put(Node.LON, lon);
            }
        });*/

        this.id = id;
        this.lon = lon;
        this.lat = lat;

    }

    public TagNode(XMLBuilder builder) {
        /*super(new HashMap<Node, Object>(){
            {
                //put(Node.ID, builder.getId());
                //put(Node.LAT, builder.getLat());
                //put(Node.LON, builder.getLon());
            }
        });*/

        this.id = builder.getIdasInt();
        this.lon = builder.getLon();
        this.lat = builder.getLat();
    }

    @Override
    public long getId(){
        return id;
    }

    public int getIdasInt(){
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


 
}
