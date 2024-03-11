package org.parser;


import java.util.HashMap;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;

enum Adress{
    ID, LAT, LON, CITY, STREET, HOUSENUMBER, POSTCODE, COUNTRY;

    static public Adress convert(Node node) {
        switch (node) {
            case ID:
                return Adress.ID;
            case LAT:
                return Adress.LAT;
            case LON:
                return Adress.LON;
            default:
                throw new IllegalArgumentException("Invalid node: " + node);
        }
    }
    
    static public Adress convert(String value) {
        switch (value) {
            case "addr:city":
                return Adress.CITY;
            case "addr:street":
                return Adress.STREET;
            case "addr:housenumber":
                return Adress.HOUSENUMBER;
            case "addr:postcode":
                return Adress.POSTCODE;
            case "addr:country":
                return Adress.COUNTRY;
            default:
                return null;
        }
    }
}


public class TagAdress extends Tag<Adress, String> {

    public TagAdress(XMLStreamReader reader) throws XMLStreamException {
        super(setAdress(reader));
    }

    private static HashMap<Adress, String> setAdress(XMLStreamReader reader) throws XMLStreamException {
        HashMap<Adress, String> adress = new HashMap<Adress, String>();
        while (reader.hasNext()) {
            reader.next();
            if (reader.getEventType() == XMLStreamReader.START_ELEMENT) {
                Adress node = Adress.convert(reader.getAttributeLocalName(0));
                if (node != null) {
                    adress.put(node, reader.getAttributeValue(0));
                }
            }
        }
        return adress;
    }
}
