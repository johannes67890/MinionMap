package org.parser;

import java.util.HashMap;

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
}
