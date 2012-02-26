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

import org.convx.reader.ConstantReaderNode;
import org.convx.reader.ReaderNode;
import org.convx.util.CharacterUtil;
import org.convx.util.IndentationWriter;
import org.convx.writer.ConstantWriterNode;
import org.convx.writer.WriterNode;

/**
 * @author johan
 * @since 2011-06-06
 */
public class ConstantSchemaNode extends SchemaNode {
    private String constant;

    public ConstantSchemaNode(String constant) {
        this.constant = constant;
    }

    @Override
    public ReaderNode asReaderNode() {
        return new ConstantReaderNode(constant);
    }

    @Override
    public WriterNode asWriterNode() {
        return new ConstantWriterNode(constant);
    }

    @Override
    protected void describe(IndentationWriter writer) {
        writer.writeLine("ConstantNode: " + CharacterUtil.escapeCharacters(constant));
    }
}
