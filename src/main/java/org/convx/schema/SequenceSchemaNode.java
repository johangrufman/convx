package org.convx.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.convx.reader.PrefixMatcher;
import org.convx.reader.ReaderNode;
import org.convx.reader.SequenceReaderNode;
import org.convx.writer.SequenceWriterNode;
import org.convx.writer.WriterNode;

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
    }

    @Override
    public ReaderNode asReaderNode() {
        List<ReaderNode> subNodes = new ArrayList<ReaderNode>();
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            subNodes.add(subSchemaNode.asReaderNode());
        }
        return new SequenceReaderNode(subNodes.toArray(new ReaderNode[subNodes.size()]));
    }

    @Override
    public WriterNode asWriterNode() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            sequenceWriterNode.addSubNode(subSchemaNode.asWriterNode());
        }
        return sequenceWriterNode;
    }
}
