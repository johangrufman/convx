package org.convx.schema;


import org.convx.reader.ReaderNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-05-22
 */
public abstract class SchemaNode {

    public abstract ReaderNode asReaderNode();

    public abstract WriterNode asWriterNode();
}
