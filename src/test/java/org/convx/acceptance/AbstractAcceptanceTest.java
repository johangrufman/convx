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

import org.convx.schema.Schema;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import static junit.framework.Assert.assertEquals;

/**
 * @author johan
 * @since 2011-10-30
 */
@SuppressWarnings("deprecation")
public abstract class AbstractAcceptanceTest {
    private File flatFile;

    private File xmlFile;

    @Before
    public void setup() {
        URL url = this.getClass().getResource("/testcases/" + name() + "/flatfile.txt");
        flatFile = new File(url.getFile());
        url = this.getClass().getResource("/testcases/" + name() + "/xmlfile.xml");
        xmlFile = new File(url.getFile());

    }

    protected abstract String name();

    protected abstract Schema schema();

    @Test
    public void convertFlatFileToXml() throws IOException, XMLStreamException {
        XMLEventReader flatFileReader = schema().parser(new FileReader(flatFile));
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(new FileInputStream(xmlFile));
        assertEquals(name() + "-file converted to incorrectly to xml", serialize(xmlFileReader), serialize(flatFileReader));
    }

    private String serialize(XMLEventReader flatFileReader) throws XMLStreamException, IOException {
        Document flatFileDoc = TestUtil.buildDom(flatFileReader);
        StringWriter flatFileXml = new StringWriter();
        new org.apache.xml.serialize.XMLSerializer(flatFileXml, new org.apache.xml.serialize.OutputFormat((String) null, null, true)).serialize(flatFileDoc);
        return flatFileXml.toString();
    }

    @Test
    public void convertXmlToFlatFile() throws IOException, XMLStreamException {
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(new FileInputStream(xmlFile));
        StringWriter flatFileWriter = new StringWriter();
        schema().writer(flatFileWriter).add(xmlFileReader);
        flatFileWriter.flush();
        String actualFlatFileContent = flatFileWriter.toString();
        String expectedFlatFileContent = TestUtil.readFile(flatFile);
        assertEquals(name() + "-file converted to incorrectly from xml", expectedFlatFileContent, actualFlatFileContent);
    }
}
