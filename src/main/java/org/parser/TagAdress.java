package org.parser;


import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

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
    TagAdress(XMLStreamReader event, XMLStreamReader eventReader) throws XMLStreamException {
        TagNode node = new Tag.TagNode(event);
        HashMap<Adress, String> adressTag = new HashMap<Adress, String>();
        
        node.getTag().forEach((key, value) -> {
            Adress adressKey = Adress.convert(key);
            String adressValue = value.toString();
            adressTag.put(adressKey, adressValue);
        });


        adressTag.putAll(setAdress(eventReader));
        super.setTag(adressTag);
    }

       static private HashMap<Adress , String> setAdress(XMLStreamReader eventReader) throws XMLStreamException {
        HashMap<Adress , String> adress = new HashMap<Adress, String>();
        Adress key = null;
        String value = null;
        // TODO: Refactor this method to use the XMLStreamReader instead of the XMLEvent.
        // while(eventReader.hasNext())  {
        //     if(isEndTag(event, "node")) return adress;
        //     if(isStartTag(event, "tag")) {
        //         key = Adress.convert(event.asStartElement().getAttributeByName(new QName("k")).getValue());
        //         value = event.asStartElement().getAttributes().next().getValue();
        //         if(key != null && value != null) adress.put(key, value);
        //     }
        // }

        return adress;
    }
}
