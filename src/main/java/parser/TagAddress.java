package parser;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import gnu.trove.map.hash.THashMap;

enum Address{
    ID, LAT, LON, CITY, COUNTRY, STREET, HOUSENUMBER, POSTCODE, MUNICIPALITY;
}

/**
 * Class for storing a {@link HashMap} of the adress tags.
 * Contains the following tags:
 * <p>
 * {@link Address#ID}, {@link Address#LAT}, {@link Address#LON}, {@link Address#STREET}, {@link Address#HOUSENUMBER}, {@link Address#POSTCODE}, {@link Address#MUNICIPALITY}
 * </p>
*/
public class TagAddress extends Tag<Address, THashMap<Address, Object>> {

    THashMap<Address, Object> address = new THashMap<>();

    TagAddress(XMLBuilder builder){
        address = new THashMap<Address, Object>(){
            {
                put(Address.ID, builder.getId());
                put(Address.LAT, builder.getLat());
                put(Address.LON, builder.getLon());
                put(Address.STREET, builder.getAddressBuilder().street);
                put(Address.HOUSENUMBER, builder.getAddressBuilder().house);
                put(Address.POSTCODE, builder.getAddressBuilder().postcode);
                put(Address.MUNICIPALITY, builder.getAddressBuilder().municipality);
                put(Address.CITY, builder.getAddressBuilder().city);
                put(Address.COUNTRY, builder.getAddressBuilder().country);
            }
        };
    }

    @Override
    public THashMap<Address, Object> getMap() {
        return this.address;
    }
    
    @Override
    public long getId() {
        return (long) address.get(Address.ID);
    }
    @Override
    public double getLat(){
        return (double) address.get(Address.LAT);
    }
    @Override
    public double getLon(){
        return (double) address.get(Address.LON);
    }

    @Override
    public boolean isEmpty(){
        return address.isEmpty();
    }

    public String getStreet() {
        return address.get(Address.STREET).toString();
    }

    public String getHouseNumber() {
        return address.get(Address.HOUSENUMBER).toString();
    }

    public String getPostcode() {
        return address.get(Address.POSTCODE).toString();
    }

    public String getMunicipality() {
        return address.get(Address.MUNICIPALITY).toString();
    }

    public String getCity() {
        return address.get(Address.CITY).toString();
    }

    public String getCountry() {
        return address.get(Address.COUNTRY).toString();
    }

    /**
     * Builder for a single address.
     * <p>
     * Constructs a instance of the builder, that later can be used to construct a {@link TagAddress}.
     * </p>
     */
    public static class AddressBuilder {
        public String street, house, postcode, country, city, municipality;
        private boolean isEmpty = true;

        public boolean isEmpty() {
            return isEmpty;
        }

        public AddressBuilder street(String _street) {
            street = _street;
            isEmpty = false;
            return this;
        }

        
        public AddressBuilder house(String _house) {
            house = _house;
            return this;
        }

        public AddressBuilder floor(String _floor) {
            return this;
        }

        public AddressBuilder side(String _side) {
            return this;
        }

        public AddressBuilder postcode(String _postcode) {
            postcode = _postcode;
            return this;
        }

        public AddressBuilder country(String _country) {
            country = _country;
            return this;
        }

        public AddressBuilder city(String _city) {
            city = _city;
            return this;
        }

        public AddressBuilder municipality(String _municipality) {
            municipality = _municipality;
            return this;
            }
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
