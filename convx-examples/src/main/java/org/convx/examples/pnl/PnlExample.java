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
package org.convx.examples.pnl;

import org.convx.Schema;
import org.convx.util.XmlUtil;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import static org.convx.examples.ResourceUtil.getResource;

/**
 * @author johan
 * @since 2012-07-08
 */
public class PnlExample {

    private final Schema pnlSchema = Schema.build(getResource("pnl/pnl.fsd"));

    public String parsePnl() throws IOException, XMLStreamException {
        XMLEventReader parser = pnlSchema.parser(new FileReader(getResource("pnl/pnl.txt")));
        return XmlUtil.serialize(parser);
    }

    public String writePnl() throws IOException, XMLStreamException {
        StringWriter stringWriter = new StringWriter();
        XMLEventWriter xmlEventWriter = pnlSchema.writer(stringWriter);
        FileInputStream xmlInputStream = new FileInputStream(getResource("pnl/pnl.xml"));
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(xmlInputStream);
        xmlEventWriter.add(xmlFileReader);
        xmlEventWriter.flush();
        stringWriter.flush();
        return stringWriter.toString();
    }

    public static void main(String args[]) throws XMLStreamException, IOException {
        PnlExample pnlExample = new PnlExample();
        System.out.println("Parsing pnl file gives this xml content:");
        System.out.println(pnlExample.parsePnl());
        System.out.println("Writing pnl file gives this flat file content:");
        System.out.println(pnlExample.writePnl());
    }
}
