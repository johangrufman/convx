/**
 *     Copyright (C) 2012 Johan Grufman
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.convx.writer;

import org.junit.Before;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.events.StartElement;
import java.io.StringWriter;

/**
 * @author johan
 * @since 2011-10-21
 */
public class AbstractWriterTest {
    private XMLEventFactory xmlEventFactory;
    private StringWriter outputWriter;
    protected WriterContext context;

    @Before
    public void setup() {
        outputWriter = new StringWriter();
        context = new WriterContext(outputWriter);
        xmlEventFactory = XMLEventFactory.newFactory();
    }

    protected void pushStartElement(String localName) {
        context.consumeStartElement(createStartElement(localName));
    }

    protected StartElement createStartElement(String localName) {
        return xmlEventFactory.createStartElement(new QName(localName), null, null);
    }

    protected void pushEndElement(String localName) {
        context.consumeEndElement(xmlEventFactory.createEndElement(new QName(localName), null));
    }

    protected void pushEmptyElement(String localName) {
        pushStartElement(localName);
        pushEndElement(localName);
    }

    protected void pushCharacters(String characters) {
        context.consumeCharacters(xmlEventFactory.createCharacters(characters));
    }

    protected String output() {
        return outputWriter.toString();
    }
}
