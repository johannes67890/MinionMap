package org.parser;
import java.util.HashMap;

enum Node {
    ID, LAT, LON;
}
public class TagNode extends HashMap<Node,Number> {
    public TagNode(XMLReader.Builder builder) {
        super(new HashMap<Node, Number>(){
            {
                put(Node.ID, builder.getID());
                put(Node.LAT, builder.getLat());
                put(Node.LON, builder.getLon());
            }
        });
    }
}
