package org.convx.schema;

import org.convx.reader.NamedReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.writer.NamedWriterNode;
import org.convx.writer.WriterNode;

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
    public ReaderNode asReaderNode() {
        return new NamedReaderNode(name, schemaNode.asReaderNode());
    }

    @Override
    public WriterNode asWriterNode() {
        return new NamedWriterNode(name, schemaNode.asWriterNode());
    }
}
