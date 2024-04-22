package org;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;

import parser.TagAddress;
import parser.Type;
import parser.XMLReader;
import util.FileDistributer;
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
            new Tree(XMLReader.getWays());
            Tree.insertTagInTree(start);
            ArrayList<Tag> t = Tree.getTagsNearTag(start, Type.RESIDENTIAL);
            assertEquals(1, t.size());
        }
    }
}
