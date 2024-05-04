package parser;
import java.util.HashMap;

import util.Type;


/**
 * Class for storing a {@link HashMap} of a single node in a grid.
 * Contains the following tags:
 * <p>
 * </p>
*/
public class TagGrid extends Tag {

    private float lon;
    private float lat;

    public TagGrid(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }
   
    @Override
    public long getId(){
        return 0;
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
 

    @Override
    public String toString() {
        return "TagGrid{" +
                " lon=" + lon +
                ", lat=" + lat +
                '}';
    }

}
