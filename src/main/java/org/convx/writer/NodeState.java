package org.convx.writer;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 * @author johan
 * @since 2011-09-17
 */
public class NodeState {
    private WriterNode writerNode;

    public NodeState(WriterNode writerNode) {
        this.writerNode = writerNode;
    }

    public void consumeStartElement(StartElement startElement, WriterContext writerContext) {
        writerNode.consumeStartElement(startElement, writerContext, this);
    }

    public void consumeEndElement(EndElement endElement, WriterContext writerContext) {
        writerNode.consumeEndElement(endElement, writerContext, this);
    }

    public void consumeCharacters(Characters characters, WriterContext writerContext) {
        writerNode.consumeCharacters(characters, writerContext, this);
    }

    public void moveOn() {

    }
}
