package org.convx.schema;

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
    private char delimiter;

    private Set<Character> exceptions = new HashSet<Character>();

    public DelimitedSchemaNode(char delimiter, char... exceptions) {
        this.delimiter = delimiter;
        this.exceptions.add(delimiter);
        for (char c : exceptions) {
            this.exceptions.add(c);
        }
    }

    @Override
    public ReaderNode asReaderNode() {
        return new DelimitedReaderNode(delimiter, exceptions);
    }

    @Override
    public WriterNode asWriterNode() {
        return new DelimitedWriterNode();
    }
}
