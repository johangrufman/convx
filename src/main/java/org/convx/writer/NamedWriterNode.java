package org.convx.writer;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 * @author johan
 * @since 2011-09-17
 */
public class NamedWriterNode implements WriterNode {
    private String name;
    private WriterNode node;

    public NamedWriterNode(String name, WriterNode node) {
        this.name = name;
        this.node = node;
    }

    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
        if (startElement.getName().getLocalPart().equals(name)) {
            node.init(context);
        } else {
            throw new RuntimeException("Unexpected start element: " + startElement.getName());
        }
    }

    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
        if (endElement.getName().getLocalPart().equals(name)) {
            context.pop();
        } else {
            throw new RuntimeException("Unexpected end element: " + endElement.getName());
        }
    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {

    }

    public boolean startsWith(StartElement startElement) {
        return startElement.getName().getLocalPart().equals(name);
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
