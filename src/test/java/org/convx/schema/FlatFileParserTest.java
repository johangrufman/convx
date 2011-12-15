package org.convx.schema;

import java.io.StringReader;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndDocument;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

import org.convx.characters.CharacterSet;
import org.convx.reader.FlatFileParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author johan
 * @since 2011-06-06
 */
public class FlatFileParserTest {

    private CharacterSet allButAsterisk;

    @Before
    public void setup() {
        allButAsterisk = CharacterSet.complete().remove('*').build();
    }

    @Test
    public void testParseOneDelimitedField() throws Exception {

        String elementName = "foo";
        Schema schema = new Schema(new NamedSchemaNode(elementName, new FieldSchemaNode(true, allButAsterisk, null)));
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
        FieldSchemaNode delimitedNode = new FieldSchemaNode(true, allButAsterisk, null);
        ConstantSchemaNode constantNode = new ConstantSchemaNode("*");
        SchemaNode root = new NamedSchemaNode("baz", SequenceSchemaNode.sequence().add(new NamedSchemaNode(elementName, delimitedNode)).add(constantNode).build());
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
