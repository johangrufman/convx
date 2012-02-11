package org.convx.reader;

import org.convx.reader.elements.Element;
import org.convx.reader.elements.MarkupElement;
import org.convx.reader.elements.NodeElement;
import org.convx.schema.Schema;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * @author johan
 * @since 2011-05-21
 */
public class FlatFileParser implements XMLEventReader {
    private ParserContext context;

    private Stack<Element> parserStack = new Stack<Element>();

    ReaderNode rootReaderNode;

    public FlatFileParser(Schema schema, Reader reader) {
        rootReaderNode = schema.root().asReaderNode();
        context = new ParserContext(reader, rootReaderNode.lookAhead());
        init();
    }

    private void init() {
        parserStack.push(MarkupElement.endDocument());
        parserStack.push(new NodeElement(rootReaderNode));
        parserStack.push(MarkupElement.startDocument());
    }

    public XMLEvent nextEvent() {
        if (parserStack.isEmpty()) {
            throw new NoSuchElementException();
        }
        Element element = parserStack.pop();
        if (element instanceof MarkupElement) {
            return ((MarkupElement) element).event();
        }
        ((NodeElement) element).parse(parserStack, context);
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
