package org.convx.reader;

import java.util.Stack;

import org.convx.reader.elements.Element;
import org.convx.reader.elements.NodeElement;
import org.convx.reader.elements.ParsingNodeState;

/**
 * @author johan
 * @since 2011-10-29
 */
public class RepetitionReaderNode implements ReaderNode {
    private ReaderNode readerNode;

    private int minOccurs;

    private int maxOccurs;

    public static final int UNBOUNDED = Integer.MAX_VALUE;

    public RepetitionReaderNode(ReaderNode readerNode, int minOccurs, int maxOccurs) {
        this.readerNode = readerNode;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    public int lookAhead() {
        return readerNode.lookAhead();
    }

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        int numberOfPreviousRepeats = state == null ? 1 : ((RepetitionState) state).numberOfPreviousRepeats;
        if (context.hasMoreCharacters() && readerNode.prefixes().matches(context.nextCharacters().toString())) {
            if (isUnbounded() || numberOfPreviousRepeats < maxOccurs) {
                parserStack.push(new NodeElement(this, new RepetitionState(numberOfPreviousRepeats + 1)));
            }
            parserStack.push(new NodeElement(readerNode));
        }

        return true;
    }

    public PrefixMatcher prefixes() {
        return readerNode.prefixes();
    }

    public boolean isOptional() {
        return minOccurs == 0;
    }


    private boolean isUnbounded() {
        return maxOccurs == UNBOUNDED;
    }

    private class RepetitionState implements ParsingNodeState {
        private int numberOfPreviousRepeats;

        private RepetitionState(int numberOfPreviousRepeats) {
            this.numberOfPreviousRepeats = numberOfPreviousRepeats;
        }
    }
}
