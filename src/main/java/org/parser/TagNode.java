package org.parser;

import org.Tags;
import java.util.HashMap;
import javax.xml.stream.events.XMLEvent;
import java.math.BigDecimal;

public class TagNode extends Tag<Tags.Node, Number> {
    TagNode(XMLEvent event) {
        super(setNode(event));
    }

    static private HashMap<Tags.Node, Number> setNode(XMLEvent event){
        HashMap<Tags.Node, Number> node = new HashMap<>();

        event.asStartElement().getAttributes().forEachRemaining(attribute -> {
            switch (attribute.getName().getLocalPart()) {
                case "id":
                    node.put(Tags.Node.ID, Long.parseLong(attribute.getValue()));
                    break;
                case "lat":
                    node.put(Tags.Node.LAT, new BigDecimal(attribute.getValue()));
                    break;
                case "lon":
                    node.put(Tags.Node.LON, new BigDecimal(attribute.getValue()));
                    break;
                default:
                    break;
            }
        });

        return node;
    }
}
