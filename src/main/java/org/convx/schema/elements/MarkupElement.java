package org.convx.schema.elements;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.XMLEvent;

/**
 * @author johan
 * @since 2011-06-27
 */
public class MarkupElement implements Element {
    private static final XMLEventFactory factory = XMLEventFactory.newFactory();

    public static MarkupElement startDocument() {
        return new MarkupElement(factory.createStartDocument());
    }

    public static MarkupElement endDocument() {
        return new MarkupElement(factory.createEndDocument());
    }

    public static MarkupElement startElement(String name) {
        return new MarkupElement(factory.createStartElement(new QName(name), null, null));
    }

    public static MarkupElement endElement(String name) {
        return new MarkupElement(factory.createEndElement(new QName(name), null));
    }

    public static MarkupElement characters(String content) {
        return new MarkupElement(factory.createCharacters(content));
    }

    private XMLEvent event;

    private MarkupElement(XMLEvent event) {
        this.event = event;
    }

    public XMLEvent event() {
        return event;
    }
}
