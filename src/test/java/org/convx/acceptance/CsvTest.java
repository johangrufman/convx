package org.convx.acceptance;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

/**
 * @author johan
 * @since 2012-02-11
 */
public class CsvTest extends AcceptanceTest {
    public CsvTest() throws SAXException, JAXBException, TransformerException {
        super("csv");
    }

    @Test
    public void testParsingNonCanonicalFile1() throws Exception {
        convertFlatFilesToXmlAndValidate("nc_csv1.txt");
    }

    @Test
    public void testParsingNonCanonicalFile2() throws Exception {
        convertFlatFilesToXmlAndValidate("nc_csv2.txt");
    }
}
