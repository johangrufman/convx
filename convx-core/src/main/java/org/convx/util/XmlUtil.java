/*
    Copyright (C) 2012 Johan Grufman

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.convx.util;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.dom.DOMResult;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author johan
 * @since 2012-07-16
 */
public class XmlUtil {
//    public static String serialize(XMLEventReader reader) throws XMLStreamException {
//        try {
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            transformerFactory.setAttribute("indent-number", 4);
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            StreamResult result = new StreamResult(new StringWriter());
//            transformer.transform(new StAXSource(reader), result);
//            return result.getWriter().toString();
//        } catch (TransformerException e) {
//            if (e.getCause() instanceof XMLStreamException) {
//                throw (XMLStreamException)e.getCause();
//            }
//            throw new RuntimeException(e);
//        }
//    }

    private static Document buildDom(XMLEventReader eventReader) throws XMLStreamException {
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
        }
    }

    @SuppressWarnings("deprecation")
    public static String serialize(XMLEventReader flatFileReader) throws XMLStreamException, IOException {
        Document flatFileDoc = buildDom(flatFileReader);
        StringWriter flatFileXml = new StringWriter();
        new org.apache.xml.serialize.XMLSerializer(flatFileXml,
                new org.apache.xml.serialize.OutputFormat((String) null, null, true)).serialize(flatFileDoc);
        return flatFileXml.toString();
    }


}
