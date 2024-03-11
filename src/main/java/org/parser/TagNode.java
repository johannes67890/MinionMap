package org.parser;
import java.util.HashMap;

import javax.xml.stream.XMLStreamReader;
enum Node {
    ID, LAT, LON;
}
public class TagNode extends Tag<Node, Number> {
    public TagNode(XMLStreamReader reader) {
        super(setNode(reader));
    }

    private static HashMap<Node, Number> setNode(XMLStreamReader reader){
        return new HashMap<Node, Number>(){
            {
                put(Node.ID, Long.parseUnsignedLong(reader.getAttributeValue(null, "id")));
                put(Node.LAT, getAttributeByBigDecimal(reader, "lat"));
                put(Node.LON, getAttributeByBigDecimal(reader, "lon"));
            }
        
        };
    }
}
