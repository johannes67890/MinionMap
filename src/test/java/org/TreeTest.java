package org;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.sql.rowset.spi.XmlReader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import parser.Tag;
import parser.TagNode;
import parser.XMLReader;
import util.K3DTree;
import util.Point3D;
import util.Rect3D;
import util.Tree;

public class TreeTest {
    private XMLReader reader;
    private Tree tree;
    @BeforeEach
    void setUp() {
        this.reader = new XMLReader("src/test/java/org/ressources/map.osm");
        assertNotNull(reader);
        assertDoesNotThrow(() -> this.reader);
        ArrayList<Tag> tempList = new ArrayList<>();
        tempList.addAll(reader.getWays().valueCollection());

        Tree.initialize(tempList);
    }

    @Test
    void testInsertTagInTree() {
        assertNotNull(Tree.getNearestPoint(new Point3D(20, 20, 0)));
    }


    @Test
    void testGetTagsInBounds() {
        ArrayList<Tag> tempList = new ArrayList<>(reader.getNodes().valueCollection());
        tempList.addAll(reader.getWays().valueCollection());
        tempList.addAll(reader.getRelations().valueCollection());

        HashSet<Tag> tagsInBounds = Tree.getTagsInBounds(new Rect3D(-200, -200, 0, 200, 200, 10));

        assertTrue(tagsInBounds.size() > 0);
    }

    @Test
    void testGetTagsNearPoint(){
        ArrayList<Tag> tagList = new ArrayList<>(reader.getNodes().valueCollection());

        Point3D point = new Point3D(tagList.get(0).getLon(), tagList.get(0).getLat(), 0);

        assertTrue(Tree.getTagsNearPoint(point).size() > 0);
    }

    @Test
    void testGetTagsFromPoint(){
        ArrayList<Tag> tagList = new ArrayList<>(reader.getNodes().valueCollection());

        Point3D point = new Point3D(tagList.get(0).getLon(), tagList.get(0).getLat(), 0);

        assertTrue(Tree.getTagsFromPoint(point).size() > 0);
    }   
}
