package org.convx.schema;

import java.io.Reader;
import java.io.Writer;

import org.convx.writer.FlatFileWriter;

/**
 * @author johan
 * @since 2011-05-21
 */
public class Schema {
    private SchemaNode root;

    public Schema(SchemaNode root) {
        this.root = root;
    }

    public FlatFileParser parser(Reader reader) {
        return new FlatFileParser(this, reader);
    }

    public FlatFileWriter writer(Writer writer) {
        return new FlatFileWriter(this, writer);
    }

    public int lookAhead() {
        return root.lookAhead();
    }

    public SchemaNode root() {
        return root;
    }
}
