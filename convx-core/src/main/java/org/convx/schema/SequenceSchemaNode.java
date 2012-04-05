/**
 *     Copyright (C) 2012 Johan Grufman
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.convx.schema;

import com.ibm.icu.text.UnicodeSet;
import org.convx.reader.ConstantReaderNode;
import org.convx.reader.FieldReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.reader.SequenceReaderNode;
import org.convx.util.IndentationWriter;
import org.convx.writer.ConstantWriterNode;
import org.convx.writer.SequenceWriterNode;
import org.convx.writer.WriterNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.convx.format.IdentityFormat.IDENTITY_FORMAT;

/**
 * @author johan
 * @since 2011-05-22
 */
public class SequenceSchemaNode extends SchemaNode {
    private LinkedList<SchemaNode> subSchemaNodes;
    private Character separator = null;
    private boolean line;

    private SequenceSchemaNode(SchemaNode... schemaNodes) {
        this.subSchemaNodes = new LinkedList<SchemaNode>(Arrays.asList(schemaNodes));
    }

    public static Builder sequence(SchemaNode... schemaNodes) {
        return new Builder(false, schemaNodes);
    }

    public static Builder lineSequence(SchemaNode... schemaNodes) {
        return new Builder(true, schemaNodes);
    }

    @Override
    public ReaderNode asReaderNode() {
        List<ReaderNode> subNodes = new ArrayList<ReaderNode>();
        boolean first = true;
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            ReaderNode subReaderNode = subSchemaNode.asReaderNode();
            if (separator != null) {
                if (!first) {
                    subNodes.add(new ConstantReaderNode(separator));
                }
                subReaderNode.remove(new UnicodeSet("[" + separator + "]"));
            }
            subNodes.add(subReaderNode);
            first = false;
        }
        if (line) {
            subNodes.add(new FieldReaderNode(false, new UnicodeSet("[\\n\\r]"), null, null, IDENTITY_FORMAT));
        }
        return new SequenceReaderNode(subNodes.toArray(new ReaderNode[subNodes.size()]));
    }

    @Override
    public WriterNode asWriterNode() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        boolean first = true;
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            if (!first && separator != null) {
                sequenceWriterNode.addSubNode(new ConstantWriterNode(separator));
            }
            sequenceWriterNode.addSubNode(subSchemaNode.asWriterNode());
            first = false;
        }
        if (line) {
            sequenceWriterNode.addSubNode(new ConstantWriterNode("\n"));
        }
        return sequenceWriterNode;
    }

    @Override
    protected void describe(IndentationWriter writer) {
        writer.writeLine("SequenceNode  {");
        for (SchemaNode subSchemaNode : subSchemaNodes) {
            subSchemaNode.describe(writer);
        }
        writer.writeLine("}");
    }

    public static class Builder {
        private SequenceSchemaNode instance;

        private Builder(boolean line, SchemaNode... schemaNodes) {
            instance = new SequenceSchemaNode(schemaNodes);
            instance.line = line;
        }

        public Builder add(SchemaNode node) {
            instance.subSchemaNodes.add(node);
            return this;
        }

        public SequenceSchemaNode build() {
            return instance;
        }

        public SequenceSchemaNode setSeparatedBy(char separator) {
            instance.separator = separator;
            return instance;
        }

    }
}
