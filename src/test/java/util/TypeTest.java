package util;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class TypeTest {
    @Test
    public void testGetAllRoads() {
        List<Type> roads = Type.getAllRoads();

        assertEquals(roads.size(), 7);
        for (Type type : roads) {
            assertTrue(type.getKey().equals("highway"));
        }
    }

    @Test
    public void testGetAllCarRoads() {
        String[] unAllowed = {"footway", "steps", "cycleway", "bridleway", "path", "track", 
        "pedestrian", "service", "living_street", "unclassified", "road", "mini_roundabout"};

        List<Type> types = Type.getAllCarRoads();
       
        for (Type type : types) {
            String[] strs = type.getValue();
            // If unAllowed is in any of theStrs, then the test fails
            assertFalse(Arrays.stream(strs).anyMatch(s -> Arrays.stream(unAllowed).anyMatch(s::equals)));
        }
        
    }

    @Test
    public void testGetAllPedestrianRoads() {
        String[] unAllowed = {"motorway", "motorway_link", "primary", "primary_link", "trunk", "trunk_link", "cycleway"};

        List<Type> types = Type.getAllPedestrianRoads();
       
        for (Type type : types) {
            String[] strs = type.getValue();
            // If unAllowed is in any of theStrs, then the test fails
            assertFalse(Arrays.stream(strs).anyMatch(s -> Arrays.stream(unAllowed).anyMatch(s::equals)));
        }
    }

    @Test
    public void testGetAllTypesOfHierarchy() {
        List<Type> hierarchys = Type.getTypesOfHierarchy(9);
        assertThrows(IllegalArgumentException.class, () -> Type.getTypesOfHierarchy(10));
        for (Type type : hierarchys) {
            assertTrue(Type.getHierarchy(type) == 9);
            
        }
    }
}
