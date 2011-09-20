package org.convx.schema;

import java.util.Stack;

import org.convx.schema.elements.Element;
import org.convx.schema.elements.MarkupElement;
import org.convx.schema.elements.ParsingNodeState;
import org.convx.writer.FixedLengthWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-09-17
 */
public class FixedLengthSchemaNode extends SchemaNode {
    private int length;
    public FixedLengthSchemaNode(int length) {
        this.length = length;
    }

    @Override
    public int lookAhead() {
        return 0;
    }

    @Override
    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        parserStack.push(MarkupElement.characters(consume(context)));
        return true;
    }

    private String consume(ParserContext context) {
        StringBuilder sb = new StringBuilder();
        while (context.hasMoreCharacters() && sb.length() < length) {
            CharSequence charSequence = sequenceOfMaxLength(context.nextCharacters(), length - sb.length());
            sb.append(charSequence);
            context.advance(charSequence.length());
        }
        return sb.toString().trim();
    }

    private CharSequence sequenceOfMaxLength(CharSequence sequence, int maxLength) {
        return sequence.subSequence(0, Math.min(maxLength, sequence.length()));
    }

    @Override
    public PrefixMatcher prefixes() {
        return PrefixMatcher.ALL;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public WriterNode asWriterNode() {
        return new FixedLengthWriterNode(length);
    }
}
