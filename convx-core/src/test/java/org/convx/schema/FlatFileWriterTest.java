/*
   Copyright (C) 2012 Johan Grufman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.convx.schema;

import org.convx.Schema;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import static junit.framework.Assert.assertEquals;

/**
 * @author johan
 * @since 2011-09-03
 */
public class FlatFileWriterTest {

    @Test
    public void writeDocumentWithSingleElement() throws XMLStreamException {
        SchemaNode root = new NamedSchemaNode("foo", new ConstantSchemaNode("bar"));
        Schema schema = new SchemaImpl(root);
        String xmlInput = "<foo>bar</foo>";
        String expectedFlatFileContent = "bar";
        verifyXmlToFlatFileConversion(schema, xmlInput, expectedFlatFileContent);
    }

    private void verifyXmlToFlatFileConversion(Schema schema, String xmlInput, String expectedFlatFileContent)
            throws XMLStreamException {
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(new ByteArrayInputStream(xmlInput.getBytes()));
        StringWriter flatFileWriter = new StringWriter();
        schema.writer(flatFileWriter).add(xmlFileReader);
        flatFileWriter.flush();
        String actualFlatFileContent = flatFileWriter.toString();
        assertEquals(expectedFlatFileContent, actualFlatFileContent);
    }
}
