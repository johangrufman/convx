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
package org.convx.schema;

import org.convx.reader.FlatFileParser;
import org.convx.reader.ParserStream;
import org.convx.reader.ParsingException;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author johan
 * @since 2011-06-06
 */
public class FlatFileParserTest {

    @Before
    public void setup() {
    }

    @Test
    public void testBasicIteratorBehaviour() throws Exception {
        ParserStreamMock parserStream = parserStreamMock().addStartDocumentEvent().addEndDocumentEvent().build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        assertTrue(flatFileParser.hasNext());
        assertTrue(flatFileParser.next() instanceof StartDocument);
        assertTrue(flatFileParser.hasNext());
        assertTrue(flatFileParser.next() instanceof EndDocument);
        assertFalse(flatFileParser.hasNext());
    }

    @Test
    public void testBasicXmlIteratorBehaviour() throws Exception {
        ParserStreamMock parserStream = parserStreamMock().addStartDocumentEvent().addEndDocumentEvent().build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        assertTrue(flatFileParser.hasNext());
        assertTrue(flatFileParser.peek() instanceof StartDocument);
        assertTrue(flatFileParser.nextEvent() instanceof StartDocument);
        assertTrue(flatFileParser.hasNext());
        assertTrue(flatFileParser.peek() instanceof EndDocument);
        assertTrue(flatFileParser.nextEvent() instanceof EndDocument);
        assertFalse(flatFileParser.hasNext());
    }

    @Test
    public void testGetElementText() throws XMLStreamException {
        ParserStreamMock parserStream = parserStreamMock()
                .addStartElementEvent()
                .addCharacters("content")
                .addEndElementEvent()
                .build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        assertEquals("content", flatFileParser.getElementText());
        assertTrue(flatFileParser.peek() instanceof EndElement);
    }

    @Test
    public void testGetElementTextWithMultipleCharacterEvents() throws XMLStreamException {
        ParserStreamMock parserStream = parserStreamMock()
                .addStartElementEvent()
                .addCharacters("content")
                .addCharacters(" ")
                .addCharacters("content")
                .addEndElementEvent()
                .build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        assertEquals("content content", flatFileParser.getElementText());
        assertTrue(flatFileParser.peek() instanceof EndElement);
    }

    @Test(expected = XMLStreamException.class)
    public void testGetElementTextWithMixedContent() throws XMLStreamException {
        ParserStreamMock parserStream = parserStreamMock()
                .addStartElementEvent()
                .addCharacters("content")
                .addStartElementEvent()
                .addCharacters("content")
                .addEndElementEvent()
                .addCharacters("content")
                .addEndElementEvent()
                .build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        flatFileParser.getElementText();
    }

    @Test(expected = XMLStreamException.class)
    public void testGetElementTextWithoutEndElement() throws XMLStreamException {
        ParserStreamMock parserStream = parserStreamMock()
                .addStartElementEvent()
                .addCharacters("content")
                .build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        flatFileParser.getElementText();
    }

    @Test
    public void testNextTagWithEmptyElement() throws Exception {
        ParserStreamMock parserStream = parserStreamMock()
                .addStartElementEvent()
                .addEndElementEvent()
                .build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        assertTrue(flatFileParser.nextTag() instanceof StartElement);
        assertTrue(flatFileParser.nextTag() instanceof EndElement);
    }

    @Test
    public void testNextTagSkipsWhitespace() throws Exception {
        ParserStreamMock parserStream = parserStreamMock()
                .addWhitespace()
                .addWhitespace()
                .addStartElementEvent()
                .build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        assertTrue(flatFileParser.nextTag() instanceof StartElement);
    }

    @Test(expected = XMLStreamException.class)
    public void testNextTagThrowsExceptionOnNonWhitespace() throws Exception {
        ParserStreamMock parserStream = parserStreamMock()
                .addWhitespace()
                .addCharacters("content")
                .addStartElementEvent()
                .build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        flatFileParser.nextTag();
    }

    @Test(expected = XMLStreamException.class)
    public void testNextTagThrowsExceptionOnNoTagFound() throws Exception {
        ParserStreamMock parserStream = parserStreamMock()
                .addWhitespace()
                .build();
        FlatFileParser flatFileParser = new FlatFileParser(parserStream);
        flatFileParser.nextTag();
    }

    private static ParserStreamMockBuilder parserStreamMock() {
        return new ParserStreamMockBuilder();
    }

    private static class ParserStreamMock implements ParserStream {
        private Iterator<XMLEvent> iterator;

        private ParserStreamMock(List<XMLEvent> events) {
            this.iterator = events.iterator();
        }

        @Override
        public XMLEvent nextEvent() throws ParsingException {
            return iterator.next();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public void close() throws XMLStreamException {
        }
    }

    private static class ParserStreamMockBuilder {
        public static final QName ELEMENT_NAME = new QName("foo");
        private XMLEventFactory xmlEventFactory = XMLEventFactory.newFactory();
        private List<XMLEvent> events = new ArrayList<XMLEvent>();

        public ParserStreamMockBuilder addStartDocumentEvent() {
            events.add(xmlEventFactory.createStartDocument());
            return this;
        }

        public ParserStreamMockBuilder addEndDocumentEvent() {
            events.add(xmlEventFactory.createEndDocument());
            return this;
        }

        public ParserStreamMockBuilder addStartElementEvent() {
            events.add(xmlEventFactory.createStartElement(ELEMENT_NAME, null, null));
            return this;
        }

        public ParserStreamMockBuilder addEndElementEvent() {
            events.add(xmlEventFactory.createEndElement(ELEMENT_NAME, null));
            return this;
        }

        public ParserStreamMockBuilder addCharacters(String content) {
            events.add(xmlEventFactory.createCharacters(content));
            return this;
        }

        public ParserStreamMockBuilder addWhitespace() {
            events.add(xmlEventFactory.createIgnorableSpace(" "));
            return this;
        }

        public ParserStreamMock build() {
            return new ParserStreamMock(events);
        }
    }
}
