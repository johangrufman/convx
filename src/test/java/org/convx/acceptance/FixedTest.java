package org.convx.acceptance;

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
}
