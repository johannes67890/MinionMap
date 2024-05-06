package parser;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gnu.trove.list.linked.TLinkedList;
import util.FileDistributer;
import util.FileHandler;

import java.io.File;

public class TagWayTest {
    private XMLReader reader;

    @BeforeEach
    void testXMLReaderStartup() {
        reader = new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
        assertNotNull(reader);
        assertDoesNotThrow(() -> this.reader);
    }

    @Test
    void testConstructor(){
        TLinkedList<TagNode> nodes = new TLinkedList<>();
        nodes.add(new TagNode(0, 0));
        nodes.add(new TagNode(1, 1));
        nodes.add(new TagNode(2, 2));

        TagWay way = new TagWay(0l, "Way", nodes, (short)20, null);

        assertEquals(20, way.speedLimit);
        assertEquals("Way", way.name);
        assertEquals(0l, way.id);
    }
}
