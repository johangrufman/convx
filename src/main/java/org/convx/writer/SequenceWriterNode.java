package org.convx.writer;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author johan
 * @since 2011-09-17
 */
public class SequenceWriterNode implements WriterNode {
    private List<WriterNode> subWriterNodes = new ArrayList<WriterNode>();

    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
        SequenceNodeState sequenceNodeState = (SequenceNodeState) state;

        handleNodesNotTriggeredByEvents(context, sequenceNodeState);
        while (sequenceNodeState.index() < subWriterNodes.size()) {
            if (currentSubNode(sequenceNodeState).startsWith(startElement)) {
                currentSubNode(sequenceNodeState).init(context);
                context.consumeStartElement(startElement);
                return;
            } else if (!currentSubNode(sequenceNodeState).isOptional()) {
                throw new RuntimeException("Mandatory element");
            }
        }
        sequenceNodeState.increaseIndex();
        throw new RuntimeException("Unexpected start element: " + startElement.getName());
    }

    private WriterNode currentSubNode(SequenceNodeState sequenceNodeState) {
        return subWriterNodes.get(sequenceNodeState.index());
    }

    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
        SequenceNodeState sequenceNodeState = (SequenceNodeState) state;
        handleNodesNotTriggeredByEvents(context, sequenceNodeState);
        if (isSatisfied((SequenceNodeState) state)) {
            context.pop();
            context.consumeEndElement(endElement);
        } else {
            throw new RuntimeException("Unexpected end element: " + endElement.getName());
        }
    }

    private void handleNodesNotTriggeredByEvents(WriterContext context, SequenceNodeState sequenceNodeState) {
        while (sequenceNodeState.index() < subWriterNodes.size() && !currentSubNode(sequenceNodeState).isTriggeredByEvent()) {
            currentSubNode(sequenceNodeState).init(context);
            sequenceNodeState.increaseIndex();
        }
    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {
        SequenceNodeState sequenceNodeState = (SequenceNodeState) state;

        handleNodesNotTriggeredByEvents(context, sequenceNodeState);
        currentSubNode(sequenceNodeState).init(context);
        context.consumeCharacters(characters);
    }

    private boolean isSatisfied(SequenceNodeState state) {
        return areAllSubNodesOptional(state.index());
    }


    private boolean areAllSubNodesOptional(int fromIndex) {
        for (int i = fromIndex; i < subWriterNodes.size(); i++) {
            if (!subWriterNodes.get(i).isOptional()) {
                return false;
            }
        }
        return true;
    }

    public boolean startsWith(StartElement startElement) {
        for (WriterNode subWriterNode : subWriterNodes) {
            if (subWriterNode.startsWith(startElement)) {
                return true;
            } else if (!subWriterNode.isOptional()) {
                return false;
            }
        }
        return false;
    }

    public boolean isOptional() {
        return areAllSubNodesOptional(0);
    }

    public void addSubNode(WriterNode subWriterNode) {
        subWriterNodes.add(subWriterNode);
    }

    public void init(WriterContext context) {
        context.push(new SequenceNodeState(this));
    }

    public boolean isTriggeredByEvent() {
        return true;
    }

    static class SequenceNodeState extends NodeState {
        private int index = 0;

        SequenceNodeState(WriterNode writerNode) {
            super(writerNode);
        }

        public int index() {
            return index;
        }

        public void increaseIndex() {
            index++;
        }

        @Override
        public boolean moveOn() {
            increaseIndex();
            return index() >= ((SequenceWriterNode) writerNode).subWriterNodes.size();
        }
    }
}
