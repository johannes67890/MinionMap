package parser;

import java.util.HashMap;
import java.lang.reflect.Type;
import java.math.BigDecimal;

enum Node {
    ID, LAT, LON;
}

public abstract class Tag<E extends Enum<E>> extends HashMap<E, Object> {
    public abstract long getId();
    public abstract double getLat();
    public abstract double getLon();

    public Tag() {}

    public Tag(HashMap<E, Object> map) {
        super(map);
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

     /*
     * Method to check if a node is within the bounds of a given area.
     */
    public static boolean isInBounds(Tag<?> node, TagBound bound) {
        return Double.valueOf(node.getLat()).compareTo(bound.getMinLat()) == 1 && Double.valueOf(node.getLat()).compareTo(bound.getMaxLat()) == -1
            && Double.valueOf(node.getLon()).compareTo(bound.getMinLon()) == 1 && Double.valueOf(node.getLon()).compareTo(bound.getMaxLon()) == -1;
    }
}