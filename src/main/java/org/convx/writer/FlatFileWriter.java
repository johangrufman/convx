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

import org.convx.schema.Schema;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.Writer;

/**
 * @author johan
 * @since 2011-05-22
 */
public class FlatFileWriter implements XMLEventWriter {

    private WriterContext writerContext;

    public FlatFileWriter(Schema schema, Writer writer) {
        this.writerContext = new WriterContext(writer);
        new RootWriterNode(schema.root().asWriterNode()).init(writerContext);
    }

    public void add(XMLEvent event) throws XMLStreamException {
        if (event.isStartElement()) {
            writerContext.consumeStartElement(event.asStartElement());
        } else if (event.isEndElement()) {
            writerContext.consumeEndElement(event.asEndElement());
        } else if (event.isCharacters() && !event.asCharacters().isWhiteSpace()) {
            writerContext.consumeCharacters(event.asCharacters());
        }
    }


    public void flush() throws XMLStreamException {

    }

    public void close() throws XMLStreamException {

    }

    public void add(XMLEventReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            add(reader.nextEvent());
        }
    }

    public String getPrefix(String uri) throws XMLStreamException {
        return null;
    }

    public void setPrefix(String prefix, String uri) throws XMLStreamException {

    }

    public void setDefaultNamespace(String uri) throws XMLStreamException {

    }

    public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {

    }

    public NamespaceContext getNamespaceContext() {
        return null;
    }

    private static class RootWriterNode implements WriterNode {
        private WriterNode root;

        private RootWriterNode(WriterNode root) {
            this.root = root;
        }

        public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
            if (root.startsWith(startElement)) {
                root.init(context);
                context.consumeStartElement(startElement);
            } else {
                throw new RuntimeException("Unexpected start element: " + startElement.getName());
            }
        }

        public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
            context.pop();
        }

        public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {

        }

        public boolean startsWith(StartElement startElement) {
            return false;
        }

        public boolean isOptional() {
            return false;
        }

        public void init(WriterContext context) {
            context.push(new NodeState(this));
        }

        public boolean isTriggeredByEvent() {
            return true;
        }
    }
}
