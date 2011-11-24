package org.convx.fsd;

import java.util.HashMap;
import java.util.Map;

/**
* @author johan
* @since 2011-11-24
*/
class SymbolTable {
    private Map<String, ElementBaseType> elements = new HashMap<String, ElementBaseType>();

    public void add(ElementBaseType element) {
        if (elements.containsKey(element.getId())) {
            throw new SchemaBuilderException("Symbol table already contains element with id: " + element.getId());
        }
        elements.put(element.getId(), element);
    }

    public ElementBaseType get(String id) {
        return elements.get(id);
    }

    public boolean containsElement(String id) {
        return elements.containsKey(id);
    }
}
