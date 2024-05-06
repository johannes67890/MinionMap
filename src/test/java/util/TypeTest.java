package util;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class TypeTest {
    @Test
    public void testGetAllRoads() {
        List<Type> roads = Type.getAllRoads();

        assertEquals(12,roads.size());
        for (Type type : roads) {
            assertTrue(type.getKey().equals("highway"));
        }
    }

    @Test
    public void testGetAllCarRoads() {
        String[] unAllowed = {"footway", "steps", "cycleway", "bridleway", "path", 
        "pedestrian", "track", "mini_roundabout"};

        List<Type> types = Type.getAllCarRoads();
       
        for (Type type : types) {
            String[] strs = type.getValue();
            // If unAllowed is in any of theStrs, then the test fails
            assertFalse(Arrays.stream(strs).anyMatch(s -> Arrays.stream(unAllowed).anyMatch(s::equals)));
        }
        
    }

    @Test
    public void testGetAllPedestrianRoads() {
        String[] unAllowed = {"motorway", "motorway_link", "primary", "primary_link", "trunk", "trunk_link"};

        List<Type> types = Type.getAllPedestrianRoads();
       
        for (Type type : types) {
            String[] strs = type.getValue();
            // If unAllowed is in any of theStrs, then the test fails
            assertFalse(Arrays.stream(strs).anyMatch(s -> Arrays.stream(unAllowed).anyMatch(s::equals)));
        }
    }

    @Test
    public void testGetAllBikeRoads(){
        String[] unAllowed = {"motorway", "motorway_link", "trunk", "trunk_link"};

        List<Type> types = Type.getAllBikeRoads();
       
        for (Type type : types) {
            String[] strs = type.getValue();
            // If unAllowed is in any of theStrs, then the test fails
            assertFalse(Arrays.stream(strs).anyMatch(s -> Arrays.stream(unAllowed).anyMatch(s::equals)));
        }
    }

    @Test
    public void testGetAllTypesOfHierarchy() {
        List<Type> hierarchys = Type.getTypesOfHierarchy(9);
        assertThrows(IllegalArgumentException.class, () -> Type.getTypesOfHierarchy(14));
        for (Type type : hierarchys) {
            assertTrue(Type.getHierarchy(type) == 9);
            
        }
    }
}
