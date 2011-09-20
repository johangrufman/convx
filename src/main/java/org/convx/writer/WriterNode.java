package org.convx.writer;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
* @author johan
* @since 2011-09-17
*/
public interface WriterNode {
    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state);
    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state);
    public void consumeCharacters(Characters characters, WriterContext context, NodeState state);

    boolean startsWith(StartElement startElement);

    boolean isOptional();

    boolean isTriggeredByEvent();

    void init(WriterContext context);
}
