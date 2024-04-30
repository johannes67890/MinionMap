package org;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import parser.Type;

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

        List<String> types = Type.getAllCarRoads();
       
        for (String type : types) {
            if(Arrays.asList(unAllowed).contains(type)){
                fail();
            }
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
