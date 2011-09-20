package org.convx.writer;

import java.io.PrintWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * @author johan
 * @since 2011-09-16
 */
public class Writer2 {
    private WriterContext writerContext = new WriterContext(new PrintWriter(System.out));


    public Writer2(WriterNode root) {
        new RootWriterNode(root).init(writerContext);
    }

    public void add(XMLEvent event) throws XMLStreamException {
        if (event.isStartElement()) {
            writerContext.consumeStartElement(event.asStartElement());
        } else if (event.isEndElement()) {
            writerContext.consumeEndElement(event.asEndElement());
        }
    }

    public void add(XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            add(reader.nextEvent());
        }
    }

    private static class RootWriterNode implements WriterNode {
        private WriterNode root;

        private RootWriterNode(WriterNode root) {
            this.root = root;
        }

        public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
            if (root.startsWith(startElement)) {
                root.init(context);
                context.consumeStartElement(startElement);
            } else {
                throw new RuntimeException("Unexpected start element: " + startElement.getName());
            }
        }

        public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
            context.pop();
        }

        public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {

        }

        public boolean startsWith(StartElement startElement) {
            return false;
        }

        public boolean isOptional() {
            return false;
        }

        public void init(WriterContext context) {
            context.push(new NodeState(this));
        }

        public boolean isTriggeredByEvent() {
            return true;
        }
    }
}
