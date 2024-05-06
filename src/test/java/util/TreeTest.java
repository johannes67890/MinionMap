package util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.Tag;
import parser.TagBound;
import parser.TagNode;
import parser.TagWay;
import parser.XMLReader;
import structures.KDTree.Point3D;
import structures.KDTree.Rect3D;
import structures.KDTree.Tree;

public class TreeTest {
    private XMLReader reader;
    private Tree tree;
    @BeforeEach
    void setUp() {
        reader = new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
        assertNotNull(reader);
        assertDoesNotThrow(() -> this.reader);
        Tree.initializeTree();
        for (TagWay way : reader.getWays().valueCollection()){
            Tree.insertTagWayInTree(way);
        }
    }

    @Test
    void testInsertTagInTree() {
        
        assertEquals(1 , Tree.getNearestOfClassBruteForce(new TagNode(0, 0), TagWay.class).size());
    }


    @Test
    void testGetTagsInBounds() {
        
        TagBound bound = reader.getBound();
        HashSet<Tag> tagsInBounds = Tree.getTagsInBounds(new Rect3D(bound.getMinLon(), bound.getMinLat(), 0, bound.getMaxLon(), bound.getMaxLat(), 10));

        assertEquals(211 ,tagsInBounds.size());
    }

    @Test
    void testGetTagsNearPoint(){
        ArrayList<Tag> tagList = new ArrayList<>(reader.getWays().valueCollection());

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
