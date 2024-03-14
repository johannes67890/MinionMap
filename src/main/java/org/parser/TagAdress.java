package org.parser;

import java.util.HashMap;

enum Adress{
    ID, LAT, LON, CITY, STREET, HOUSENUMBER, POSTCODE, MUNICIPALITY;
}

public class TagAdress extends HashMap<Adress, String> {
    
    TagAdress(XMLReader.Builder builder){
        super(new HashMap<Adress, String>(){
            {
                put(Adress.ID, builder.getID().toString());
                put(Adress.LAT, builder.getLat().toString());
                put(Adress.LON, builder.getLon().toString());
                put(Adress.STREET, builder.getAdressBuilder().street);
                put(Adress.HOUSENUMBER, builder.getAdressBuilder().house);
                put(Adress.POSTCODE, builder.getAdressBuilder().postcode);
                put(Adress.MUNICIPALITY, builder.getAdressBuilder().municipality);
            }
        });
    }

    public Long getID() {
        return Long.parseUnsignedLong(this.get(Adress.ID));
    }
}
