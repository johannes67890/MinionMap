package org.parser;

import java.util.HashMap;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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

    public void createXMLElement(XMLStreamWriter wrinter) throws XMLStreamException{
        wrinter.writeStartElement("bounds");
        // Iterate through the attributes and write
            wrinter.writeAttribute("id", this.get(Address.ID).toString());
            wrinter.writeAttribute("lat", this.get(Address.LAT).toString());
            wrinter.writeAttribute("lon", this.get(Address.LON).toString());
            wrinter.writeAttribute("street", this.get(Address.STREET).toString());
            wrinter.writeAttribute("housenumber", this.get(Address.HOUSENUMBER).toString());
            wrinter.writeAttribute("postcode", this.get(Address.POSTCODE).toString());
            wrinter.writeAttribute("municipality", this.get(Address.MUNICIPALITY).toString());
        wrinter.writeEndElement();
    }
    
}
