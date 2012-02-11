package org.convx.acceptance;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

/**
 * @author johan
 * @since 2012-02-11
 */
public class MlrTest extends AcceptanceTest {
    public MlrTest() throws SAXException, JAXBException, TransformerException {
        super("mlr");
    }
}
