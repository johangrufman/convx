package org.convx.writer;

import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 * @author johan
 * @since 2011-09-17
 */
public class FieldWriterNode implements WriterNode {
    private Integer length;

    private String defaultOutput;

    public FieldWriterNode(Integer length, String defaultOutput) {
        this.length = length;
        this.defaultOutput = defaultOutput;
    }

    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
        throw new RuntimeException("Unexpected start element: " + startElement.getName());
    }

    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
        context.write(((FieldWriterNodeState) state).buildString(length));
        context.pop();
        context.consumeEndElement(endElement);
    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {
        ((FieldWriterNodeState) state).append(characters.getData());
    }

    public boolean startsWith(StartElement startElement) {
        return false;
    }

    public boolean isOptional() {
        return false;
    }

    public boolean isTriggeredByEvent() {
        return defaultOutput == null;
    }

    public void init(WriterContext context) {
        if (!isTriggeredByEvent()) {
            context.write(defaultOutput);
        } else {
            context.push(new FieldWriterNodeState(this));
        }
    }

    static class FieldWriterNodeState extends NodeState {
        private StringBuilder stringBuilder = new StringBuilder();

        private FieldWriterNodeState(WriterNode writerNode) {
            super(writerNode);
        }

        public void append(CharSequence charSequence) {
            stringBuilder.append(charSequence);
        }

        public CharSequence buildString(Integer length) {
            if (length != null) {
                return StringUtils.rightPad(stringBuilder.toString(), length, ' ');
            } else {
                return stringBuilder.toString();
            }
        }
    }
}
