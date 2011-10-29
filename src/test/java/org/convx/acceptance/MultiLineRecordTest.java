package org.convx.acceptance;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.convx.schema.ConstantSchemaNode;
import org.convx.schema.DelimitedSchemaNode;
import org.convx.schema.NamedSchemaNode;
import org.convx.schema.RepetitionSchemaNode;
import org.convx.schema.Schema;
import org.convx.schema.SchemaNode;
import org.convx.schema.SequenceSchemaNode;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import static junit.framework.Assert.assertEquals;


/**
 * Acceptance test for CSV-files.
 * @author johan
 * @since 2011-05-21
 */
public class MultiLineRecordTest {
    private Schema fixedLengthSchema;
    private File flatFile;
    private File xmlFile;

    @Before
    public void setup() {
        URL url = this.getClass().getResource("/testcases/mlr/flatfile.txt");
        flatFile = new File(url.getFile());
        url = this.getClass().getResource("/testcases/mlr/xmlfile.xml");
        xmlFile = new File(url.getFile());

        SchemaNode eol = new ConstantSchemaNode("\n");
        DelimitedSchemaNode delimitedField = new DelimitedSchemaNode('\n', '\r');
        SchemaNode firstName = new NamedSchemaNode("firstName", SequenceSchemaNode.sequence().add(
                new ConstantSchemaNode("firstName:")).add(
                delimitedField).add(
                eol).build());
        SchemaNode lastName = new NamedSchemaNode("lastName", SequenceSchemaNode.sequence().add(new ConstantSchemaNode("lastName:")).add(
                delimitedField).add(eol).build());
        SchemaNode age = new NamedSchemaNode("age", SequenceSchemaNode.sequence().add(new ConstantSchemaNode("age:")).add(delimitedField).add(eol).build());
        SchemaNode personHeader = new ConstantSchemaNode("[person]\n");
        SchemaNode person = new NamedSchemaNode("person", SequenceSchemaNode.sequence().add(personHeader).add(firstName).add(lastName).add(age).build());
        SchemaNode repeatedPerson = new RepetitionSchemaNode(person, 1, RepetitionSchemaNode.UNBOUNDED);
        SchemaNode root = new NamedSchemaNode("persons", SequenceSchemaNode.sequence().add(repeatedPerson).build());
        fixedLengthSchema = new Schema(root);
    }

    @Test
    public void convertFlatFileToXml() throws IOException, XMLStreamException {
        XMLEventReader flatFileReader = fixedLengthSchema.parser(new FileReader(flatFile));
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
        fixedLengthSchema.writer(flatFileWriter).add(xmlFileReader);
        flatFileWriter.flush();
        String actualFlatFileContent = flatFileWriter.toString();
        String expectedFlatFileContent = TestUtil.readFile(flatFile);
        assertEquals(expectedFlatFileContent, actualFlatFileContent);
    }

}
