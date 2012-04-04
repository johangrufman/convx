package org.convx.reader;

import javax.xml.stream.XMLInputFactory;
import java.util.HashMap;
import java.util.Map;

public class XmlInputFactoryProperties {
    static final Map<String, Object> properties = new HashMap<String, Object>();

    static {
        properties.put(XMLInputFactory.IS_VALIDATING, false);
        properties.put(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        properties.put(XMLInputFactory.IS_COALESCING, false);
        properties.put(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        properties.put(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        properties.put(XMLInputFactory.SUPPORT_DTD, false);
        properties.put(XMLInputFactory.REPORTER, null);
        properties.put(XMLInputFactory.RESOLVER, null);
        properties.put(XMLInputFactory.ALLOCATOR, null);
    }

    public static Object getProperty(String name) {
        if (XmlInputFactoryProperties.properties.containsKey(name)) {
            return XmlInputFactoryProperties.properties;
        } else {
            throw new IllegalArgumentException();
        }
    }


}