package org.parser;

import java.util.HashMap;


enum Way {
    ID, REFS, NAME, TYPE
}
public class TagWay extends HashMap<Way, Object>{
    public TagWay(XMLReader.Builder builder) {
        super(new HashMap<Way, Object>(){
            {
                put(Way.ID, builder.getID());
                put(Way.REFS, builder.getWayBuilder().getRefNodes());
                put(Way.TYPE, builder.getType());
                put(Way.NAME, builder.getName());
            }
        });
    }

    public Long getId() {
        return (Long) this.get(Way.ID);
    }

    public Type getType() {
        return (Type) this.get(Way.TYPE);
    }

    public Long[] getNodes() {
        return (Long[]) this.get(Way.REFS);
    }

    // public Long[] getTags() {
    //     return tags;
    // }

    public boolean isEmpty() {
        return getNodes().length == 0;
    }

    public int size() {
        return getNodes().length;
    }


}
