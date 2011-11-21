package org.convx.schema;

import org.convx.reader.FixedLengthReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.util.IndentationWriter;
import org.convx.writer.FixedLengthWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-09-17
 */
public class FixedLengthSchemaNode extends SchemaNode {
    private int length;

    public FixedLengthSchemaNode(int length) {
        this.length = length;
    }

    @Override
    public ReaderNode asReaderNode() {
        return new FixedLengthReaderNode(length);
    }

    @Override
    public WriterNode asWriterNode() {
        return new FixedLengthWriterNode(length);
    }

    @Override
    protected void describe(IndentationWriter writer) {
        writer.writeLine("FixedLengthNode: " + length);
    }
}
