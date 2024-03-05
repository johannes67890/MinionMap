package org.parser;

import static org.parser.Tag.isEndTag;
import static org.parser.Tag.isStartTag;

import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.Tags;

public class TagAdress extends Tag<Tags.Adress, String> {
    TagAdress(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
        TagNode node = new TagNode(event);
        HashMap<Tags.Adress, String> adressTag = new HashMap<Tags.Adress, String>();
        
        node.getTag().forEach((key, value) -> {
            Tags.Adress adressKey = Tags.convert(key);
            String adressValue = value.toString();
            adressTag.put(adressKey, adressValue);
        });


        adressTag.putAll(setAdress(eventReader));
        super.setTag(adressTag);
    }

       static private HashMap<Tags.Adress , String> setAdress(XMLEventReader eventReader) throws XMLStreamException {
        HashMap<Tags.Adress , String> adress = new HashMap<Tags.Adress , String>();
        Tags.Adress key = null;
        String value = null;

        while(eventReader.hasNext())  {
            XMLEvent event = eventReader.nextEvent();

            if(isEndTag(event, "node")) return adress;
            if(isStartTag(event, "tag")) {
                key = Tags.convert(event.asStartElement().getAttributeByName(new QName("k")).getValue());
                value = event.asStartElement().getAttributes().next().getValue();
                if(key != null && value != null) adress.put(key, value);
            }
        }

        return adress;
    }
}
