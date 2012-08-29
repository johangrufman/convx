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
package org.convx;

import org.convx.fsd.SchemaBuilder;
import org.convx.reader.FlatFileParser;
import org.convx.reader.Parser;
import org.convx.schema.SchemaImpl;
import org.convx.writer.FlatFileWriter;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import java.io.File;
import java.io.Reader;
import java.io.Writer;

/**
 * Represents a flat file schema. A schema instance is immutable and can be reused. The schema is thus thread safe, however the readers and writers it
 * returns are not.
 * @author johan
 * @since 2011-05-21
 */
public class Schema {
    /**
     * Builds a flat file schema from an xml representation.
     * @param schemaFile a file containing an xml representation.
     * @return an immutable flat file schema instance
     */
    public static Schema build(File schemaFile) {
        return new SchemaImpl(SchemaBuilder.build(schemaFile));
    }

    /**
     * Returns an {@link XMLEventReader} implementation that produces XML by parsing a flat file.
     * @param reader a reader returning flat file content
     * @return an {@link XMLEventReader} implementation
     */
    public XMLEventReader parser(Reader reader) {
        return new FlatFileParser(new Parser(this, reader));
    }

    /**
     * Returns an {@link XMLEventWriter} implementation that when fed with XML produces corresponding flat file content.
     * @param writer a writer to which flat file content is written
     * @return an {@link XMLEventWriter} implementation
     */
    public XMLEventWriter writer(Writer writer) {
        return new FlatFileWriter(this, writer);
    }
}
