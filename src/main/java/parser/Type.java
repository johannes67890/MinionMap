package parser;

import java.sql.Array;

import gui.GraphicsHandler;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.*;
import java.util.stream.Collectors;



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
public enum Type  {

    REGION("place", new String[]{"island"}, 10, 2, Color.LIGHTYELLOW.desaturate(), Color.PLUM, 1000, false),
    //BOUNDARY("boundary", new String[]{"administrative"}, 10, 2, Color.LIGHTYELLOW.desaturate(), Color.YELLOW, 5, false),
    BORDER("admin_level", new String[]{"9", "8", "7", "6", "5", "4", "3"}, 10, 2, Color.LIGHTYELLOW.desaturate(), Color.YELLOW, 5, false),


    // Natural, Landuse and main infrastructure (Hierarchy 9)
    PRIMARY_ROAD("highway", new String[]{"primary"}, 9, 9, Color.PEACHPUFF, 5,  true, 6, 100),
    MOTORWAY("highway", new String[]{"motorway"}, 9, 9, Color.DARKRED, 5, true, 6, 100),
    SECONDARY_ROAD("highway", new String[]{"secondary"}, 9, 9, Color.YELLOW.desaturate(), 5, true, 6, 75),
    TERTIARY_ROAD("highway",new String[]{"tertiary", "tertiary_link"},9, 9, Color.DARKGRAY, 4, true, 4, 50),
    RAILWAY("railway",new String[]{"rail","light_rail","subway"}, 9, 9, Color.DARKGRAY, 2, true, 4, 1000),
    WATER("natural",new String[]{"water"}, 9, 9, Color.LIGHTBLUE, Color.LIGHTBLUE.darker(), 5, false),
    WATERWAY("waterway",new String[]{""},9, 8, Color.LIGHTBLUE, 3, true, 2, 25),
    //COASTLINE("natural", new String[]{"coastline"}, 10, 1, Color.PLUM, 5, true, 6, 100),
    
    // Landuse (Hierarchy: 8)
    RESIDENTIAL("landuse", new String[]{"residential"}, 8, 7, Color.LIGHTGRAY.interpolate(Color.WHITE, 0.5), Color.LIGHTGRAY, 1, false),
    INDUSTRIAL("landuse", new String[]{"industrial"}, 8, 8, Color.PLUM.brighter().desaturate().interpolate(Color.WHITE, 0.5), Color.PLUM.darker(), 1, false),
    AEROWAY("aeroway", new String[]{"aerodome", "apron", "hangar", "helipad", "heliport", "spaceport", "terminal"}, 8, 8, Color.LIGHTGRAY, Color.LIGHTGRAY.darker(), 5, false),
    FARMFIELD("landuse", new String[]{"farmland"}, 8, 6, Color.KHAKI.brighter(), Color.KHAKI.darker(), 5, false),

    BEACH("natural",new String[]{"beach"}, 8, 3, Color.YELLOW, Color.YELLOW.darker(), 5, false),
    FOREST("landuse",new String[]{"forest"}, 8, 4, Color.GREEN.brighter().desaturate(), Color.GREEN.darker(), 5, false),
    GRASS("landuse",new String[]{"meadow","grass"}, 8, 5, Color.GREEN.brighter().desaturate().brighter(), Color.GREEN.darker(), 5, false),
 
    NATURALS("natural",new String[]{"scrub","grassland","heath", "wood"}, 8, 5, Color.GREENYELLOW, Color.GREENYELLOW.darker(), 5, false),
    WETLAND("natural",new String[]{"wetland"}, 8, 5, Color.DARKKHAKI, Color.DARKKHAKI, 5, false), 
    LANDUSE("landuse", new String[]{"commercial","construction","brownfield","greenfield","allotments","basin",
    "cemetery","depot","garages","greenhouse_horticulture","landfill","orchard","plant_nursery",
    "quarry","railway","recreation_ground","religious","reservoir","retail","salt_pond","village_green","vineyard",
    "winter_sports","farmyard","farm"}, 8, 7, Color.BLANCHEDALMOND, Color.BLANCHEDALMOND.darker(), 5, false),
    ROCK("natural", new String[]{"arch", "bare_rock", "blockfield", "cave_entrance", "dune", "fumarole", "hill", "rock", "sand", "scree", "sinkhole", "stone"}, 8, 7, Color.LIGHTGREY, Color.LIGHTGRAY, 5, false),
    ROCKLINE("natural", new String[]{"arete", "cliff", "earth_bank", "ridge", "valley"}, 8, 7, Color.LIGHTGRAY, 5, true, 2, 50),
    SAND("natural", new String[]{"sand", "dune"}, 8, 7, Color.SANDYBROWN.brighter(), Color.SANDYBROWN, 5, false),

    PORT("industrial", new String[]{"port"}, 8, 0, Color.TRANSPARENT, Color.TRANSPARENT, 5, false),


    // Urban and natural (Hierarchy: 7)
    ABANDONEDRAIL("railway",new String[]{"abandoned"}, 5, 9, Color.DARKGRAY, 2, true, 4, 1000),
    MILITARY("landuse", new String[]{"military"}, 7, 9, Color.SALMON.interpolate(Color.WHITE.TRANSPARENT, 0.5), Color.SALMON.interpolate(Color.WHITE, 0.5).darker(), 5, false),
    BUILDING("building",new String[]{"", "yes"},7, 9, Color.LIGHTGRAY, Color.LIGHTGRAY.darker(), 5, false),
    LEISURE("leisure",new String[]{"park"},7, 8, Color.LIGHTGREEN, Color.LIGHTGREEN.darker(), 5, false),

    // Man made objects (Hierarchy: 6)
    BRIDGE("man_made", new String[]{"bridge"},6, 9, Color.WHITE, 5, true, 4, 5),
    PIER("man_made", new String[]{"pier"},6, 9, Color.WHITE, 5, true, 5, 10),

    // Natural (Hierarchy: 5)
    
    // Other roads (Hierarchy: 4)
    AERIALWAY("aerialway", new String[]{"cable_car", "gondola", "mixed_lift", "chair_lift", "drag_lift", "t-bar", "j-bar", "platter", "rope_tow", "magic_carpet", "zip_line", "goods", "pylon"}, 
    4, 9, Color.LIGHTGRAY, 2, true, 4, 10),
    AERIALWAYSTATION("aerialway", new String[]{"station"},4, 8, Color.GRAY, Color.GRAY.darker(), 5, false),
    OTHER_ROAD("highway",new String[]{"unclassified", "track", "footway", "cycleway", "path", 
    "service", "motorway_link", "steps", "living_street", "mini_roundabout", "pedestrian"}, 4, 9, Color.WHITE, 5, true, 2, 7),

    RESIDENTIAL_ROAD("highway",new String[]{"residential"}, 4, 9, Color.WHITE, 5, true, 2, 7),

    // Relations (Hierarchy: 3)
    MULTIPOLYGON("type", new String[]{"multipolygon"}, 3, Color.BLACK, 0),
    RESTRICTION("type", new String[]{"restriction"}, 3, Color.BLACK, 0),
    ROUTE("type", new String[]{"route"}, 3, Color.BLACK, 0),
    // Unknown (Hierarchy: 0)
    UNKNOWN("", new String[]{""}, 0, 9, Color.BLACK, 5, true, 2, 7);



    private final String key; // key of the tag
    private final String[] value; // value of the tag
    private final int hierarchy; // hierarchy of the tag - How important is it to display
    private final int layer; // The layer of where the object should be drawn
    private final Color color;
    private final Color polyLineColor;
    private final double width;
    private final boolean isLine;
    private boolean toShow = true;
    private final double minWidth;
    private final double maxWidth;

    Type(String key, String[] value, int hierarchy, Color color, double width) {
        this.key = key;
        this.value = value;
        this.hierarchy = hierarchy;
        this.color = color;
        this.width = 0.2;
        this.isLine = false;
        this.minWidth = 0.5;
        this.maxWidth = 50;
        this.layer = 0;
        this.polyLineColor = Color.BLACK;
    }

    Type(String key, String[] value, int hierarchy, int layer, Color color, Color polyLineColor, double width, boolean isLine) {
        this.key = key;
        this.value = value;
        this.hierarchy = hierarchy;
        this.layer = layer;
        this.color = color;
        this.polyLineColor = polyLineColor;
        this.width = 0.2;
        this.isLine = isLine;
        this.minWidth = 0.5;
        this.maxWidth = 50;
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
        this.polyLineColor = Color.BLACK;
    }
    
    /**** Groups ****/
    public static List<Type> getAllRoads() {
        return Arrays.stream(Type.values())
            .filter(type -> type.getKey().contains("highway"))
            .collect(Collectors.toList());
    }

    public static List<Type> getTypesOfHierarchy(int i) {
        if(i < 0 || i > 9) throw new IllegalArgumentException("Hierarchy must be between 0 and 9");
        return Arrays.stream(Type.values())
            .filter(type -> Type.getHierarchy(type) == i)
            .collect(Collectors.toList());
    }


    public String getKey() {
        return key;
    }

    public String[] getValue() {
        return value;
    }

    public void toggleToShow(){
        if(toShow){
            toShow = false;
        } else toShow = true;
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
        switch (GraphicsHandler.getGraphicStyle()) {
            case DEFAULT:
                return color;    
            case DARKMODE:
                return color.grayscale().invert();     
            case GRAYSCALE:
                return color.grayscale();
            default: return color;
        }
    }
    public Color getPolyLineColor(){
        switch (GraphicsHandler.getGraphicStyle()) {
            case DEFAULT:
                return polyLineColor;       
            case DARKMODE:
                return polyLineColor.grayscale().invert(); 
            case GRAYSCALE:
                return polyLineColor.grayscale();
            default: return polyLineColor;
        }
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

    /* 
    public boolean equals(Type type) {
        if(this instanceof Type){
            return this.getKey().equals(type.getKey());   
        }
        return false;
    }*/

}
