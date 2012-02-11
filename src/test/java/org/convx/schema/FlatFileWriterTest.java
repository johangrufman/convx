package org.convx.schema;

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
        Schema schema = new Schema(root);
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
