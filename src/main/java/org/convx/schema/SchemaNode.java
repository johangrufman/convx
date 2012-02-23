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
import org.convx.util.IndentationWriter;
import org.convx.writer.WriterNode;

import java.io.StringWriter;

/**
 * @author johan
 * @since 2011-05-22
 */
public abstract class SchemaNode {

    public abstract ReaderNode asReaderNode();

    public abstract WriterNode asWriterNode();

    protected abstract void describe(IndentationWriter writer);

    @Override
    public String toString() {
        StringWriter stringWriter = new StringWriter();
        describe(new IndentationWriter(stringWriter));
        return stringWriter.toString();
    }
}
