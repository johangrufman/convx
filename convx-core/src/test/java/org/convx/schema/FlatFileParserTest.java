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
import org.convx.reader.FlatFileParser;
import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * @author johan
 * @since 2011-06-06
 */
public class FlatFileParserTest {

    private UnicodeSet allButAsterisk;

    @Before
    public void setup() {
        allButAsterisk = new UnicodeSet("[^*]");
    }

    @Test
    public void testParseOneDelimitedField() throws Exception {

        String elementName = "foo";
        Schema schema = new Schema(new NamedSchemaNode(elementName, new FieldSchemaNode(true, allButAsterisk, null, null, null)));
        XMLEventReader flatFileParser = schema.parser(new StringReader("bar*"));

        assertStartOfDocument(flatFileParser);
        assertElementWithContent(flatFileParser, elementName, "bar");
        assertEndOfDocument(flatFileParser);
    }

    @Test
    public void testParseOneConstantField() throws Exception {

        Schema schema = new Schema(new ConstantSchemaNode("*"));
        XMLEventReader flatFileParser = schema.parser(new StringReader("*"));

        assertStartOfDocument(flatFileParser);
        assertEndOfDocument(flatFileParser);
        assertTrue(((FlatFileParser) flatFileParser).hasConsumedAllInput());
    }

    @Test
    public void testParseSequence() throws XMLStreamException {
        String elementName = "foo";
        FieldSchemaNode delimitedNode = new FieldSchemaNode(true, allButAsterisk, null, null, null);
        ConstantSchemaNode constantNode = new ConstantSchemaNode("*");
        SchemaNode root = new NamedSchemaNode("baz", SequenceSchemaNode.sequence().add(new NamedSchemaNode(elementName, delimitedNode)).add(constantNode).build());
        Schema schema = new Schema(root);
        XMLEventReader flatFileParser = schema.parser(new StringReader("bar*"));

        assertStartOfDocument(flatFileParser);
        assertEquals("baz", ((StartElement) flatFileParser.nextEvent()).getName().getLocalPart());
        assertElementWithContent(flatFileParser, elementName, "bar");
        assertEquals("baz", ((EndElement) flatFileParser.nextEvent()).getName().getLocalPart());
        assertEndOfDocument(flatFileParser);
        assertTrue(((FlatFileParser) flatFileParser).hasConsumedAllInput());
    }


    private void assertElementWithContent(XMLEventReader flatFileParser, String elementName, String elementContent) throws XMLStreamException {
        assertEquals(elementName, ((StartElement) flatFileParser.nextEvent()).getName().getLocalPart());
        assertEquals(elementContent, ((Characters) flatFileParser.nextEvent()).getData());
        assertEquals(elementName, ((EndElement) flatFileParser.nextEvent()).getName().getLocalPart());
    }


    private void assertStartOfDocument(XMLEventReader flatFileParser) throws XMLStreamException {
        assertTrue(flatFileParser.nextEvent() instanceof StartDocument);
    }

    private void assertEndOfDocument(XMLEventReader flatFileParser) throws XMLStreamException {
        assertTrue(flatFileParser.nextEvent() instanceof EndDocument);
        assertFalse(flatFileParser.hasNext());
    }
}
