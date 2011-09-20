package org.convx.acceptance;


import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.convx.schema.*;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * Acceptance test for CSV-files.
 * @author johan
 * @since 2011-05-21
 */
public class CsvTest {
    private org.convx.schema.Schema csvSchema;
    private File flatFile;
    private File xmlFile;

    @Before
    public void setup() {
        URL url = this.getClass().getResource("/testcases/csv/flatfile.txt");
        flatFile = new File(url.getFile());
        url = this.getClass().getResource("/testcases/csv/xmlfile.xml");
        xmlFile = new File(url.getFile());

        SchemaNode firstName = new NamedSchemaNode("firstName", new DelimitedSchemaNode(',', '\n', '\r'));
        SchemaNode lastName = new NamedSchemaNode("lastName", new DelimitedSchemaNode(',', '\n', '\r'));
        SchemaNode age = new NamedSchemaNode("age", new DelimitedSchemaNode(',', '\n', '\r'));
        SchemaNode comma = new ConstantSchemaNode(",");
        SchemaNode eol = new ConstantSchemaNode("\n");
        SchemaNode person = new NamedSchemaNode("person", new SequenceSchemaNode(firstName, comma, lastName, comma, age, eol));
        SchemaNode repeatedPerson = new RepetitionSchemaNode(person, 1, RepetitionSchemaNode.UNBOUNDED);
        SchemaNode root = new NamedSchemaNode("persons", new SequenceSchemaNode(repeatedPerson));
        csvSchema = new Schema(root);
    }

    @Test
    public void convertFlatFileToXml() throws IOException, XMLStreamException {
        XMLEventReader flatFileReader = csvSchema.parser(new FileReader(flatFile));
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(new FileInputStream(xmlFile));
        Document xmlFileDoc = TestUtil.buildDom(xmlFileReader);
        Document flatFileDoc = TestUtil.buildDom(flatFileReader);
//        new XMLSerializer(System.out, new OutputFormat((String)null, null, true)).serialize(flatFileDoc);
//        new XMLSerializer(System.out, new OutputFormat((String)null, null, true)).serialize(xmlFileDoc);
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(xmlFileDoc, flatFileDoc);
    }
    
    @Test
    public void convertXmlToFlatFile() throws IOException, XMLStreamException {
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(new FileInputStream(xmlFile));
        StringWriter flatFileWriter = new StringWriter();
        csvSchema.writer(flatFileWriter).add(xmlFileReader);
        flatFileWriter.flush();
        String actualFlatFileContent = flatFileWriter.toString();
        String expectedFlatFileContent = TestUtil.readFile(flatFile);
        assertEquals(expectedFlatFileContent, actualFlatFileContent);
    }

}
