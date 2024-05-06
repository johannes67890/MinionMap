package util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import parser.TagAddress;
import parser.XMLReader;
import structures.Trie;

public class TrieTest {

    private Trie trie;
    private XMLReader reader;

    //private XMLReader reader;
    @BeforeEach
    void setUp() {
        reader = new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
        assertNotNull(reader);
        assertDoesNotThrow(() -> this.reader);
        trie = XMLReader.getTrie();
    }

    

  
    @Test
    public void testGetAddressObject() {

        TagAddress address = trie.getAddressObject("Hvidovre Kirkeplads 2650 Hvidovre", " 2");
        assertTrue(address.toString().equals("Hvidovre Kirkeplads, 2, 2650 Hvidovre"));

        address = trie.getAddressObject("Hvidovre Kirkeplads 2650 Hvidovre", " 3");
        assertTrue(address.toString().equals("Hvidovre Kirkeplads, 3, 2650 Hvidovre"));

    }

    @Test
    public void testGetAddressSuggestions() {

        List<TagAddress> addresses = trie.getAddressSuggestions("Hvidovre", 5);
        assertEquals(addresses.size(), 2);

        // more suggestions will be added, as the list now goes deeper to addresses with housenumbers.
        addresses = trie.getAddressSuggestions("Hvidovrevej", 5);
        assertEquals(addresses.size(), 14);

        addresses = trie.getAddressSuggestions("Hvidovre Kirkeplads, 2, 2650 Hvidovre", 5);
        assertEquals(addresses.size(), 0);
    }
}
