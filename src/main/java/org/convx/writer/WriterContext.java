package org.convx.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

import javax.xml.soap.Node;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 * @author johan
 * @since 2011-09-17
 */
public class WriterContext {
    private Stack<NodeState> stack = new Stack<NodeState>();
    private Writer writer;

    public WriterContext(Writer writer) {
        this.writer = writer;
    }

    public void push(NodeState nodeState) {
        stack.push(nodeState);
    }

    public void pop() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Parser stack is empty");
        }
        stack.pop();
        if (!stack.empty()) {
            currentNodeState().moveOn();
        }
    }

    public void consumeStartElement(StartElement startElement) {
        currentNodeState().consumeStartElement(startElement, this);
    }

    public void consumeEndElement(EndElement endElement) {
        currentNodeState().consumeEndElement(endElement, this);
    }

    public void consumeCharacters(Characters characters) {
        currentNodeState().consumeCharacters(characters, this);
    }

    public void write(CharSequence charSequence) {
        try {
            writer.append(charSequence);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private NodeState currentNodeState() {
        return stack.peek();
    }
}
