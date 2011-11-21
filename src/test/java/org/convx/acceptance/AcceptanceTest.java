package org.convx.acceptance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.convx.fsd.SchemaBuilder;
import org.convx.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

import static junit.framework.Assert.assertEquals;

/**
 * @author johan
 * @since 2011-10-30
 */

@RunWith(Parameterized.class)
public class AcceptanceTest {

    private static final String TEST_CASE_FOLDER = "/testcases/";

    private String name;

    private File flatFile;

    private File xmlFile;

    private File schemaFile;

    javax.xml.validation.Schema schemaSchema;

    private Schema flatFileSchema;

    public AcceptanceTest(String name) throws SAXException, JAXBException {
        this.name = name;
        flatFile = testFile(name, ".txt");
        xmlFile = testFile(name, ".xml");
        schemaFile = testFile(name, ".fsd");
        flatFileSchema = SchemaBuilder.build(schemaFile);

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaSchema = factory.newSchema(new StreamSource(TestUtil.getTestResource("/xsd/fsd.xsd")));
    }

    private File testFile(String name, String fileSuffix) {
        return new File(TestUtil.getTestResource(TEST_CASE_FOLDER + name + "/" + name + fileSuffix));
    }


    @Parameterized.Parameters
    public static Collection<Object[]> testCaseNames() {
        File testCaseFolder = new File(TestUtil.getTestResource(TEST_CASE_FOLDER));
        Collection<Object[]> testCaseNames = new ArrayList<Object[]>();
        for (String subFolder : testCaseFolder.list()) {
            testCaseNames.add(new Object[] {subFolder});
        }

        return testCaseNames;
    }


    @Test
    public void convertFlatFileToXml() throws IOException, XMLStreamException {
        XMLEventReader flatFileReader = flatFileSchema.parser(new FileReader(flatFile));
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(new FileInputStream(xmlFile));
        assertEquals(name + "-file converted to incorrectly to xml", TestUtil.serialize(xmlFileReader), TestUtil
                .serialize(flatFileReader));
    }

    @Test
    public void convertXmlToFlatFile() throws IOException, XMLStreamException {
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(new FileInputStream(xmlFile));
        StringWriter flatFileWriter = new StringWriter();
        flatFileSchema.writer(flatFileWriter).add(xmlFileReader);
        flatFileWriter.flush();
        String actualFlatFileContent = flatFileWriter.toString();
        String expectedFlatFileContent = TestUtil.readFile(flatFile);
        assertEquals(name + "-file converted to incorrectly from xml", expectedFlatFileContent, actualFlatFileContent);
    }

    @Test
    public void flatFileSchemaShouldValidateAgainstXmlSchema() throws IOException, SAXException {
        Validator validator = schemaSchema.newValidator();
        validator.validate(new StreamSource(new FileReader(schemaFile)));
    }

    @Override
    public String toString() {
        return name;
    }
}
