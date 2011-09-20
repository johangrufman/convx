package org.convx.writer;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
* @author johan
* @since 2011-09-17
*/
public class RepeatedWriterNode implements WriterNode {
    private WriterNode subWriterNode;
    private long minOccurs = 0;
    private long maxOccurs = Long.MAX_VALUE;

    public RepeatedWriterNode(WriterNode subWriterNode) {
        this.subWriterNode = subWriterNode;
    }

    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
        RepeatedNodeState repeatedNodeState = (RepeatedNodeState) state;
        if (subWriterNode.startsWith(startElement) && !repeatedNodeState.maxOccursReached()) {
            subWriterNode.init(context);
            context.consumeStartElement(startElement);
            repeatedNodeState.increaseCounter();
        } else {
            if (repeatedNodeState.isSatisfied()) {
                context.pop();
                context.consumeStartElement(startElement);
            } else {
                throw new RuntimeException("Unexpected start element");
            }
        }
    }


    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
        context.pop();
        context.consumeEndElement(endElement);
    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {

    }

    public boolean startsWith(StartElement startElement) {
        return subWriterNode.startsWith(startElement);
    }

    public boolean isOptional() {
        return minOccurs == 0;
    }

    public void init(WriterContext context) {
        context.push(new RepeatedNodeState(this));
    }

    public boolean isTriggeredByEvent() {
        return true;
    }

    static class RepeatedNodeState extends NodeState {
        private long counter = 0;
        private long minOccurs;
        private long maxOccurs;

        RepeatedNodeState(RepeatedWriterNode node) {
            super(node);
            this.minOccurs = node.minOccurs;
            this.maxOccurs = node.maxOccurs;
        }

        boolean maxOccursReached() {
            return counter >= maxOccurs;
        }

        boolean isSatisfied() {
            return counter >= minOccurs;
        }

        public void increaseCounter() {
            counter++;
        }

    }
}
