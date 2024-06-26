package util;

import java.util.Comparator;

import parser.TagAddress;

/**
 * Comparater for the {@link TagAddress} object, which is used to sort, the suggestionlist while the user
 * searches for an address.
 */
public class AddressComparator implements Comparator<TagAddress> {
    @Override
    public int compare(TagAddress address1, TagAddress address2) {
        // Compare house numbers
        int houseComparison = compareHouseNumbers(address1.getHouseNumber(), address2.getHouseNumber());
        if (houseComparison != 0) {
            return houseComparison;
        }
        
        // If house numbers are equal, compare streets
        int streetComparison = address1.getStreet().compareTo(address2.getStreet());
        if (streetComparison != 0) {
            return streetComparison;
        }
        
        // If streets are also equal, compare cities
        return address1.getCity().compareTo(address2.getCity());
    }
    
    /**
     * Custom method to compare house numbers
     * @param house1    The first housenumber
     * @param house2    The second housenumber
     * @return          Returns -1 if second is higher and +1 if the first is higher
     */
    private int compareHouseNumbers(String house1, String house2) {
        // Extract numeric parts from house numbers
        int number1 = extractNumber(house1);
        int number2 = extractNumber(house2);
        
        // Compare numeric parts
        if (number1 < number2) {
            return -1;
        } else if (number1 > number2) {
            return 1;
        } else {
            // If numeric parts are equal, compare alphabetic parts
            return house1.compareTo(house2);
        }
    }
    
    /**
     * Helper method to extract numeric part from house number
     * @param houseNumber   The address string
     * @return              Returns only the housenumber
     */
    private int extractNumber(String houseNumber) {
        try {
            return Integer.parseInt(houseNumber.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            // If it's not a pure number, return -1
            return -1;
        }
    }
}
