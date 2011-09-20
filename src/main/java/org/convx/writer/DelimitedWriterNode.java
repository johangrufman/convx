package org.convx.writer;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 * @author johan
 * @since 2011-09-17
 */
public class DelimitedWriterNode implements WriterNode {
    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
        throw new RuntimeException("Unexpected start element: " + startElement.getName());
    }

    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
        context.pop();
        context.consumeEndElement(endElement);
    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {
        context.write(characters.getData());
    }

    public boolean startsWith(StartElement startElement) {
        return false;
    }

    public boolean isOptional() {
        return false;
    }

    public boolean isTriggeredByEvent() {
        return true;
    }

    public void init(WriterContext context) {
        context.push(new NodeState(this));
    }
}
