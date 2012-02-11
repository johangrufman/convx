package org.convx.schema;

import com.ibm.icu.text.UnicodeSet;
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

    private UnicodeSet characterSet;

    private Integer length;

    private String defaultOutput;

    public FieldSchemaNode(boolean trim, UnicodeSet characterSet, Integer length, String defaultOutput) {
        this.trim = trim;
        this.characterSet = characterSet;
        this.length = length;
        this.defaultOutput = defaultOutput;
    }

    @Override
    public ReaderNode asReaderNode() {
        return new FieldReaderNode(trim, characterSet, length);
    }

    @Override
    public WriterNode asWriterNode() {
        return new FieldWriterNode(length, defaultOutput);
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
