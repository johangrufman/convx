package org.convx.schema;


import org.convx.reader.ReaderNode;
import org.convx.util.IndentationWriter;
import org.convx.writer.WriterNode;

import java.io.StringWriter;

/**
 * @author johan
 * @since 2011-05-22
 */
public abstract class SchemaNode {

    public abstract ReaderNode asReaderNode();

    public abstract WriterNode asWriterNode();

    protected abstract void describe(IndentationWriter writer);

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        describe(new IndentationWriter(stringWriter));
        return stringWriter.toString();
    }
}
