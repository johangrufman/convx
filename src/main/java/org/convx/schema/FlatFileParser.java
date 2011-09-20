package org.convx.schema;

import org.convx.schema.elements.Element;
import org.convx.schema.elements.MarkupElement;
import org.convx.schema.elements.NodeElement;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @author johan
 * @since 2011-05-21
 */
public class FlatFileParser implements XMLEventReader {
    private ParserContext context;
    private Stack<Element> parserStack = new Stack<Element>();

    public FlatFileParser(Schema schema, Reader reader) {
        context = new ParserContext(reader, schema.lookAhead());
        init(schema);
    }

    private void init(Schema schema) {
        parserStack.push(MarkupElement.endDocument());
        parserStack.push(new NodeElement(schema.root()));
        parserStack.push(MarkupElement.startDocument());
    }

    public XMLEvent nextEvent() {
        if (parserStack.isEmpty()) {
            throw new NoSuchElementException();
        }
        Element element = parserStack.pop();
        if (element instanceof MarkupElement) {
            return ((MarkupElement)element).event();
        }
        ((NodeElement)element).parse(parserStack, context);
        return nextEvent();
    }

    public boolean hasConsumedAllInput() {
        return !context.hasMoreCharacters();
    }

    public boolean hasNext() {
        return !parserStack.isEmpty();
    }

    public Object next() {
        return nextEvent();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public XMLEvent peek() throws XMLStreamException {
        return null;
    }

    public String getElementText() throws XMLStreamException {
        return null;
    }

    public XMLEvent nextTag() throws XMLStreamException {
        return null;
    }

    public Object getProperty(String name) throws IllegalArgumentException {
        return null;
    }

    public void close() throws XMLStreamException {

    }


}
