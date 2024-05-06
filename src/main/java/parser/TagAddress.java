package parser;

import java.io.Serializable;

import util.Type;

enum Address{
    ID, LAT, LON, CITY, COUNTRY, STREET, HOUSENUMBER, POSTCODE, MUNICIPALITY;
}

/**
 * Class representing an address in the OSM XML file.
 * 
 * <p>
 * The address is represented by the following attributes:
 * <ul>
 * <li>{@link Address#ID} - The id of the address.</li>
 * <li>{@link Address#LAT} - The latitude of the address.</li>
 * <li>{@link Address#LON} - The longitude of the address.</li>
 * <li>{@link Address#STREET} - The street of the address.</li>
 * <li>{@link Address#HOUSENUMBER} - The house number of the address.</li>
 * <li>{@link Address#POSTCODE} - The post code of the address.</li>
 * <li>{@link Address#MUNICIPALITY} - The municipality of the address.</li>
 * </ul>
 * </p>
*/
public class TagAddress extends Tag  implements Comparable<TagAddress>{
    long id;
    float lat;
    float lon;
    String street;
    String house;
    String postCode;
    String municipality;
    String city;
    String country;

    /**
     * Create a new TagAddress with the {@link TagAddress.AddressBuilder} class.
     * @param builder - The {@link TagAddress.AddressBuilder} to get the attribute from.
     */
    TagAddress(XMLBuilder builder){
        id = builder.getId();
        lat = builder.getLat();
        lon = builder.getLon();
        street = builder.getAddressBuilder().street;
        house = builder.getAddressBuilder().house;
        postCode = builder.getAddressBuilder().postcode;
        municipality =  builder.getAddressBuilder().municipality;
        city = builder.getAddressBuilder().city;
        country = builder.getAddressBuilder().country;
    }

    /**
     * Create a new TagAddress with the given values.
     * @param id - The id of the address.
     * @param lat - The latitude of the address.
     * @param lon - The longitude of the address.
     * @param street - The street of the address.
     * @param house - The house number of the address.
     * @param postCode - The post code of the address.
     * @param municipality - The municipality of the address.
     * @param city - The city of the address.
     * @param country - The country of the address.
     */
    public TagAddress (long id, float lat, float lon, String street, String house, String postCode, String municipality, String city, String country){
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.street = street;
        this.house = house;
        this.postCode = postCode;
        this.municipality = municipality;
        this.city = city;
        this.country = country;
    }

    @Override
    public long getId() {
        return id;
    }
    @Override
    public float getLat() {
        return lat;
    }
    @Override
    public float getLon() {
        return lon;
    }

    @Override
    public Type getType() {
        throw new UnsupportedOperationException("TagAddress does not have a type.");
    }

    /**
     * Get the street of the address.
     * @return The street of the address.
     */
    public String getStreet() {
        return street;
    }

    /**
     * Set the street of the address.
     * @param street - The street of the address.
     */
    public void setStreet(String street){
        this.street = street;
    }

    /**
     * Get the house number of the address.
     * @return The house number of the address.
     */
    public String getHouseNumber() {
        return house;
    }

    /**
     * Set the house number of the address.
     * @param houseNumber - The house number of the address.
     */
    public void setHouseNumber(String houseNumber){
        this.house = houseNumber;
    }

    /**
     * Get the post code of the address.
     * @return The post code of the address.
     */
    public String getPostcode() {
        return postCode;
    }

    /**
     * Set the post code of the address.
     * @param postcode - The post code of the address.
     */
    public void setPostCode(String postcode){
        this.postCode = postcode;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality){
        this.municipality = municipality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String toString() {
        if (!house.equals("")){
            return street + ", " + house + ", " + postCode + " " + city;
        } else{
            return street + ", " + postCode + " " + city;
        }
    }

    /**
     * Builder for a single address.
     * <p>
     * Constructs a instance of the builder, that later can be used to construct a {@link TagAddress}.
     * </p>
     * @see {@link XMLBuilder} for the usage of the builder.
     * @see {@link TagAddress} for the final object.
     */
    public static class AddressBuilder  implements Serializable{
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

    

        
    @Override
    public int compareTo(TagAddress tagAddress) {
        if (tagAddress == null){
            throw new IllegalArgumentException("Paramenter object is of null");
        }else{
            int comparison = this.city.compareTo(tagAddress.city);

            if (comparison == 0){
                comparison = this.street.compareTo(tagAddress.street);
                if (comparison == 0){
                    return this.house.compareTo(tagAddress.house);
                }else{
                    return comparison;
                }
            }else{
                return comparison;
            }

        }
    }
}
