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
package org.convx.reader;

import org.convx.reader.elements.Element;
import org.convx.reader.elements.MarkupElement;
import org.convx.reader.elements.NodeElement;
import org.convx.schema.Schema;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.Reader;
import java.util.Stack;

/**
 * @author johan
 * @since 2012-03-31
 */
public class Parser implements ParserStream {
    private ParserContext context;

    private Stack<Element> parserStack = new Stack<Element>();

    private ReaderNode rootReaderNode;

    public Parser(Schema schema, Reader reader) {
        rootReaderNode = schema.root().asReaderNode();
        context = new ParserContext(reader, rootReaderNode.lookAhead());
        init();
    }

    private void init() {
        parserStack.push(MarkupElement.endDocument());
        parserStack.push(new NodeElement(rootReaderNode));
        parserStack.push(MarkupElement.startDocument());
    }

    @Override
    public XMLEvent nextEvent() throws ParsingException {
        if (parserStack.empty()) {
            return null;
        }
        Element element = parserStack.pop();
        if (element instanceof MarkupElement) {
            return ((MarkupElement) element).event();
        }
        ((NodeElement) element).parse(parserStack, context);
        return nextEvent();
    }

    @Override
    public boolean hasNext() {
        return !parserStack.isEmpty();
    }

    @Override
    public void close() throws XMLStreamException {
        try {
            context.close();
        } catch (IOException e) {
            throw new XMLStreamException(e);
        }
    }
}
