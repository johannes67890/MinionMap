package util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import gui.Search;
import parser.TagAddress;
import parser.XMLReader;
import structures.Trie;

public class SearchTest {


    private Trie trie;
    private Search search;

    @BeforeEach
    public void setup(){
        synchronized(this){
            new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
            trie = XMLReader.getTrie();
            assertNotNull(trie);
            search = new Search(trie);
            assertNotNull(search);
        }
    }

    @Test
    public void getAddress(){
        trie = XMLReader.getTrie();
        assertNotNull(trie);
        search = new Search(trie);
        assertNotNull(search);

        TagAddress tagAddress = search.getAddress("Hvidovrevej 2650 Hvidovre", "132C");


        tagAddress = search.getAddress("Hvidovrevej 2650 Hvidovre", "02834908");
        
        assertNull(tagAddress);
    }


    @Test
    public void getSuggestion(){

        synchronized(this){
          new XMLReader(FileHandler.getFileInputStream(new File(FileDistributer.testMap.getFilePath())));
            trie = XMLReader.getTrie();
            assertNotNull(trie);
            search = new Search(trie);
            assertNotNull(search);

        String address = "hvidovre kirkeplads";

        ArrayList<TagAddress> suggestion = search.getSuggestions(address);

        TagAddress address1 = suggestion.get(0);

        TagAddress address2 = suggestion.get(1);        
        assertTrue(address1.getHouseNumber().compareTo(address2.getHouseNumber()) < 0);
        }
    }
    
}
