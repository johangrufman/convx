package org.convx.writer;

import org.junit.Before;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.StartElement;
import java.io.StringWriter;

/**
 * @author johan
 * @since 2011-10-21
 */
public class AbstractWriterTest {
    private XMLEventFactory xmlEventFactory;
    private StringWriter outputWriter;
    protected WriterContext context;

    @Before
    public void setup() {
        outputWriter = new StringWriter();
        context = new WriterContext(outputWriter);
        xmlEventFactory = XMLEventFactory.newFactory();
    }

    protected void pushStartElement(String localName) {
        context.consumeStartElement(createStartElement(localName));
    }

    protected StartElement createStartElement(String localName) {
        return xmlEventFactory.createStartElement(new QName(localName), null, null);
    }

    protected void pushEndElement(String localName) {
        context.consumeEndElement(xmlEventFactory.createEndElement(new QName(localName), null));
    }

    protected void pushEmptyElement(String localName) {
        pushStartElement(localName);
        pushEndElement(localName);
    }

    protected void pushCharacters(String characters) {
        context.consumeCharacters(xmlEventFactory.createCharacters(characters));
    }

    protected String output() {
        return outputWriter.toString();
    }
}
