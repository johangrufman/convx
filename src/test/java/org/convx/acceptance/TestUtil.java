package org.convx.acceptance;

import org.custommonkey.xmlunit.XMLAssert;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.dom.DOMResult;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author johan
 * @since 2011-05-21
 */
public class TestUtil {


    public static Document buildDom(XMLEventReader eventReader) throws XMLStreamException {
        try {
            XMLOutputFactory xof = XMLOutputFactory.newInstance();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.newDocument();
            XMLEventWriter writer = xof.createXMLEventWriter(new DOMResult(doc));
            writer.add(eventReader);
            writer.close();
            return doc;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }


    public static String readFile(File file) throws IOException {
        FileReader reader = new FileReader(file);
        StringBuilder flatFileContent = new StringBuilder();
        int ch;
        while ((ch = reader.read()) >= 0) {
            flatFileContent.append((char)ch);
        }
        return flatFileContent.toString();
    }
}
