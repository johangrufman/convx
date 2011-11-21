package org.convx.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.convx.reader.ConstantReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.reader.SequenceReaderNode;
import org.convx.util.IndentationWriter;
import org.convx.writer.ConstantWriterNode;
import org.convx.writer.SequenceWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-05-22
 */
public class SequenceSchemaNode extends SchemaNode {
    private LinkedList<SchemaNode> subSchemaNodes;
    private Character separator = null;

    private SequenceSchemaNode(SchemaNode... schemaNodes) {
        this.subSchemaNodes = new LinkedList<SchemaNode>(Arrays.asList(schemaNodes));
    }

    public static Builder sequence(SchemaNode... schemaNodes) {
        return new Builder(schemaNodes);
    }

    @Override
    public ReaderNode asReaderNode() {
        List<ReaderNode> subNodes = new ArrayList<ReaderNode>();
        boolean first = true;
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            ReaderNode subReaderNode = subSchemaNode.asReaderNode();
            if (separator != null) {
                if (!first) {
                    subNodes.add(new ConstantReaderNode(separator));
                }
                subReaderNode.remove(separator);
            }
            subNodes.add(subReaderNode);
            first = false;
        }
        return new SequenceReaderNode(subNodes.toArray(new ReaderNode[subNodes.size()]));
    }

    @Override
    public WriterNode asWriterNode() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        boolean first = true;
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            if (!first && separator != null) {
                sequenceWriterNode.addSubNode(new ConstantWriterNode(separator));
            }
            sequenceWriterNode.addSubNode(subSchemaNode.asWriterNode());
            first = false;
        }
        return sequenceWriterNode;
    }

    @Override
    protected void describe(IndentationWriter writer) {
        writer.writeLine("SequenceNode  {");
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            subSchemaNode.describe(writer);
        }
        writer.writeLine("}");
    }

    public static class Builder {
        private SequenceSchemaNode instance;

        public Builder(SchemaNode... schemaNodes) {
            instance = new SequenceSchemaNode(schemaNodes);
        }

        public Builder add(SchemaNode node) {
            instance.subSchemaNodes.add(node);
            return this;
        }

        public SequenceSchemaNode build() {
            return instance;
        }

        public Builder separatedBy(Character separator) {
            instance.separator = separator;
            return this;
        }
    }
}
