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
public class TagAddress extends Tag<Address> {
    TagAddress(XMLReader.Builder builder){
        super(new HashMap<Address, Object>(){
            {
                put(Address.ID, builder.getID().toString());
                put(Address.LAT, builder.getLat().toString());
                put(Address.LON, builder.getLon().toString());
                put(Address.STREET, builder.getAddressBuilder().street);
                put(Address.HOUSENUMBER, builder.getAddressBuilder().house);
                put(Address.POSTCODE, builder.getAddressBuilder().postcode);
                put(Address.MUNICIPALITY, builder.getAddressBuilder().municipality);
            }
        });
    }
}
