package parser;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Enum for the different types of tags.  
 * <p>
 * The different types of tags are of a hierarchy from 0 to 9, where 9 is the most important and 0 is the least important.
 * The hierarchy is used for optimization of the map, with priority rendering.
 * </p>
 *  * <p>
 * The different types are layered differently to prevent improper graphical overwriting. Layers are determined from 0 to 9 also, 0 being far behind, and 9 being up front. 
 * </p>
 * <p>
 * A enum field contains the following:
 * <ul>
 * <li>key ({@link String}) - The key of the tag</li>
 * <li>value ({@link Array} of {@link String}) - The value of the tag</li>    
 * <li>hierarchy ({@link Integer}) - The hierarchy of the tag</li>
 * <li>layer ({@link Integer}) - The layer of the tag</li>
 * <li>color ({@link Color}) - The color of the tag</li>
 * <li>width ({@link Double}) - The width of the tag</li>
*  <li>isLine ({@link Boolean}) - Determines to be drawn as a line or polygon</li> * </ul>
 * </p>
 */
public enum Type {
    // Natural, Landuse and main infrastructure (Hierarchy 9)
    COASTLINE("natural", new String[]{"coastline"}, 9, 9, Color.PLUM, 5, true, 2, 100),
    PRIMARY_ROAD("highway", new String[]{"primary"}, 9, 9, Color.PEACHPUFF, 5,  true, 6, 100),
    RESIDENTIAL("landuse", new String[]{"residential", "industrial"}, 9, 7, Color.PEACHPUFF, 0, false),
    
    
    // Landuse (Hierarchy: 8)
    SECONDARY_ROAD("highway", new String[]{"secondary"}, 8, 9, Color.LIGHTYELLOW, 5, true, 6, 150),
    RAILWAY("railway",new String[]{"rail","light_rail","subway", "abandoned"}, 8, 9, Color.DARKGRAY, 2, true, 4, 1000),
    FARMFIELD("landuse", new String[]{"farmland"}, 8, 7, Color.KHAKI, 0, false),
    LANDUSE("landuse", new String[]{"commercial","construction","brownfield","greenfield","allotments","basin",
    "cemetery","depot","garages","greenhouse_horticulture","landfill","military","orchard","plant_nursery",
    "port","quarry","railway","recreation_ground","religious","reservoir","retail","salt_pond","village_green","vineyard",
    "winter_sports","farmyard","farm", ""}, 8, 7, Color.BLANCHEDALMOND, 0, false),

    // Urban and natural (Hierarchy: 7)
    BUILDING("building",new String[]{"", "yes"},7, 8, Color.BURLYWOOD, 0, false),
    LEISURE("leisure",new String[]{"park"},7, 8, Color.LIGHTGREEN, 0, false),
    WATERWAY("waterway",new String[]{""},7, 8, Color.LIGHTBLUE, 3, true, 2, 25),

    // Man made objects (Hierarchy: 6)
    BRIDGE("man_made", new String[]{"bridge"},6, 9, Color.WHITE, 5, true, 4, 5),
    TERTIARY_ROAD("highway",new String[]{"tertiary", "tertiary_link"},6, 9, Color.LIGHTGRAY, 5, true, 4, 10),
    PIER("man_made", new String[]{"pier"},6, 9, Color.WHITE, 5, true, 5, 10),

    // Natural (Hierarchy: 5)
    BEACH("natural",new String[]{"beach"}, 5, 7, Color.LIGHTYELLOW, 0, false),
    FOREST("landuse",new String[]{"forest","meadow","grass"}, 5, 7, Color.GREEN, 0, false),
    NATURALS("natural",new String[]{"scrub","grassland","heath", "wood"}, 5, 7, Color.GREENYELLOW, 0, false),
    WATER("natural",new String[]{"water"}, 5, 8, Color.LIGHTBLUE, 0, false),
    WETLAND("natural",new String[]{"wetland"}, 5, 8, Color.DARKKHAKI, 0, false),
    // Other roads (Hierarchy: 4)
    OTHER_ROAD("highway",new String[]{"residential", "unclassified", "track", "footway", "cycleway", "path", 
    "service", "motorway_link", "steps", "living_street", "mini_roundabout", "pedestrian"}, 4, 9, Color.LIGHTGRAY, 5, true, 2, 7),

    // Unknown (Hierarchy: 0)
    UNKNOWN("", new String[]{""}, 0, 9, Color.BLACK, 5, true, 2, 7);


    private final String key; // key of the tag
    private final String[] value; // value of the tag
    private final int hierarchy; // hierarchy of the tag - How important is it to display
    private final int layer; // The layer of where the object should be drawn
    private final Color color;
    private final double width;
    private final boolean isLine;
    private final double minWidth;
    private final double maxWidth;

    Type(String key, String[] value, int hierarchy, int layer, Color color, double width, boolean isLine) {
        this.key = key;
        this.value = value;
        this.hierarchy = hierarchy;
        this.layer = layer;
        this.color = color;
        this.width = width;
        this.isLine = isLine;
        this.minWidth = 1;
        this.maxWidth = 1;
    }

    Type(String key, String[] value, int hierarchy, int layer, Color color, double width, boolean isLine, double minWidth, double maxWidth) {
        this.key = key;
        this.value = value;
        this.hierarchy = hierarchy;
        this.layer = layer;
        this.color = color;
        this.width = width;
        this.isLine = isLine;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;

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

        /**
     * Returns the highest layered type of the two types.
     * @param type1 - The first type to compare.
     * @param type2 - The second type to compare.
     * @return The highest layered {@link Type} of the two types.
     */
    public static Type getHighestLayer(Type type1, Type type2){
        if(type1.layer > type2.layer){
            return type1;
        } else {
            return type2;
        }
    }

        
    public int getThisHierarchy(){
        return hierarchy;
    }

    public int getLayer(){
        return layer;
    }

    public double getWidth(){
        return width;
    }
    
    public Paint getPaint() {
        return color;
    }
    public Color getColor(){
        return color;
    }
    public boolean getIsLine(){
        return isLine;
    }

    public double getMinWidth(){
        return minWidth;
    }
    public double getMaxWidth(){
        return maxWidth;
    }


}