package org.convx.schema;

import org.convx.schema.elements.Element;
import org.convx.schema.elements.MarkupElement;
import org.convx.schema.elements.ParsingNodeState;
import org.convx.writer.NamedWriterNode;
import org.convx.writer.WriterNode;

import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

/**
 * @author johan
 * @since 2011-08-21
 */
public class NamedSchemaNode extends SchemaNode {
    private SchemaNode schemaNode;
    private String name;

    public NamedSchemaNode(String name, SchemaNode schemaNode) {
        this.name = name;
        this.schemaNode = schemaNode;
    }

    @Override
    public int lookAhead() {
        return schemaNode.lookAhead();
    }

    @Override
    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        Stack<Element> tmpParserStack = new Stack<Element>();
        boolean successfulParse = schemaNode.parse(tmpParserStack, context, state);
        if (successfulParse) {
            parserStack.push(MarkupElement.endElement(name));
            parserStack.addAll(tmpParserStack);
            parserStack.push(MarkupElement.startElement(name));
        }
        return successfulParse;
    }

    @Override
    public PrefixMatcher prefixes() {
        return schemaNode.prefixes();
    }

    @Override
    public boolean isOptional() {
        return schemaNode.isOptional();
    }

    @Override
    public WriterNode asWriterNode() {
        return new NamedWriterNode(name, schemaNode.asWriterNode());
    }
}
