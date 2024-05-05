package org;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import parser.Tag;
import parser.TagAddress;
import parser.XMLReader;
import util.Tree;
import util.Trie;
import java.util.List;

public class TrieTest {

    private Trie trie;
    private XMLReader reader;

    //private XMLReader reader;
    @BeforeEach
    void setUp() {
        this.reader = new XMLReader("src/test/java/org/ressources/testmap.osm");
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

    //FAILING TESTS FOR UNUSED METHODS:

    // @Test
    // public void testContainsSeach() {

    //     boolean containsSearch = trie.containsSearch("h");
    //     assertTrue(containsSearch);

    //     containsSearch = trie.containsSearch("a");
    //     assertFalse(containsSearch);

    //     containsSearch = trie.containsSearch("hvidovr");
    //     assertTrue(containsSearch);

    // }

    // @Test
    // public void testContainsFullSearch() {

    //     boolean containsSearch = trie.fullContainsSearch("Hvidovre Kirkeplads, 2650 Hvidovre");
    //     assertTrue(containsSearch);

    //     containsSearch = trie.fullContainsSearch("a");
    //     assertFalse(containsSearch);

    //     containsSearch = trie.fullContainsSearch("hvidovr");
    //     assertTrue(containsSearch);

    // }






    
}
