package org.convx.fsd;

import java.util.HashMap;
import java.util.Map;

/**
* @author johan
* @since 2011-11-24
*/
class SymbolTable {
    private Map<String, ElementBase> elements = new HashMap<String, ElementBase>();

    public void addElement(ElementBase element) {
        if (elements.containsKey(element.getId())) {
            throw new SchemaBuilderException("Symbol table already contains element with id: " + element.getId());
        }
        elements.put(element.getId(), element);
    }

    public ElementBase getElement(String id) {
        return elements.get(id);
    }

    public boolean containsElement(String id) {
        return elements.containsKey(id);
    }
}
