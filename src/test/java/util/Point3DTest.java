package util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import structures.KDTree.Point3D;

public class Point3DTest {

    @Test
    public void distanceTo(){
        
        Point3D point1 = new Point3D(3f, 3f, (byte) 0);
        Point3D point2 = new Point3D(4f, 3f, (byte) 0);
        float distance = point1.distanceTo(point2);

        assertEquals(distance, 1.0f);

        point1 = new Point3D(3f, 5f, (byte) 0);
        point2 = new Point3D(3f, 3f, (byte) 0);

        distance = point1.distanceTo(point2);

        assertEquals(distance, 2.0f);
    }

    @Test
    public void distanceSquaredTo(){
        
        Point3D point1 = new Point3D(3f, 3f, (byte) 0);
        Point3D point2 = new Point3D(4f, 3f, (byte) 0);
        float distance = point1.distanceSquaredTo(point2);

        assertEquals(distance, 1.0f);

        point1 = new Point3D(3f, 5f, (byte) 0);
        point2 = new Point3D(3f, 3f, (byte) 0);

        distance = point1.distanceSquaredTo(point2);

        assertEquals(distance, 4.0f);
    }

    @Test
    public void compareTo(){
        
        Point3D point1 = new Point3D(3f, 3f, (byte) 0);
        Point3D point2 = new Point3D(4f, 3f, (byte) 0);

        assertEquals(point1.compareTo(point2), -1);
        assertEquals(point1.compareTo(point1), 0);
    }

    @Test
    public void equals(){
        Point3D point1 = new Point3D(3f, 3f, 0);
        Point3D point2 = new Point3D(3f, 3f, 0);
        Point3D point3 = new Point3D(3, 4, 0);

        assertTrue(point1.equals(point2));
        assertFalse(point3.equals(point1));
    }


}
