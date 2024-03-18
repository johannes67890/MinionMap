package org;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Address.Address;
import Address.AddressSearchPage;

public class AddressSeachTest {


    AddressSearchPage addressSearch;
    Address a;


    @BeforeEach
    void setUp() {
        addressSearch = new AddressSearchPage();
    }

    @Test
    void CapitalFirstLetter() {
        
        String address = "stenvænget";
        a = Address.parse(address);
        Boolean b = a.equals(address);
        assertEquals(a.street, "Stenvænget");

    }

    @Test
    void FindAddress(){

        addressSearch.readFiles();
        String address = "stenvænget 3400 hillerød";
        a = Address.parse(address);

        assertTrue(addressSearch.getCities().contains(a.city));
        assertTrue(addressSearch.getStreets().contains(a.street));
        assertTrue(addressSearch.getPostCodes().contains(a.postcode));

    }





    
}
