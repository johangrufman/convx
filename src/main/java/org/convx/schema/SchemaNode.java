package org.convx.schema;


import org.convx.schema.elements.Element;
import org.convx.schema.elements.ParsingNodeState;
import org.convx.writer.WriterNode;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

/**
 * @author johan
 * @since 2011-05-22
 */
public abstract class SchemaNode {
    public abstract int lookAhead();

    public abstract boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state);
    public abstract PrefixMatcher prefixes();

    public abstract boolean isOptional();

    public abstract WriterNode asWriterNode();
}
