package structures;
import java.util.HashMap;

import parser.Tag;
import util.Type;


/**
 * Class for storing a {@link HashMap} of a single node in a grid.
 * The grids are meant to ensure that bigger relations and ways, will be drawn,
 * despite zooming in on them.
 * The {@link K3Dtree} will be able to recognize these taggrids and will draw whatever way
 * these grids are meant for.
*/
public class TagGrid extends Tag {

    private float lon;
    private float lat;

    public TagGrid(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }
    /**
     * Returns zero, as TagGrid is not meanth to have an id.
     */
    @Override
    public long getId(){
        return 0;
    }

    /**
     * Returns the lattitude (the y-value) of the TagGrid
     */
    @Override
    public float getLat(){
        return this.lat;
    }
    /**
     * Returns the longitude (the x-value) of the TagGrid
     */
    @Override
    public float getLon(){
        return this.lon;
    }

    /**
     * Cannot get a type from TagGrid!
     */
    @Override
    public Type getType() {
        throw new UnsupportedOperationException("TagGrid does not have a type.");
    }
 

    @Override
    public String toString() {
        return "TagGrid{" +
                " lon=" + lon +
                ", lat=" + lat +
                '}';
    }

}
