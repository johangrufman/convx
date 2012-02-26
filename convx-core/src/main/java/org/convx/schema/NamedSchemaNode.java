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

import org.convx.reader.NamedReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.util.IndentationWriter;
import org.convx.writer.NamedWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-08-21
 */
public class NamedSchemaNode extends SchemaNode {
    private SchemaNode schemaNode;

    private String name;

    public NamedSchemaNode(String name, SchemaNode schemaNode) {
        this.name = name;
        this.schemaNode = schemaNode;
    }

    @Override
    public ReaderNode asReaderNode() {
        return new NamedReaderNode(name, schemaNode.asReaderNode());
    }

    @Override
    public WriterNode asWriterNode() {
        return new NamedWriterNode(name, schemaNode.asWriterNode());
    }

    @Override
    protected void describe(IndentationWriter writer) {
        writer.writeLine("NamedNode " + name + " {");
        schemaNode.describe(writer);
        writer.writeLine("}");
    }
}
