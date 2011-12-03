package org.convx.reader;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.convx.reader.elements.Element;
import org.convx.reader.elements.MarkupElement;
import org.convx.reader.elements.ParsingNodeState;

/**
 * @author johan
 * @since 2011-10-29
 */
public class DelimitedReaderNode implements ReaderNode {
    Set<Character> exceptions = new HashSet<Character>();

    private boolean trim;

    public DelimitedReaderNode(boolean trim, Set<Character> exceptions) {
        this.trim = trim;
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
        String content = consume(context);
        if (trim) {
            content = content.trim();
        }
        parserStack.push(MarkupElement.characters(content));
        return true;
    }

    public PrefixMatcher prefixes() {
        return PrefixMatcher.ALL;
    }

    public boolean isOptional() {
        return false;
    }

    public void remove(Character character) {
        exceptions.add(character);
    }
}
