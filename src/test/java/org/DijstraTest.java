package org;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.princeton.cs.algs4.Point2D;

import java.io.File;
import java.util.ArrayList;

import parser.TagAddress;
import parser.Type;
import parser.XMLReader;
import util.FileDistributer;
import util.KdTree;
import util.Tree;
import parser.Tag;
import parser.TagAddress;

public class DijstraTest {
    private XMLReader reader;
    @BeforeEach
    void testXMLReaderStartup() {
        this.reader = new XMLReader("src/test/java/org/ressources/testMap.osm");
        assertNotNull(reader);
        assertDoesNotThrow(() -> this.reader);
    }

    @Test
    void testE() {
        TagAddress start = XMLReader.getAddressById(1447913335l);
        if(start instanceof TagAddress){
            //new Tree(XMLReader.getWays());
            Tree.initialize(new ArrayList<Tag>(XMLReader.getWays().valueCollection()));;
            Tree.insertTagInTree(start);
            KdTree tree = Tree.getKDTree();
            Tag tag = tree.nearestOfType(new Point2D(start.getLon(), start.getLat()), Type.RESIDENTIAL, 10);
            ArrayList<Tag> t = Tree.getTagsNearTag(start, Type.RESIDENTIAL);
            assertEquals(367167754, tag.getId());
        }
    }
}
