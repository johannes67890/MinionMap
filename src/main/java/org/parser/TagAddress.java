package org.parser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum Address{
    ID, LAT, LON, CITY, STREET, HOUSENUMBER, POSTCODE, MUNICIPALITY;
}

/**
 * Class for storing a {@link HashMap} of the adress tags.
 * Contains the following tags:
 * <p>
 * {@link Address#ID}, {@link Address#LAT}, {@link Address#LON}, {@link Address#STREET}, {@link Address#HOUSENUMBER}, {@link Address#POSTCODE}, {@link Address#MUNICIPALITY}
 * </p>
*/
public class TagAddress extends HashMap<Address, String> {

    TagAddress(XMLReader.Builder builder){
        super(new HashMap<Address, String>(){
            {
                put(Address.ID, builder.getID().toString());
                put(Address.LAT, builder.getLat().toString());
                put(Address.LON, builder.getLon().toString());
                put(Address.STREET, builder.getAddressBuilder().street);
                put(Address.HOUSENUMBER, builder.getAddressBuilder().house);
                put(Address.POSTCODE, builder.getAddressBuilder().postcode);
                put(Address.MUNICIPALITY, builder.getAddressBuilder().municipality);
                put(Address.CITY, builder.getAddressBuilder().city);
            }
        });
    }

    public String getStreet() {
        return this.get(Address.STREET);
    }

    public String getHouseNumber() {
        return this.get(Address.HOUSENUMBER);
    }

    public String getPostcode() {
        return this.get(Address.POSTCODE);
    }

    public String getMunicipality() {
        return this.get(Address.MUNICIPALITY);
    }

    public String getCity() {
        return this.get(Address.CITY);
    }

    public String getID() {
        return this.get(Address.ID);
    }

    public String getLat() {
        return this.get(Address.LAT);
    }

    public String getLon() {
        return this.get(Address.LON);
    }


    
    public static class SearchAddress {
        public String street, house, floor, side, postcode, city;

        public SearchAddress(String input){
            final String REGEX = "(?<street>[A-Za-zØÆÅåæø ]+)? ?(?<house>[0-9]{1,3}[A-Za-z]{0,2})? ?(?<floor> st| [0-9]{1,3})? ?(?<side>tv|th|mf)?\\.? ?(?<postcode>[0-9]{4})? ?(?<city>[A-Za-ØÆÅåøæ ]+)?$";
            final Pattern PATTERN = Pattern.compile(REGEX);

            Matcher matcher = PATTERN.matcher(input);

            if(matcher.matches()){
                this.house = matcher.group("house");
                this.floor = matcher.group("floor");
                this.side = matcher.group("side");
                this.postcode = matcher.group("postcode");

                if(matcher.group("floor") != null && !matcher.group("floor").contains(".")){
                    this.floor += ".";
                } else{this.floor = "";}

                if(matcher.group("side") != null && !matcher.group("side").contains(".")){
                    this.side += ".";
                } else{this.side = ""; }

                if(matcher.group("street") != null){
                    String[] streetSplit = matcher.group("street").split(" ");
                    this.street = "";
                    for(String splitString : streetSplit){
                        splitString = splitString.substring(0,1).toUpperCase() +
                        splitString.substring(1).toLowerCase();
                        this.street += splitString + " ";
                    }
                    this.street = this.street.substring(0, this.street.length() - 1);
                    
                } else{
                    this.street = "";
                }

                if(matcher.group("city") != null){
                    String[] citySplit = matcher.group("city").split(" ");
                    this.city = "";
                    for(String splitString : citySplit){
                        splitString = splitString.substring(0,1).toUpperCase() +
                        splitString.substring(1).toLowerCase();
                        this.city += splitString + " ";
                    }
                    this.city = this.city.substring(0, this.city.length() - 1);
                    
                } else{
                    this.city = "";
                }

            } else{
                throw new IllegalArgumentException("Invalid input");
            }
        }

        public String toString() {
            return street + " " + house + ", " + floor + " " + side + "\n"
                    + postcode + " " + city;
        }
    }
}
