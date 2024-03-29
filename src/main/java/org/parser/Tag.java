package org.parser;

import java.util.HashMap;
import java.math.BigDecimal;

public class Tag<E extends Enum<E>> extends HashMap<E, Object> {
    // existing code
    public Tag(HashMap<E, ?> map) {
        super(map);
    }

    public Tag(E key, Object value) {
        super();
        this.put(key, value);
    }

    /*
     * Method to check if a node is within the bounds of a given area.
     */
    public static boolean isInBounds(TagNode node, TagBound bound) {
        return node.getLat().compareTo(bound.getMinLat()) == 1 && node.getLat().compareTo(bound.getMaxLat()) == -1
            && node.getLon().compareTo(bound.getMinLon()) == 1 && node.getLon().compareTo(bound.getMaxLon()) == -1;
    }

    /*
     * Method to check if a node is within the bounds of a given area.
     */
    public static boolean isInBounds(TagAddress address, TagBound bound) {
        return address.getLat().compareTo(bound.getMinLat()) == 1 && address.getLat().compareTo(bound.getMaxLat()) == -1
            && address.getLon().compareTo(bound.getMinLon()) == 1 && address.getLon().compareTo(bound.getMaxLon()) == -1;
    }
}
