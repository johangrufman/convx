package org.convx.schema;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.convx.reader.DelimitedReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.writer.DelimitedWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-06-06
 */
public class DelimitedSchemaNode extends SchemaNode {

    private Set<Character> exceptions = new HashSet<Character>();

    public DelimitedSchemaNode(Character... exceptions) {
        this.exceptions.addAll(Arrays.asList(exceptions));
    }

    @Override
    public ReaderNode asReaderNode() {
        return new DelimitedReaderNode(exceptions);
    }

    @Override
    public WriterNode asWriterNode() {
        return new DelimitedWriterNode();
    }
}
