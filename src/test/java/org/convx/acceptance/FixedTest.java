package org.convx.acceptance;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

/**
 * @author johan
 * @since 2012-02-11
 */
public class FixedTest extends AcceptanceTest {
    public FixedTest() throws SAXException, JAXBException, TransformerException {
        super("fixed");
    }

    @Test
    public void testParsingNonCanonicalFile1() throws Exception {
        convertFlatFilesToXmlAndValidate("nc_fixed1.txt");
    }
}
