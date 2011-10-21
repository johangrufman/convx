package org.convx.writer;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

import org.apache.commons.lang3.StringUtils;

/**
 * @author johan
 * @since 2011-09-17
 */
public class FixedLengthWriterNode implements WriterNode {
    private int length;

    public FixedLengthWriterNode(int length) {
        this.length = length;
    }

    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
        throw new RuntimeException("Unexpected start element: " + startElement.getName());
    }

    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
        context.write(((FixedLengthWriterNodeState)state).stringOfLength(length));
        context.pop();
        context.consumeEndElement(endElement);
    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {
        ((FixedLengthWriterNodeState)state).append(characters.getData());
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
        context.push(new FixedLengthWriterNodeState(this));
    }

    static class FixedLengthWriterNodeState extends NodeState {
        private StringBuilder stringBuilder = new StringBuilder();

        private FixedLengthWriterNodeState(WriterNode writerNode) {
            super(writerNode);
        }

        public void append(CharSequence charSequence) {
            stringBuilder.append(charSequence);
        }

        public CharSequence stringOfLength(int length) {
            return StringUtils.rightPad(stringBuilder.toString(), length, ' ') ;
        }
    }
}
