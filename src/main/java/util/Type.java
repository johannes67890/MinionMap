package util;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import gui.GraphicsHandler;
import javafx.scene.paint.Color;



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

    /* Draw Types */
    // PATHWAY - The red path for shortest path
    PATHWAY("", new String[]{""}, 9, 9, Color.RED, 4, true, 3, 100), 
    // PATHGRID - The blue path for shortest path
    PATHGRID("", new String[]{""}, 9, 9, Color.BLUE, 4, true, 3, 100),

    /* OSM Types */
    REGION("place", new String[]{"island"}, 10, 2, Color.web("#F2EFE9"), Color.web("#F2EFE9").darker(), 1000, false),
    //BOUNDARY("boundary", new String[]{"administrative"}, 10, 2, Color.LIGHTYELLOW.desaturate(), Color.YELLOW, 5, false),
    BORDER("admin_level", new String[]{"9", "8", "7", "6", "5", "4", "3"}, 10, 2, Color.web("#F2EFE9"), Color.web("#F2EFE9").darker(), 5, false),

    // Natural, Landuse and main infrastructure (Hierarchy 9)

    MOTORWAY("highway", new String[]{"motorway", "motorway_link"}, 9, 12, Color.web("#E892A2"), 5, true, 8, 100),
    PRIMARY_ROAD("highway", new String[]{"primary", "trunk"}, 9, 13, Color.web("#FCD6A4"), 5,  true, 12, 100),
    SECONDARY_ROAD("highway", new String[]{"secondary"}, 9, 11, Color.web("#F3F6B9").interpolate(Color.web("#F3F6B9").darker(), 0.2), 5, true, 6, 75),
    TERTIARY_ROAD("highway",new String[]{"tertiary", "tertiary_link"},9, 10, Color.DARKGRAY, 4, true, 4, 50),
    RAILWAY("railway",new String[]{"rail","light_rail","subway"}, 9, 9, Color.DARKGRAY, 2, true, 4, 1000),
    WATER("natural",new String[]{"water"}, 9, 9, Color.web("#AAD3DF"), Color.LIGHTBLUE.darker(), 5, false),
    WATERWAY("waterway",new String[]{""},9, 8, Color.web("#AAD3DF"), 3, true, 2, 25),
    //COASTLINE("natural", new String[]{"coastline"}, 10, 1, Color.PLUM, 5, true, 6, 100),
    
    // Landuse (Hierarchy: 8)
    RESIDENTIAL("landuse", new String[]{"residential"}, 8, 6, Color.web("#E0DFDF"), Color.LIGHTGRAY, 1, false),
    AEROWAY("aeroway", new String[]{"apron", "hangar", "spaceport", "terminal"}, 8, 8, Color.web("#DADAE0"), Color.LIGHTGRAY.darker(), 5, false),
    AEROWAYLINE("aeroway", new String[]{"taxiway", "runway"}, 8, 9, Color.web("#BDBDCE"), 5, true, 6, 75),
    HELIPAD("aeroway", new String[]{"helipad", "heliport"}, 8, 9, Color.web("#E9E7E2"), Color.LIGHTGRAY.darker(), 5, false),
    AERODROME("aerodrome", new String[]{""}, 8, 3, Color.web("#E9E7E2"), Color.LIGHTGRAY.darker(), 5, false),
    BEACH("natural",new String[]{"beach"}, 8, 3, Color.web("#FBEDB7"), Color.YELLOW.darker(), 5, false),
    ALLOTMENTS("landuse", new String[]{"allotments"}, 8, 8, Color.web("#C9E1BF"), Color.BLANCHEDALMOND.darker(), 5, false),
    GRAVEYARD("amenity", new String[]{"grave_yard"}, 8, 7, Color.web("#AACBAF"), Color.BLANCHEDALMOND.darker(), 5, false),
    CEMETERY("landuse", new String[]{"cemetery"}, 8, 7, Color.web("#AACBAF"), Color.BLANCHEDALMOND.darker(), 5, false),
    PARKING("amenity", new String[]{"parking"}, 8, 7, Color.web("#EEEEEE"), Color.BLANCHEDALMOND.darker(), 5, false),
    SAND("natural", new String[]{"sand", "dune"}, 8, 7, Color.web("#F7EDD1"), Color.SANDYBROWN, 5, false),
    PORT("industrial", new String[]{"port"}, 8, 0, Color.TRANSPARENT, Color.TRANSPARENT, 5, false),
    
    // Urban and natural (Hierarchy: 7)
    FOREST("landuse",new String[]{"forest"}, 7, 4, Color.web("#ADD19E"), Color.GREEN.darker(), 5, false),
    INDUSTRIAL("landuse", new String[]{"industrial"}, 7, 8, Color.web("#EBDBE8"), Color.PLUM.darker(), 1, false),
    FARMFIELD("landuse", new String[]{"farmland"}, 7, 6, Color.web("#EEF0D5"), Color.web("#EEF0D5").darker(), 5, false),
    GRASS("landuse",new String[]{"meadow","grass"}, 7, 5, Color.web("#CDEBB0"), Color.web("#CDEBB0").darker(), 5, false),
    NATURALS("natural",new String[]{"scrub","grassland","heath", "wood"}, 7, 5, Color.web("#C8D7AB"), Color.GREENYELLOW.darker(), 5, false),
    WETLAND("natural",new String[]{"wetland"}, 7, 5, Color.web("#D6D99F"), Color.DARKKHAKI, 5, false), 
    LANDUSE("landuse", new String[]{"brownfield","greenfield","basin","greenhouse_horticulture",
    "landfill","orchard","plant_nursery","religious","reservoir","retail","salt_pond","village_green","vineyard","winter_sports"}, 
    7, 7, Color.web("#EBDBE8"), Color.BLANCHEDALMOND.darker(), 5, false),
    LANDUSE_URBAN("landuse", new String[]{"commercial","construction","depot", "garages", "railway"}, 7, 7,Color.web("#EBDBE8"), Color.BLANCHEDALMOND.darker(), 5, false),
    TENTS("tents",new String[]{""}, 7, 5, Color.web("#CDEBB0"), Color.GREEN.darker(), 5, false),
    HOSPITAL("amenity", new String[]{"hospital"}, 7, 8, Color.web("#FFFFE5"), Color.web("#FFFFE5").darker(), 5, false),
    RECREATIONGROUND("landuse", new String[]{"recreation_ground"}, 7, 7, Color.web("#DFFCE2"), Color.BLANCHEDALMOND.darker(), 5, false),
    QUARRY("landuse", new String[]{"quarry"}, 7, 7, Color.web("#C5C3C3"), Color.BLANCHEDALMOND.darker(), 5, false),
    FARMYARD("landuse", new String[]{"farmyard", "farm"}, 7, 7, Color.web("#F5DCBA"), Color.BLANCHEDALMOND.darker(), 5, false),
    ROCK("natural", new String[]{"arch", "bare_rock", "blockfield", "cave_entrance", "dune", "fumarole", "hill", "rock", "sand", "scree", "sinkhole", "stone"}, 7, 7, Color.web("#DBD6D0"), Color.LIGHTGRAY, 5, false),
    ROCKLINE("natural", new String[]{"arete", "cliff", "earth_bank", "ridge", "valley"}, 7, 9, Color.web("#DBD6D0"), 5, true, 2, 50),
    ABANDONEDRAIL("railway",new String[]{"abandoned"}, 5, 9, Color.DARKGRAY, 2, true, 4, 1000),
    MILITARY("landuse", new String[]{"military"}, 7, 10, Color.SALMON.interpolate(Color.TRANSPARENT, 0.5), Color.SALMON.interpolate(Color.WHITE, 0.5).darker(), 5, false),
    BUILDING("building",new String[]{"", "yes"},5, 9, Color.web("#D9D0C9"), Color.web("#D9D0C9").darker(), 5, false),
    LEISURE("leisure",new String[]{"park", "dog_park", "garden", "horse_riding", "miniature_golf", 
    "pitch", "playground", "resort", "stadium", "summer_camp", "track", "sports_centre", "fitness_station", "disc_golf_course"},7, 8, Color.web("#C8FACC"), Color.web("#C8FACC").darker(), 5, false),
    GOLF("leisure",new String[]{"golf_course"},7, 8, Color.web("#DEF6C0"), Color.web("#DEF6C0").darker(), 5, false),

    // Man made objects (Hierarchy: 6)
    BRIDGE("man_made", new String[]{"bridge"},6, 9, Color.WHITE, 5, true, 4, 5),
    PIER("man_made", new String[]{"pier"},6, 9, Color.WHITE, 5, true, 5, 10),

    // Natural (Hierarchy: 5)
    
    // Other roads (Hierarchy: 4)
    AERIALWAY("aerialway", new String[]{"cable_car", "gondola", "mixed_lift", "chair_lift", "drag_lift", "t-bar", "j-bar", "platter", "rope_tow", "magic_carpet", "zip_line", "goods", "pylon"}, 
    4, 9, Color.LIGHTGRAY, 2, true, 4, 10),
    AERIALWAYSTATION("aerialway", new String[]{"station"},5, 8, Color.GRAY, Color.GRAY.darker(), 5, false),
    RESIDENTIAL_ROAD("highway",new String[]{"residential"}, 7, 9, Color.WHITE, 5, true, 2, 7),
    UNCLASSIFIED("highway",new String[]{"unclassified"}, 7, 9, Color.WHITE, 5, true, 2, 7),
    LIVING_STREET("highway",new String[]{"living_street"}, 6, 9, Color.WHITE, 5, true, 2, 7),

    ROAD("highway",new String[]{"road"}, 7, 9, Color.WHITE, 5, true, 2, 7),


    SERVICE("highway",new String[]{"service"}, 6, 9, Color.WHITE, 5, true, 2, 7),
    TRACK("highway",new String[]{"track"}, 6, 9, Color.WHITE, 5, true, 2, 7),

    BIKE_ROAD("cycleway",new String[]{"lane", "oppisite", "opposite_lane", "track", "opposite_track", "share_busway", "shared_lane", "opposite_share_busway"}, 6, 9, Color.WHITE, 5, true, 2, 7),
    PEDESTRIAN_ROAD("highway",new String[]{"footway", "path", "steps", "bridleway", "pedestrian"}, 6, 9, Color.WHITE, 5, true, 2, 7),
    OTHER_ROAD("highway",new String[]{"cycleway", "mini_roundabout"}, 6, 9, Color.WHITE, 5, true, 2, 2),
    // Relations (Hierarchy: 3)
    MULTIPOLYGON("nothing must be mutlipolygon", new String[]{"multipolygon"}, 3, Color.BLACK, 0),
    RESTRICTION("type", new String[]{"restriction"}, 3, Color.BLACK, 0),
    ROUTE("type", new String[]{"route"}, 3, Color.BLACK, 0),
    // Unknown (Hierarchy: 0)
    UNKNOWN("", new String[]{""}, 0, 9, Color.BLACK, 5, true, 2, 7);


    public enum Place {
        // Administratively declared places
        COUNTRY("country", 10, 2),
        STATE("state", 10, 2),
        REGION("region", 10, 2),
        MUNICIPALITY("municipality", 10, 2),

        // Populated settlements, urban areas
        CITY("city", 10, 2),
        BOROUGH("borough", 10, 2),
        SUBURB("suburb", 10, 2),
        QUARTER("quarter", 10, 2),
        NEIGHBOURHOOD("neighbourhood", 10, 2),
        // Populated settlements, urban and rural areas'
        TOWN("town", 10, 2),
        HAMLET("hamlet", 10, 2),
        VILLAGE("village", 10, 2),
        // Other places
        ISLAND("island", 10, 2),
        ARCHIPELAGO("archipelago", 10, 2),
        ISLET("islet", 10, 2),
        SQUARE("square", 10, 2);


        private final String key = "place"; // key of the tag
        private final String value; // value of the tag
        private final int hierarchy; // hierarchy of the tag - How important is it to display
        private final int layer; // The layer of where the object should be drawn

        Place(String value, int hierarchy, int layer) {
            this.value = value;
            this.hierarchy = hierarchy;
            this.layer = 0;
        }

        public String getKey() {
            return key;
        }
    
        public String getValue() {
            return value;
        }
    
        public static Place[] getTypes(){
            return Place.values();
        }

        public static String[] getValues(){
            return Place.getValues();
        }

         /**
         * Returns the hierarchy of the {@link Place}. Used for optimization of the map, with priority rendering.
         * @param type - The {@link Place} to get the hierarchy of.
         * @return The hierarchy of the {@link Place} from 0 (least important) to 9 (most important).
         */
        public static int getHierarchy(Place type){
            return type.hierarchy;
        }
            
        public int getThisHierarchy(){
            return hierarchy;
        }

        public int getLayer(){
            return layer;
        }
    }


    private final String key; // key of the tag
    private final String[] value; // value of the tag
    private final int hierarchy; // hierarchy of the tag - How important is it to display
    private final int layer; // The layer of where the object should be drawn
    private final Color color;
    private final Color polyLineColor;
    private final double width;
    private final boolean isLine;
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
    

    /**
     * @return - A list of Types for pathfinding with all possible roads.
    */public static List<Type> getAllRoads() {
        return Arrays.stream(Type.values())
            .filter(type -> type.getKey().contains("highway"))
            .collect(Collectors.toList());
    }

    /**
     * @return - A list of Types for pathfinding with the {@link TransportType}.CAR setting
     */
    public static List<Type> getAllCarRoads() {
        String[] unAllowed = {"footway", "steps", "cycleway", "bridleway", "path", 
        "pedestrian", "track", "mini_roundabout"};

        // A group that contains all types of roads that are for cars
        // But disallowing types with values from unAllowed
        return Arrays.stream(Type.values())
            .filter(type -> type.getKey().contains("highway"))
            .filter(type -> !Arrays.asList(unAllowed).contains(type.getValue()[0]))
            .collect(Collectors.toList());
    }

    /**
     * @return - A list of Types for pathfinding with the {@link TransportType}.BIKE setting
     */
    public static List<Type> getAllBikeRoads() {
        String[] unAllowed = {"motorway", "primary", "trunk"};
        // A group that contains all types of roads that are for bikes
        // The group is without motorways and primary roads
        return Arrays.stream(Type.values())
            .filter(type -> type.getKey().contains("cycleway") || type.getKey().contains("highway"))
            .filter(type -> !Arrays.asList(unAllowed).contains(type.getValue()[0]))
            .collect(Collectors.toList());
    }

    /**
     * @return - A list of Types for pathfinding with the {@link TransportType}.FOOT setting
     */
    public static List<Type> getAllPedestrianRoads() {
        String[] unAllowed = {"motorway", "motorway_link", "primary", "primary_link", "trunk", "trunk_link"};
        // A group that contains all types of roads that are for pedestrians
        // The group is without motorways and primary roads
        return Arrays.stream(Type.values())
            .filter(type -> type.getKey().contains("highway"))
            .filter(type -> !Arrays.asList(unAllowed).contains(type.getValue()[0]))
            .collect(Collectors.toList());
    }

    /**
     * Returns all possible Types within a given hierarchy.
     * @param i - The hierarchy to get the Types from.
     * @return A list of Types within the given hierarchy.
     */
    public static List<Type> getTypesOfHierarchy(int i) {
        if(i < 0 || i > 13) throw new IllegalArgumentException("Hierarchy must be between 0 and 13");
        return Arrays.stream(Type.values())
            .filter(type -> Type.getHierarchy(type) == i)
            .collect(Collectors.toList());
    }

    /**
     * 
     * @return The key of the {@link Type} as a string.
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @return The value of the {@link Type} as an array of strings.
     */
    public String[] getValue() {
        return value;
    }

    /**
     * 
     * @return An array of Types.
     */
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

        
    /**
     * @return The hierarchy of the {@link Type} from 0 (least important) to 9 (most important) as an integer.
     */
    public int getThisHierarchy(){
        return hierarchy;
    }

    /**
     * @return The layer of the {@link Type} from 0 (far behind) to 9 (up front) as an integer.
     */
    public int getLayer(){
        return layer;
    }

    /**
     * @return The width of the {@link Type} as a double.
     */
    public double getWidth(){
        return width;
    }
    
    /**
     * @return The color of the {@link Type}. Color is determined by the {@link GraphicsHandler}.
     */
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

    /**
     * @return The color of the polygon line. Color is determined by the {@link GraphicsHandler}.
     */
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

    /**
     * @return A boolean to determine if the {@link Type} should be drawn as a line.
     */
    public boolean getIsLine(){
        return isLine;
    }

    /**
     * @return The minimum width of the {@link Type} as a double.
     */
    public double getMinWidth(){
        return minWidth;
    }

    /**
     * @return The maximum width of the {@link Type} as a double.
     */
    public double getMaxWidth(){
        return maxWidth;
    }
}
