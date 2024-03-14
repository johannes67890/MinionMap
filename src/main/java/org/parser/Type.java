package org.parser;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum Type {
    // Natural, Landuse and main infrastructure (Hierarchy 9)
    COASTLINE("natural", new String[]{"coastline"}, 9, Color.BLACK, 0),
    PRIMARY_ROAD("highway", new String[]{"primary"}, 9, Color.BLACK, 0),
    RESIDENTIAL("landuse", new String[]{"residential", "industrial"}, 9, Color.BLACK, 0),
    
    
    // Landuse (Hierarchy: 8)
    SECONDARY_ROAD("highway", new String[]{"secondary"}, 8, Color.BLACK, 0),
    RAILWAY("railway",new String[]{"rail","light_rail","subway"}, 8, Color.BLACK, 0),
    FARMFIELD("landuse", new String[]{"farmland"}, 8, Color.BLACK, 0),

    // Urban and natural (Hierarchy: 7)
    BUILDING("building",new String[]{""},7, Color.BLACK, 0),
    LEISURE("leisure",new String[]{"park"},7, Color.BLACK, 0),
    WATERWAY("waterway",new String[]{""},7, Color.BLACK, 0),

    // Man made objects (Hierarchy: 6)
    BRIDGE("man_made", new String[]{"bridge"},6, Color.BLACK, 0),
    TERTIARY_ROAD("highway",new String[]{"tertiary", "tertiary_link"},6, Color.BLACK, 0),
    PIER("man_made", new String[]{"pier"},6, Color.BLACK, 0),

    // Natural (Hierarchy: 5)
    BEACH("natural",new String[]{"beach"}, 5, Color.BLACK, 0),
    FOREST("landuse",new String[]{"forest","meadow","grass"}, 5, Color.BLACK, 0),
    NATURALS("natural",new String[]{"scrub","grassland","heath", "wood"}, 5, Color.BLACK, 0),
    WATER("natural",new String[]{"water"}, 5, Color.BLACK, 0),
    WETLAND("natural",new String[]{"wetland"}, 5, Color.BLACK, 0),
    // Other roads (Hierarchy: 4)
    OTHER_ROAD("highway",new String[]{"residential", "unclassified", "track", "footway", "cycleway", "path", 
    "service", "motorway_link", "steps", "living_street", "mini_roundabout", "pedestrian"}, 4, Color.BLACK, 0),

    // Unknown (Hierarchy: 0)
    UNKNOWN("", new String[]{""}, 0, Color.BLACK, 0);


    
    public String getKey() {
        return key;
    }

    public String[] getValue() {
        return value;
    }

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