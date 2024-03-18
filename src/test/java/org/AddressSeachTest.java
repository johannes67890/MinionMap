package org;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Address.Address;
import Address.HelloFX;

public class AddressSeachTest {


    HelloFX helloFX;
    Address a;


    @BeforeEach
    void setUp() {
        helloFX = new HelloFX();
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

        helloFX.readFiles();
        String address = "stenvænget 3400 hillerød";
        a = Address.parse(address);

        assertTrue(helloFX.getCities().contains(a.city));
        assertTrue(helloFX.getStreets().contains(a.street));
        assertTrue(helloFX.getPostCodes().contains(a.postcode));

    }





    
}
