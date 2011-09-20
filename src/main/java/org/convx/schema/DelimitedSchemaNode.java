package org.convx.schema;

import com.sun.xml.internal.stream.events.CharacterEvent;
import org.convx.schema.elements.*;
import org.convx.writer.DelimitedWriterNode;
import org.convx.writer.WriterNode;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @author johan
 * @since 2011-06-06
 */
public class DelimitedSchemaNode extends SchemaNode {
    Set<Character> exceptions = new HashSet<Character>();

    public DelimitedSchemaNode(char delimiter, char... exceptions) {
        this.exceptions.add(delimiter);
        for (char c : exceptions) {
            this.exceptions.add(c);
        }
    }

    public String consume(ParserContext context) {
        StringBuilder sb = new StringBuilder();
        while (context.hasMoreCharacters() && !exceptions.contains(context.nextCharacter())) {
            sb.append(context.nextCharacter());
            context.advance(1);
        }
        return sb.toString();
    }

    @Override
    public int lookAhead() {
        return 1;
    }

    @Override
    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        parserStack.push(MarkupElement.characters(consume(context)));
        return true;
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
        return new DelimitedWriterNode();
    }
}
