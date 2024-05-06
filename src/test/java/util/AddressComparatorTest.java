package util;

import static org.junit.Assert.*;

import org.junit.Test;

import parser.TagAddress;

public class AddressComparatorTest {

    @Test
    public void compare(){

        AddressComparator comparator = new AddressComparator();
        TagAddress address1 = new TagAddress(12, 100, 100,
         "Stenvænget", "3", "3400", "", "Gadevang", "Denmark");
        

        TagAddress address2 = new TagAddress(12, 100, 100,
         "Stenvænget", "4", "3400", "", "Gadevang", "Denmark");
        
        int compareInt = comparator.compare(address1, address2);

        assertEquals(-1, compareInt);
        assertEquals(1, comparator.compare(address2, address1));
        assertEquals(0, comparator.compare(address1, address1));


        address1 = new TagAddress(12, 100, 100,
         "Btenvænget", "3", "3400", "", "Gadevang", "Denmark");
        

        address2 = new TagAddress(12, 100, 100,
         "AStenvænget", "3", "3400", "", "Gadevang", "Denmark");
        

        assertEquals(1, comparator.compare(address1, address2));
        assertEquals(-1, comparator.compare(address2, address1));
        assertEquals(0, comparator.compare(address1, address1));

        address1 = new TagAddress(12, 100, 100,
         "AStenvænget", "3", "3400", "", "Gadevang", "Denmark");
        

        address2 = new TagAddress(12, 100, 100,
         "AStenvænget", "3", "3400", "", "Hadevang", "Denmark");
        

        assertEquals(-1, comparator.compare(address1, address2));
        assertEquals(1, comparator.compare(address2, address1));
        assertEquals(0, comparator.compare(address1, address1));


    }
    
}