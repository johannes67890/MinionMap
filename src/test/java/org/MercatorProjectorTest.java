package org;
import org.junit.jupiter.api.Test;

import parser.TagNode;
import util.MecatorProjection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MercatorProjectorTest {
    @Test
    public void positiveMercatorProjectorTest(){
        TagNode nd = MecatorProjection.project(12, 55);
        assertEquals(1335833.875, nd.getLon(), 0.1);
        assertEquals(7361866.0, nd.getLat(), 0.15);
    }

    @Test
    public void negativeMercatorProjectorTest(){
        TagNode nd = MecatorProjection.project(-12, -55);
        assertEquals(-1335833.875, nd.getLon(),0.1);
        assertEquals(-7361866.0, nd.getLat(),0.15);
    }

    @Test
    public void zeroMercatorProjectorTest(){
        TagNode nd = MecatorProjection.project(0, 0);
        assertEquals(0.0, nd.getLon());
        assertEquals(-7.081154551613622E-10, nd.getLat());
    }

    @Test
    public void extremeMercatorProjectorTest(){
        TagNode nd = MecatorProjection.project(360, 90);
        assertEquals(4.0075016E7, nd.getLon(), 0.01E7);
        assertEquals(2.38107696E8, nd.getLat(), 0.01E8);
    }

    @Test
    public void positiveMercatorUnprojectorTest(){
        TagNode nd = MecatorProjection.unproject(1335833.875, 7361866.0);
        assertEquals(12, nd.getLon(), 0.01);
        assertEquals(55, nd.getLat(), 0.01);
    }

    @Test
    public void negativeMercatorUnprojectorTest(){
        TagNode nd = MecatorProjection.unproject(-1335833.875, -7361866.0);
        assertEquals(-12, nd.getLon(), 0.01);
        assertEquals(-55, nd.getLat(), 0.01);
    }

    @Test
    public void zeroMercatorUnprojectorTest(){
        TagNode nd = MecatorProjection.unproject(0.0, -7.081154551613622E-10);
        assertEquals(0.0, nd.getLon(), 0.1);
        assertEquals(0.0, nd.getLat(), 0.1);
    }

    @Test
    public void extremeMercatorUnprojectorTest(){
        TagNode nd = MecatorProjection.unproject(4.0075016E7, 2.38107696E8);
        assertEquals(360, nd.getLon(), 0.1);
        assertEquals(90, nd.getLat(), 0.1);
    }
}