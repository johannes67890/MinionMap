package org;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import parser.Type;

public class TypeTest {
    @Test
    public void testGetAllRoads() {
        List<Type> roads = Type.getAllRoads();

        assertEquals(roads.size(), 6);
        for (Type type : roads) {
            assertTrue(type.getKey().equals("highway"));
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
