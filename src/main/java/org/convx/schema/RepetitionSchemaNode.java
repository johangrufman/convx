package org.convx.schema;

import org.convx.schema.elements.Element;
import org.convx.schema.elements.NodeElement;
import org.convx.schema.elements.ParsingNodeState;
import org.convx.writer.RepeatedWriterNode;
import org.convx.writer.WriterNode;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

/**
 * @author johan
 * @since 2011-05-22
 */
public class RepetitionSchemaNode extends SchemaNode {
    private SchemaNode schemaNode;
    private int minOccurs;
    private int maxOccurs;
    public static final int UNBOUNDED = Integer.MAX_VALUE;

    public RepetitionSchemaNode(SchemaNode schemaNode, int minOccurs, int maxOccurs) {
        this.schemaNode = schemaNode;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    @Override
    public int lookAhead() {
        return schemaNode.lookAhead();
    }

    @Override
    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        int numberOfPreviousRepeats = state == null ? 1 : ((RepetitionState)state).numberOfPreviousRepeats;
        if (context.hasMoreCharacters() && schemaNode.prefixes().matches(context.nextCharacters().toString())) {
            if (isUnbounded() || numberOfPreviousRepeats < maxOccurs) {
                parserStack.push(new NodeElement(this, new RepetitionState(numberOfPreviousRepeats + 1)));
            }
            parserStack.push(new NodeElement(schemaNode));
        }

        return true;
    }

    @Override
    public PrefixMatcher prefixes() {
        return schemaNode.prefixes();
    }

    @Override
    public boolean isOptional() {
        return minOccurs == 0;
    }

    @Override
    public WriterNode asWriterNode() {
        return new RepeatedWriterNode(schemaNode.asWriterNode(), minOccurs, maxOccurs);
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
