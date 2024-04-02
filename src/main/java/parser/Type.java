package parser;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Enum for the different types of tags.  
 * <p>
 * The different types of tags are of a hierarchy from 0 to 9, where 9 is the most important and 0 is the least important.
 * The hierarchy is used for optimization of the map, with priority rendering.
 * </p>
 * <p>
 * A enum field contains the following:
 * <ul>
 * <li>key ({@link String}) - The key of the tag</li>
 * <li>value ({@link Array} of {@link String}) - The value of the tag</li>    
 * <li>hierarchy ({@link Integer}) - The hierarchy of the tag</li>
 * <li>color ({@link Color}) - The color of the tag</li>
 * <li>width ({@link Double}) - The width of the tag</li>
 * </ul>
 * </p>
 */
public enum Type {
    // Natural, Landuse and main infrastructure (Hierarchy 9)
    COASTLINE("natural", new String[]{"coastline"}, 9, Color.PLUM, 0),
    PRIMARY_ROAD("highway", new String[]{"primary"}, 9, Color.PEACHPUFF, 0),
    RESIDENTIAL("landuse", new String[]{"residential", "industrial"}, 9, Color.PEACHPUFF, 0),
    
    
    // Landuse (Hierarchy: 8)
    SECONDARY_ROAD("highway", new String[]{"secondary"}, 8, Color.LIGHTYELLOW, 0),
    RAILWAY("railway",new String[]{"rail","light_rail","subway", "abandoned"}, 8, Color.DARKGRAY, 0),
    FARMFIELD("landuse", new String[]{"farmland"}, 8, Color.KHAKI, 0),
    LANDUSE("landuse", new String[]{"commercial","retail","construction","brownfield","greenfield","allotments","basin",
    "cemetery","depot","garages","grass","greenhouse_horticulture","industrial","landfill","meadow","military","orchard","plant_nursery",
    "port","quarry","railway","recreation_ground","religious","reservoir","residential","retail","salt_pond","village_green","vineyard",
    "winter_sports","farmyard","forest","farm", ""}, 8, Color.BURLYWOOD, 0),

    // Urban and natural (Hierarchy: 7)
    BUILDING("building",new String[]{"", "yes"},7, Color.BURLYWOOD, 0),
    LEISURE("leisure",new String[]{"park"},7, Color.LIGHTGREEN, 0),
    WATERWAY("waterway",new String[]{""},7, Color.LIGHTBLUE, 0),

    // Man made objects (Hierarchy: 6)
    BRIDGE("man_made", new String[]{"bridge"},6, Color.WHITE, 0),
    TERTIARY_ROAD("highway",new String[]{"tertiary", "tertiary_link"},6, Color.WHITE, 0),
    PIER("man_made", new String[]{"pier"},6, Color.WHITE, 0),

    // Natural (Hierarchy: 5)
    BEACH("natural",new String[]{"beach"}, 5, Color.LIGHTYELLOW, 0),
    FOREST("landuse",new String[]{"forest","meadow","grass"}, 5, Color.GREEN, 0),
    NATURALS("natural",new String[]{"scrub","grassland","heath", "wood"}, 5, Color.GREENYELLOW, 0),
    WATER("natural",new String[]{"water"}, 5, Color.LIGHTBLUE, 0),
    WETLAND("natural",new String[]{"wetland"}, 5, Color.DARKKHAKI, 0),
    // Other roads (Hierarchy: 4)
    OTHER_ROAD("highway",new String[]{"residential", "unclassified", "track", "footway", "cycleway", "path", 
    "service", "motorway_link", "steps", "living_street", "mini_roundabout", "pedestrian"}, 4, Color.WHITE, 0),

    // Unknown (Hierarchy: 0)
    UNKNOWN("", new String[]{""}, 0, Color.WHITE, 0);


    private final String key; // key of the tag
    private final String[] value; // value of the tag
    private final int hierarchy; // hierarchy of the tag - How important is it to display
    private final Color color;
    private final double width;

    Type(String key, String[] value, int hierarchy, Color color, double width) {
        this.key = key;
        this.value = value;
        this.hierarchy = hierarchy;
        this.color = color;
        this.width = width;
    }
    
    public String getKey() {
        return key;
    }

    public String[] getValue() {
        return value;
    }

    public static Type[] getTypes(){
        return Type.values();
    }

    /**
     * Returns the hierarchy of the {@link Type}. Used for optimization of the map, with priority rendering.
     * @param type - The {@link Type} to get the hierarchy of.
     * @return The hierarchy of the {@link Type} from 0 (least important) to 9 (most important).
     */
    public static int getHierarchy(Type type){
        return type.hierarchy;
    }
    /**
     * Returns the most important type of the two types.
     * @param type1 - The first type to compare.
     * @param type2 - The second type to compare.
     * @return The most important {@link Type} of the two types.
     */
    public static Type getMostImportant(Type type1, Type type2){
        if(type1.hierarchy > type2.hierarchy){
            return type1;
        } else {
            return type2;
        }
    }

    public double getWidth(){
        return width;
    }
    
    public Paint getColor() {
        return color;
    }
}