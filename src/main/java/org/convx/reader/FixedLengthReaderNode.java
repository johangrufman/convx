package org.convx.reader;

import java.util.Stack;

import org.convx.reader.elements.Element;
import org.convx.reader.elements.MarkupElement;
import org.convx.reader.elements.ParsingNodeState;
import org.convx.writer.FixedLengthWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-10-29
 */
public class FixedLengthReaderNode implements ReaderNode {
    private int length;

    public FixedLengthReaderNode(int length) {
        this.length = length;
    }

    public int lookAhead() {
        return 0;
    }

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

    public PrefixMatcher prefixes() {
        return PrefixMatcher.ALL;
    }

    public boolean isOptional() {
        return false;
    }

    public void remove(Character character) {
        throw new UnsupportedOperationException();
    }
}
