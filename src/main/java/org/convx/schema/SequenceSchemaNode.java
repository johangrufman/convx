package org.convx.schema;

import org.convx.schema.elements.Element;
import org.convx.schema.elements.NodeElement;
import org.convx.schema.elements.ParsingNodeState;
import org.convx.writer.SequenceWriterNode;
import org.convx.writer.WriterNode;

import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @author johan
 * @since 2011-05-22
 */
public class SequenceSchemaNode extends SchemaNode {
    private LinkedList<SchemaNode> subSchemaNodes;
    private PrefixMatcher prefixMatcher;
    private boolean isOptional;

    public SequenceSchemaNode(SchemaNode... subSchemaNodes) {
        this.subSchemaNodes = new LinkedList<SchemaNode>(Arrays.asList(subSchemaNodes));
        initPrefixMatcher(subSchemaNodes);
        isOptional = true;
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            if (!subSchemaNode.isOptional()) {
                isOptional = false;
            }
        }
    }

    private void initPrefixMatcher(SchemaNode[] subSchemaNodes) {
        prefixMatcher = PrefixMatcher.NONE;
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            prefixMatcher = prefixMatcher.combine(subSchemaNode.prefixes());
            if (!subSchemaNode.isOptional()) {
                break;
            }
        }
    }


    @Override
    public int lookAhead() {
        int lookAhead = 0;
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            lookAhead = Math.max(lookAhead, subSchemaNode.lookAhead());
        }
        return 0;
    }

    @Override
    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        for (SchemaNode subSchemaNode : reverseOrder(subSchemaNodes)) {
            parserStack.push(new NodeElement(subSchemaNode));
        }
        return true;
    }


    @Override
    public PrefixMatcher prefixes() {
        return prefixMatcher;
    }


    @Override
    public boolean isOptional() {
        return isOptional;
    }

    @Override
    public WriterNode asWriterNode() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            sequenceWriterNode.addSubNode(subSchemaNode.asWriterNode());
        }
        return sequenceWriterNode;
    }

    private Iterable<SchemaNode> reverseOrder(final LinkedList<SchemaNode> schemaNodes) {
        return new Iterable<SchemaNode>() {
            public Iterator<SchemaNode> iterator() {
                return schemaNodes.descendingIterator();
            }
        };
    }
}
