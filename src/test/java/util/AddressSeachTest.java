package util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gui.Search;
import parser.TagAddress.SearchAddress;

public class AddressSeachTest {


    Search addressSearch;
    SearchAddress a;


    @BeforeEach
    void setUp() {
        // addressSearch = new Search();
    }


    @Test
    void CapitalFirstLetter() {
        
        String expectedAddress = "stenvænget";
        a = new SearchAddress(expectedAddress);
        
        assertEquals(a.street, "Stenvænget");
        
    }


    @Test
    void FindAddress(){

        // addressSearch.readFiles();
        // String expectedAddress = "stenvænget 3400 hillerød";
        // a = new SearchAddress(expectedAddress);

        // assertTrue(addressSearch.getCities().contains(a.city));
        // assertTrue(addressSearch.getStreets().contains(a.street));
        // assertTrue(addressSearch.getPostCodes().contains(a.postcode));

    }





    
}
