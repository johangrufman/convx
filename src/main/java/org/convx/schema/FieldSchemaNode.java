package org.convx.schema;

import org.convx.characters.CharacterSet;
import org.convx.reader.FieldReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.util.IndentationWriter;
import org.convx.writer.FieldWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-12-14
 */
public class FieldSchemaNode extends SchemaNode {

    private boolean trim;

    private CharacterSet characterSet;

    private Integer length;

    public FieldSchemaNode(boolean trim, CharacterSet characterSet, Integer length) {
        this.trim = trim;
        this.characterSet = characterSet;
        this.length = length;
    }

    @Override
    public ReaderNode asReaderNode() {
        return new FieldReaderNode(trim, characterSet, length);
    }

    @Override
    public WriterNode asWriterNode() {
        return new FieldWriterNode(length);
    }

    @Override
    protected void describe(IndentationWriter writer) {
        String lengthString = "";
        if (length != null) {
            lengthString = "(length = " + length + ") ";
        }
        writer.writeLine("FieldNode " + lengthString + characterSet);

    }
}
