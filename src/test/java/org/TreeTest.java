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
import util.KdTree;
import util.Tree;

public class TreeTest {
    private XMLReader reader;
    private Tree tree;

    @BeforeEach
    void setUp() {
        this.reader = new XMLReader("src/test/java/org/ressources/map.osm");
        ArrayList<Tag<?>> tempList = new ArrayList<>();
        tempList.addAll(reader.getWays().values());

        this.tree = new Tree(tempList);
    }

    @Test
    void testInsertTagInTree() {
        assertNotNull(tree.getNearestPoint(new Point2D(20, 20)));
    }


    @Test
    void testGetTagsInBounds() {
        ArrayList<Tag<?>> tempList = new ArrayList<>(reader.getNodes().values());
        tempList.addAll(reader.getWays().values());
        tempList.addAll(reader.getRelations().values());
        this.tree = new Tree(tempList);

        HashSet<Tag<?>> tagsInBounds = tree.getTagsInBounds(new RectHV(-200, -200, 200, 200));

        assertTrue(tagsInBounds.size() > 0);
    }

    @Test
    void testGetTagsNearPoint(){
        HashMap<Long,TagNode> hM = new HashMap<>(reader.getNodes());
        ArrayList<Tag<?>> tagList = new ArrayList<>(hM.values());

        Point2D point = new Point2D(tagList.get(0).getLon(), tagList.get(0).getLat());
        
        assertTrue(tree.getTagsNearPoint(point).size() > 0);
    }

    @Test
    void testGetTagsFromPoint(){
        HashMap<Long,TagNode> hM = new HashMap<>(reader.getNodes());
        ArrayList<Tag<?>> tagList = new ArrayList<>(hM.values());

        Point2D point = new Point2D(tagList.get(0).getLon(), tagList.get(0).getLat());
        
        assertTrue(tree.getTagsFromPoint(point).size() > 0);
    }
}
