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

import org.convx.reader.FlatFileParser;
import org.convx.reader.Parser;
import org.convx.writer.FlatFileWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * @author johan
 * @since 2011-05-21
 */
public class Schema {
    private SchemaNode root;

    public Schema(SchemaNode root) {
        this.root = root;
    }

    public XMLEventReader parser(Reader reader) {
        return new FlatFileParser(new Parser(this, reader));
    }

    public XMLEventWriter writer(Writer writer) {
        return new FlatFileWriter(this, writer);
    }

    public SchemaNode root() {
        return root;
    }
}
