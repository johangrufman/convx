package org.convx.reader;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.convx.reader.elements.Element;
import org.convx.reader.elements.MarkupElement;
import org.convx.reader.elements.ParsingNodeState;
import org.convx.writer.DelimitedWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-10-29
 */
public class DelimitedReaderNode implements ReaderNode {
    Set<Character> exceptions = new HashSet<Character>();

    public DelimitedReaderNode(char delimiter, Set<Character> exceptions) {
        this.exceptions.add(delimiter);
        this.exceptions.addAll(exceptions);
    }

    public String consume(ParserContext context) {
        StringBuilder sb = new StringBuilder();
        while (context.hasMoreCharacters() && !exceptions.contains(context.nextCharacter())) {
            sb.append(context.nextCharacter());
            context.advance(1);
        }
        return sb.toString();
    }


    public int lookAhead() {
        return 1;
    }

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        parserStack.push(MarkupElement.characters(consume(context)));
        return true;
    }

    public PrefixMatcher prefixes() {
        return PrefixMatcher.ALL;
    }

    public boolean isOptional() {
        return false;
    }

    public WriterNode asWriterNode() {
        return new DelimitedWriterNode();
    }
}
