package org.convx.schema;

import org.convx.schema.elements.Element;
import org.convx.schema.elements.ParsingNodeState;
import org.convx.writer.ConstantWriterNode;
import org.convx.writer.WriterNode;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

/**
 * @author johan
 * @since 2011-06-06
 */
public class ConstantSchemaNode extends SchemaNode {
    public static final ConstantSchemaNode EOL = new ConstantSchemaNode(System.getProperty("line.separator"));
    String constant;

    public ConstantSchemaNode(String constant) {
        this.constant = constant;
    }

    @Override
    public int lookAhead() {
        return constant.length();
    }

    @Override
    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        if (context.nextCharacters().toString().startsWith(constant)) {
            context.advance(constant.length());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public PrefixMatcher prefixes() {
        return new PrefixMatcher(constant);
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public WriterNode asWriterNode() {
        return new ConstantWriterNode(constant);
    }
}
