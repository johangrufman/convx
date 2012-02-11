package org.convx.acceptance;

import org.convx.fsd.SchemaBuilder;
import org.convx.schema.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

import static junit.framework.Assert.assertEquals;

/**
 * @author johan
 * @since 2011-10-30
 */

public abstract class AcceptanceTest {

    private static final String TEST_CASE_FOLDER = "/testcases/";

    private String name;

    private File flatFile;

    private File xmlFile;

    private File schemaFile;

    private javax.xml.validation.Schema schemaSchema;

    private javax.xml.validation.Schema xmlSchema;

    private Schema flatFileSchema;

    public AcceptanceTest(String name) throws SAXException, JAXBException, TransformerException {
        this.name = name;
        flatFile = testFile(name + ".txt");
        xmlFile = testFile(name + ".xml");
        schemaFile = testFile(name + ".fsd");
        try {
            flatFileSchema = SchemaBuilder.build(schemaFile);
        } catch (RuntimeException e) {
            System.out.println("Error parsing " + schemaFile);
            throw e;
        }

        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaSchema = schemaFactory.newSchema(new StreamSource(TestUtil.getTestResource("/xsd/fsd.xsd")));

        String schemaString = transformSchema();
        try {
            xmlSchema = schemaFactory.newSchema(new StreamSource(new StringReader(schemaString)));
        } catch (SAXException e) {
            System.out.println("Error parsing generated schema:");
            System.out.println(schemaString);
            throw new RuntimeException(e);
        }
    }

    @Test
    public void convertCanonicalFlatFileToXml() throws Exception {
        try {
            parseFlatFileAndValidateResult(flatFile);
        } catch (Exception e) {
            System.out.println("Error parsing flat file: " + flatFile);
            throw e;
        }
    }

    protected void parseFlatFileAndValidateResult(File flatFile) throws XMLStreamException, IOException {
        XMLEventReader flatFileReader = flatFileSchema.parser(new FileReader(flatFile));
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(new FileInputStream(xmlFile));
        assertEquals(flatFile.getName() + " converted to incorrectly to xml", TestUtil.serialize(xmlFileReader), TestUtil
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

    @Test
    public void generatedXmlSchemaShouldValidateXmlFile() throws IOException, SAXException, TransformerException {
        Validator validator = xmlSchema.newValidator();
        try {
            validator.validate(new StreamSource(new FileReader(xmlFile)));
        } catch (SAXException e) {
            Assert.fail(buildValidationErrorMessage(e.getMessage()));
        }
    }

    protected void convertFlatFilesToXmlAndValidate(String fileName) throws IOException, XMLStreamException {
        File testCaseFolder = new File(TestUtil.getTestResource(testCaseFolderName()));
        parseFlatFileAndValidateResult(new File(testCaseFolder, fileName));
    }

    private String buildValidationErrorMessage(String message) throws IOException, TransformerException {
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        sb.append("\n\n");
        sb.append(TestUtil.readFile(xmlFile));
        sb.append("\n\n");
        sb.append(transformSchema());
        return sb.toString();
    }

    @Override
    public String toString() {
        return name;
    }

    private String transformSchema() throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory
                .newTransformer(new StreamSource(TestUtil.getTestResource("/xsl/xsd-gen.xsl")));
        StringWriter xmlSchemaStringWriter = new StringWriter();
        transformer.transform(new StreamSource(schemaFile), new StreamResult(xmlSchemaStringWriter));
        return xmlSchemaStringWriter.toString();
    }

    private File testFile(String fileName) {
        return new File(TestUtil.getTestResource(testCaseFolderName() + fileName));
    }

    private String testCaseFolderName() {
        return TEST_CASE_FOLDER + name + "/";
    }
}
