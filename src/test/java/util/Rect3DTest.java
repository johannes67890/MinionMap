package util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import structures.KDTree.Point3D;
import structures.KDTree.Rect3D;

public class Rect3DTest {


    @Test
    public void intersects(){
        
        Rect3D rect1 = new Rect3D(0f, 0f, 0, 5f, 5f, 5);
        Rect3D rect2 = new Rect3D(5f, 5f, 5, 10f, 10f, 10);
        boolean intersect = rect1.intersects(rect2);
        assertTrue(intersect);

        rect1 = new Rect3D(0f, 0f, 0, 5f, 5f, 5);
        rect2 = new Rect3D(6f, 5f, 5, 10f, 10f, 10);
        intersect = rect1.intersects(rect2);
        assertFalse(intersect);

    }

    @Test
    public void contains(){
        
        Rect3D rect1 = new Rect3D(0f, 0f, 0, 5f, 5f, 5);
        Point3D point1 = new Point3D(3f, 3f, (byte) 0);

        boolean contains = rect1.contains(point1);
        assertTrue(contains);

        point1 = new Point3D(0f, 0f, (byte) 7);
        contains = rect1.contains(point1);
        assertFalse(contains);

    }

    // Implicitly also tests distanceSquaredToMethod
    @Test
    public void distanceTo(){
        
        Rect3D rect1 = new Rect3D(0f, 0f, 0, 5f, 5f, 5);
        Point3D point1 = new Point3D(3f, 3f, (byte) 0);

        double distance = rect1.distanceTo(point1);
        assertEquals(0, distance);

        point1 = new Point3D(0f, 10f, (byte) 0);
        distance = rect1.distanceTo(point1);
        assertEquals(5, distance);
    }

    @Test
    public void constructor(){
        Executable e = new Executable() {
            @Override
            public void execute() throws Throwable {
                new Rect3D(1, 1, 1, 0, 0, 0);
            }
        };
        assertThrows(IllegalArgumentException.class, e);
    }
}
