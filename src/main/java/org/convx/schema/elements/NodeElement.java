package org.convx.schema.elements;

import org.convx.schema.SchemaNode;
import org.convx.schema.ParserContext;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

/**
 * @author johan
 * @since 2011-06-27
 */
public class NodeElement implements Element {
    private SchemaNode schemaNode;
    private ParsingNodeState state;

    public NodeElement(SchemaNode schemaNode) {
        this(schemaNode, null);
    }

    public NodeElement(SchemaNode schemaNode, ParsingNodeState state) {
        this.schemaNode = schemaNode;
        this.state = state;
    }

    public void parse(Stack<Element> parserStack, ParserContext context) {
        schemaNode.parse(parserStack, context, state);
    }

    public SchemaNode node() {
        return schemaNode;
    }

}
