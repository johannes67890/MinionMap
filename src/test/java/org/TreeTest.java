package org;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
        List<Tag> tempList = new ArrayList<>();
        tempList = (List<Tag>)(List<?>) (Arrays.asList(XMLReader.getWays().values()));

        Tree.initialize(tempList);
    }

    @Test
    void testInsertTagInTree() {
        assertNotNull(Tree.getNearestPoint(new Point2D(20, 20)));
    }


    @Test
    void testGetTagsInBounds() {
        List<Tag> tempList = new ArrayList<>();
        tempList = (List<Tag>)(List<?>) (Arrays.asList(XMLReader.getWays().values()));
        tempList.addAll((ArrayList<Tag>)(ArrayList<?>) Arrays.asList(XMLReader.getWays().values()));
        tempList.addAll((ArrayList<Tag>)(ArrayList<?>) Arrays.asList(XMLReader.getRelations().values()) );

        HashSet<Tag> tagsInBounds = Tree.getTagsInBounds(new RectHV(-200, -200, 200, 200));

        assertTrue(tagsInBounds.size() > 0);
    }

    @Test
    void testGetTagsNearPoint(){


        ArrayList<Tag> tagList = new ArrayList<Tag>(XMLReader.getNodes().valueCollection());

        Point2D point = new Point2D(tagList.get(0).getLon(), tagList.get(0).getLat());
        
        assertTrue(Tree.getTagsNearPoint(point).size() > 0);
    }

    @Test
    void testGetTagsFromPoint(){

        
        ArrayList<Tag> tagList = new ArrayList<>(XMLReader.getNodes().valueCollection());


        Point2D point = new Point2D(tagList.get(0).getLon(), tagList.get(0).getLat());
        
        assertTrue(Tree.getTagsFromPoint(point).size() > 0);
    }
}
