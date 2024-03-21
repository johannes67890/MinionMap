package org.parser;

import java.util.HashMap;

public class Tag<E extends Enum<E>> extends HashMap<E, Object> {
    // existing code
    public Tag(HashMap<E, ?> map) {
        super(map);
    }

    public Tag(E key, Object value) {
        super();
        this.put(key, value);
    }
}
