package org.convx.writer;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 * @author johan
 * @since 2011-09-17
 */
public class ConstantWriterNode implements WriterNode {
    private String constant;

    public ConstantWriterNode(String constant) {
        this.constant = constant;
    }

    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {

    }

    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {

    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {

    }

    public boolean startsWith(StartElement startElement) {
        return false;
    }

    public boolean isOptional() {
        return false;
    }

    public boolean isTriggeredByEvent() {
        return false;
    }

    public void init(WriterContext context) {
        context.write(constant);
    }
}
