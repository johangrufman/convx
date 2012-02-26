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

import org.convx.reader.ReaderNode;
import org.convx.reader.RepetitionReaderNode;
import org.convx.util.IndentationWriter;
import org.convx.writer.RepeatedWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-05-22
 */
public class RepetitionSchemaNode extends SchemaNode {
    private SchemaNode schemaNode;

    private int minOccurs;

    private int maxOccurs;

    public static final int UNBOUNDED = Integer.MAX_VALUE;

    public RepetitionSchemaNode(SchemaNode schemaNode, int minOccurs, int maxOccurs) {
        this.schemaNode = schemaNode;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    @Override
    public ReaderNode asReaderNode() {
        return new RepetitionReaderNode(schemaNode.asReaderNode(), minOccurs, maxOccurs);

    }

    @Override
    public WriterNode asWriterNode() {
        return new RepeatedWriterNode(schemaNode.asWriterNode(), minOccurs, maxOccurs);

    }

    @Override
    protected void describe(IndentationWriter writer) {
        String maxOccursString = maxOccurs == UNBOUNDED ? "unbounded" : String.valueOf(maxOccurs);
        writer.writeLine("RepetitionNode [" + minOccurs + ", " + maxOccursString + "] {");
        schemaNode.describe(writer);
        writer.writeLine("}");
    }
}
