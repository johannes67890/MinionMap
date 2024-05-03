package org;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import util.Point3D;

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
    public void compareX(){
        
        Point3D point1 = new Point3D(3f, 3f, (byte) 0);
        Point3D point2 = new Point3D(4f, 3f, (byte) 0);

        assertEquals(point1.X_ORDER.compare(point1, point2), -1);
        assertEquals(point1.X_ORDER.compare(point1, point1), 0);
        assertEquals(point1.X_ORDER.compare(point2, point1), 1);

    }

    @Test
    public void compareY(){
        
        Point3D point1 = new Point3D(3f, 4f, (byte) 0);
        Point3D point2 = new Point3D(4f, 3f, (byte) 0);

        assertEquals(point1.Y_ORDER.compare(point1, point2), 1);
        assertEquals(point1.Y_ORDER.compare(point1, point1), 0);
        assertEquals(point1.Y_ORDER.compare(point2, point1), -1);

    }

    @Test
    public void compareZ(){
        
        Point3D point1 = new Point3D(3f, 4f, (byte) 1);
        Point3D point2 = new Point3D(4f, 3f, (byte) 0);

        assertEquals(point1.Z_ORDER.compare(point1, point2), 1);
        assertEquals(point1.Z_ORDER.compare(point1, point1), 0);
        assertEquals(point1.Z_ORDER.compare(point2, point1), -1);

    }

    @Test
    public void compareR(){
        
        Point3D point1 = new Point3D(7f, 4f, (byte) 1);
        Point3D point2 = new Point3D(8f, 3f, (byte) 2);

        assertEquals(point1.R_ORDER.compare(point1, point2), -1);
        assertEquals(point1.R_ORDER.compare(point1, point1), 0);
        assertEquals(point1.R_ORDER.compare(point2, point1), 1);
    }

}
