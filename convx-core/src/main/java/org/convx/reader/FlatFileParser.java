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
package org.convx.reader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.NoSuchElementException;

/**
 * @author johan
 * @since 2011-05-21
 */
public class FlatFileParser implements XMLEventReader {
    private ParserStream parserStream;
    private XMLEvent currentEvent = null;

    public FlatFileParser(ParserStream parserStream) {
        this.parserStream = parserStream;
    }

    public XMLEvent nextEvent() throws ParsingException {
        initCurrent();
        XMLEvent returnEvent = currentEvent;
        if (parserStream.hasNext()) {
            currentEvent = parserStream.nextEvent();
        } else {
            currentEvent = null;
        }
        return returnEvent;
    }

    private void initCurrent() throws ParsingException {
        if (currentEvent == null) {
            currentEvent = parserStream.nextEvent();
        }
        if (currentEvent == null) {
            throw new NoSuchElementException();
        }
    }

    public boolean hasNext() {
        return currentEvent != null || parserStream.hasNext();
    }


    public XMLEvent peek() throws XMLStreamException {
        initCurrent();
        return currentEvent;
    }

    public String getElementText() throws XMLStreamException {
        initCurrent();
        if (currentEvent == null || !currentEvent.isStartElement()) {
            throw new XMLStreamException("Next event must be START_ELEMENT");
        }
        nextEvent();
        StringBuilder sb = new StringBuilder();
        while (hasNext()) {
            if (currentEvent.isCharacters()) {
                sb.append(currentEvent.asCharacters().getData());
            } else if (currentEvent.isEndElement()) {
                return sb.toString();
            } else if (currentEvent.isStartElement()) {
                throw new XMLStreamException("Unexpected start element event");
            }
            nextEvent();
        }
        throw new XMLStreamException("Unexpected end of document");
    }

    public XMLEvent nextTag() throws XMLStreamException {
        initCurrent();
        while (hasNext()) {
            XMLEvent event = nextEvent();
            if (event.isCharacters() && !event.asCharacters().isWhiteSpace()) {
                throw new XMLStreamException("Non whitespace characters found");
            }
            if (event.isStartElement() || event.isEndElement()) {
                return event;
            }
        }
        throw new XMLStreamException("Unexpected end of document");
    }

    public Object getProperty(String name) throws IllegalArgumentException {
        return XmlInputFactoryProperties.getProperty(name);
    }


    public void close() throws XMLStreamException {
        parserStream.close();
    }

    /**
     * *****************************************************************
     * java.util.Iterator methods
     * ******************************************************************
     */
    public Object next() {
        try {
            return nextEvent();
        } catch (ParsingException e) {
            throw new RuntimeException(e);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }


}
