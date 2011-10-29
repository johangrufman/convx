package org.convx.schema;

import org.convx.reader.ConstantReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.writer.ConstantWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-06-06
 */
public class ConstantSchemaNode extends SchemaNode {
    private String constant;

    public ConstantSchemaNode(String constant) {
        this.constant = constant;
    }

    @Override
    public ReaderNode asReaderNode() {
        return new ConstantReaderNode(constant);
    }

    @Override
    public WriterNode asWriterNode() {
        return new ConstantWriterNode(constant);
    }
}
