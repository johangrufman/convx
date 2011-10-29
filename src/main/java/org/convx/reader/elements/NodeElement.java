package org.convx.reader.elements;

import java.util.Stack;

import org.convx.reader.ParserContext;
import org.convx.reader.ReaderNode;

/**
 * @author johan
 * @since 2011-06-27
 */
public class NodeElement implements Element {
    private ReaderNode readerNode;

    private ParsingNodeState state;

    public NodeElement(ReaderNode readerNode) {
        this(readerNode, null);
    }

    public NodeElement(ReaderNode readerNode, ParsingNodeState state) {
        this.readerNode = readerNode;
        this.state = state;
    }

    public void parse(Stack<Element> parserStack, ParserContext context) {
        readerNode.parse(parserStack, context, state);
    }

    public ReaderNode node() {
        return readerNode;
    }

}
