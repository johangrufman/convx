package org.convx.schema;

import org.junit.Test;

import javax.xml.stream.events.*;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author johan
 * @since 2011-06-06
 */
public class FlatFileParserTest {
    @Test
    public void testParseOneDelimitedField() throws Exception {

        String elementName = "foo";
        Schema schema = new Schema(new NamedSchemaNode(elementName, new DelimitedSchemaNode('*')));
        FlatFileParser flatFileParser = schema.parser(new StringReader("bar*"));

        assertStartOfDocument(flatFileParser);
        assertElementWithContent(flatFileParser, elementName, "bar");
        assertEndOfDocument(flatFileParser);
    }

    @Test
    public void testParseOneConstantField() throws Exception {

        Schema schema = new Schema(new ConstantSchemaNode("*"));
        FlatFileParser flatFileParser = schema.parser(new StringReader("*"));

        assertStartOfDocument(flatFileParser);
        assertEndOfDocument(flatFileParser);
        assertTrue(flatFileParser.hasConsumedAllInput());
    }

    @Test
    public void testParseSequence() {
        String elementName = "foo";
        DelimitedSchemaNode delimitedNode = new DelimitedSchemaNode('*');
        ConstantSchemaNode constantNode = new ConstantSchemaNode("*");
        SchemaNode root = new NamedSchemaNode("baz", new SequenceSchemaNode(new NamedSchemaNode(elementName, delimitedNode), constantNode));
        Schema schema = new Schema(root);
        FlatFileParser flatFileParser = schema.parser(new StringReader("bar*"));

        assertStartOfDocument(flatFileParser);
        assertEquals("baz", ((StartElement) flatFileParser.nextEvent()).getName().getLocalPart());
        assertElementWithContent(flatFileParser, elementName, "bar");
        assertEquals("baz", ((EndElement) flatFileParser.nextEvent()).getName().getLocalPart());
        assertEndOfDocument(flatFileParser);
        assertTrue(flatFileParser.hasConsumedAllInput());
    }



    private void assertElementWithContent(FlatFileParser flatFileParser, String elementName, String elementContent) {
        assertEquals(elementName, ((StartElement)flatFileParser.nextEvent()).getName().getLocalPart());
        assertEquals(elementContent, ((Characters) flatFileParser.nextEvent()).getData());
        assertEquals(elementName, ((EndElement) flatFileParser.nextEvent()).getName().getLocalPart());
    }


    private void assertStartOfDocument(FlatFileParser flatFileParser) {
        assertTrue(flatFileParser.nextEvent() instanceof StartDocument);
    }

    private void assertEndOfDocument(FlatFileParser flatFileParser) {
        assertTrue(flatFileParser.nextEvent() instanceof EndDocument);
        assertFalse(flatFileParser.hasNext());
    }
}
