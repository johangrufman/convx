package org.convx.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.convx.reader.DelimitedReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.util.CharacterUtil;
import org.convx.util.IndentationWriter;
import org.convx.writer.DelimitedWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-06-06
 */
public class DelimitedSchemaNode extends SchemaNode {

    private Set<Character> exceptions = new HashSet<Character>();

    private boolean trim;

    public DelimitedSchemaNode(boolean trim, Character... exceptions) {
        this.trim = trim;
        this.exceptions.addAll(Arrays.asList(exceptions));
    }

    @Override
    public ReaderNode asReaderNode() {
        return new DelimitedReaderNode(trim, exceptions);
    }

    @Override
    public WriterNode asWriterNode() {
        return new DelimitedWriterNode();
    }

    @Override
    protected void describe(IndentationWriter writer) {
        List<String> exceptionStrings = new ArrayList<String>();
        for (Character exception : exceptions) {
            exceptionStrings.add(CharacterUtil.escapeCharacter(exception));
        }
        writer.writeLine("DelimitedNode. " + StringUtils.join(exceptionStrings, ", "));
    }
}
