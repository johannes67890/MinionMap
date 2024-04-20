package parser;

import java.util.HashMap;

/**
 * Abstract class for a tag.
 * <p>
 * A tag is a node, way or relation in the OSM XML file.
 * </p>
 * <p>
 * The Tag class is a subclass of {@link HashMap} with the key as an {@link Enum} that is from one of the five possible OSM XML Tags- and the value is stored as an {@link Object}.
 * Not all tags have the same keys, so the Enum is used to define the keys for the different tags.
 * </p>
 * <p>
 * The possible tags are:
 * <ul>
 *  <li>{@link TagNode} - A node in the OSM XML file.</li>
 *  <li>{@link TagWay} - A way in the OSM XML file.</li>
 *  <li>{@link TagAddress} - An address in the OSM XML file.</li>
 *  <li>{@link TagRelation} - A relation in the OSM XML file.</li>
 *  <li>{@link TagBound} - A bound in the OSM XML file.</li>
 * </ul>
 * <p>
 * The Tag class has the following abstract methods:
 * <ul>
 *    <li>{@link #getId()} - Get the id of the tag.</li>
 *   <li>{@link #getLat()} - Get the latitude of the tag.</li>
 *  <li>{@link #getLon()} - Get the longitude of the tag.</li>
 * </ul>
 * **Note** that not all tags, like {@link TagRelation} and {@link TagWay} uses {@link #getLat()} and {@link #getLon()} methods.
 * </p>
 */
public abstract class Tag {
    public Tag() {}

    /**
     * Get the id of the tag.
     * @return The id of the tag.
     */
    public abstract long getId();
    /**
     * Get the latitude of the tag.
     * @throws UnsupportedOperationException if the tag does not have a latitude value.
     * @return The latitude of the tag.
     */
    public abstract float getLat();
    /**
     * Get the longitude of the tag.
     * @throws UnsupportedOperationException if the tag does not have a longitude value.
     * @return The longitude of the tag.
     */
    public abstract float getLon();

    /**
     * Check if a tag is within a specified {@link TagBound}.
     * @param node - The tag to check.
     * @param bound - The {@link TagBound} to check if the tag is within.
     * @return True if the tag is within the {@link TagBound}, false otherwise.
     */
    public boolean isInBounds(TagBound bound) {
        return Float.valueOf(this.getLat()).compareTo(bound.getMinLat()) == 1 && Float.valueOf(this.getLat()).compareTo(bound.getMaxLat()) == -1
            && Float.valueOf(this.getLon()).compareTo(bound.getMinLon()) == 1 && Float.valueOf(this.getLon()).compareTo(bound.getMaxLon()) == -1;
    }

}