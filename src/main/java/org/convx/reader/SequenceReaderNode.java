package org.convx.reader;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import org.convx.reader.elements.Element;
import org.convx.reader.elements.NodeElement;
import org.convx.reader.elements.ParsingNodeState;

/**
 * @author johan
 * @since 2011-10-29
 */
public class SequenceReaderNode implements ReaderNode {
    private LinkedList<ReaderNode> subReaderNodes;

    private PrefixMatcher prefixMatcher;

    private boolean isOptional;

    public SequenceReaderNode(ReaderNode... subReaderNodes) {
        this.subReaderNodes = new LinkedList<ReaderNode>(Arrays.asList(subReaderNodes));
        initPrefixMatcher(subReaderNodes);
        isOptional = true;
        for (ReaderNode subReaderNode : subReaderNodes) {
            if (!subReaderNode.isOptional()) {
                isOptional = false;
            }
        }
    }

    private void initPrefixMatcher(ReaderNode[] subReaderNodes) {
        prefixMatcher = PrefixMatcher.NONE;
        for (ReaderNode subReaderNode : subReaderNodes) {
            prefixMatcher = prefixMatcher.combine(subReaderNode.prefixes());
            if (!subReaderNode.isOptional()) {
                break;
            }
        }
    }


    public int lookAhead() {
        int lookAhead = 0;
        for (ReaderNode subReaderNode : subReaderNodes) {
            lookAhead = Math.max(lookAhead, subReaderNode.lookAhead());
        }
        return 0;
    }

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        for (ReaderNode subSchemaNode : reverseOrder(subReaderNodes)) {
            parserStack.push(new NodeElement(subSchemaNode));
        }
        return true;
    }


    public PrefixMatcher prefixes() {
        return prefixMatcher;
    }


    public boolean isOptional() {
        return isOptional;
    }

    public void remove(Character character) {
        for (ReaderNode subReaderNode : subReaderNodes) {
            subReaderNode.remove(character);
        }
    }

    private Iterable<ReaderNode> reverseOrder(final LinkedList<ReaderNode> readerNodes) {
        return new Iterable<ReaderNode>() {
            public Iterator<ReaderNode> iterator() {
                return readerNodes.descendingIterator();
            }
        };
    }
}
