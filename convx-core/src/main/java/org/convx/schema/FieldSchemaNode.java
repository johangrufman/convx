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
import org.convx.format.Format;
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

    private Character quoteCharacter;

    private Format format;

    public FieldSchemaNode(boolean trim, UnicodeSet characterSet, Integer length, String defaultOutput, Character quoteCharacter, Format format) {
        this.trim = trim;
        this.characterSet = characterSet;
        this.length = length;
        this.defaultOutput = defaultOutput;
        this.quoteCharacter = quoteCharacter;
        this.format = format;
    }

    @Override
    public ReaderNode asReaderNode() {
        return new FieldReaderNode(trim, characterSet, length, quoteCharacter, format);
    }

    @Override
    public WriterNode asWriterNode() {
        return new FieldWriterNode(length, defaultOutput, characterSet, quoteCharacter, format);
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
