package org;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import parser.Tag;
import parser.TagNode;
import parser.XMLReader;
import util.Tree;

public class TreeTest {

    @BeforeEach
    void setUp() {
        new XMLReader("src/test/java/org/ressources/map.osm");
        ArrayList<Tag<?>> tempList = new ArrayList<>();
        tempList.addAll(XMLReader.getWays().values());

        Tree.initialize(tempList);
    }

    @Test
    void testInsertTagInTree() {
        assertNotNull(Tree.getNearestPoint(new Point2D(20, 20)));
    }


    @Test
    void testGetTagsInBounds() {
        ArrayList<Tag<?>> tempList = new ArrayList<>(XMLReader.getNodes().values());
        tempList.addAll(XMLReader.getWays().values());
        tempList.addAll(XMLReader.getRelations().values());

        HashSet<Tag<?>> tagsInBounds = Tree.getTagsInBounds(new RectHV(-200, -200, 200, 200));

        assertTrue(tagsInBounds.size() > 0);
    }

    @Test
    void testGetTagsNearPoint(){
        HashMap<Long,TagNode> hM = new HashMap<>(XMLReader.getNodes());
        ArrayList<Tag<?>> tagList = new ArrayList<>(hM.values());

        Point2D point = new Point2D(tagList.get(0).getLon(), tagList.get(0).getLat());
        
        assertTrue(Tree.getTagsNearPoint(point).size() > 0);
    }

    @Test
    void testGetTagsFromPoint(){
        HashMap<Long,TagNode> hM = new HashMap<>(XMLReader.getNodes());
        ArrayList<Tag<?>> tagList = new ArrayList<>(hM.values());

        Point2D point = new Point2D(tagList.get(0).getLon(), tagList.get(0).getLat());
        
        assertTrue(Tree.getTagsFromPoint(point).size() > 0);
    }
}
