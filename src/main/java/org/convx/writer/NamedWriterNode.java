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

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 * @author johan
 * @since 2011-09-17
 */
public class NamedWriterNode implements WriterNode {
    private String name;

    private WriterNode node;

    public NamedWriterNode(String name, WriterNode node) {
        this.name = name;
        this.node = node;
    }

    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
        if (startElement.getName().getLocalPart().equals(name)) {
            node.init(context);
        } else {
            throw new RuntimeException("Unexpected start element: " + startElement.getName());
        }
    }

    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
        if (endElement.getName().getLocalPart().equals(name)) {
            context.pop();
        } else {
            throw new RuntimeException("Unexpected end element: " + endElement.getName());
        }
    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {

    }

    public boolean startsWith(StartElement startElement) {
        return startElement.getName().getLocalPart().equals(name);
    }

    public boolean isOptional() {
        return false;
    }

    public boolean isTriggeredByEvent() {
        return true;
    }

    public void init(WriterContext context) {
        context.push(new NodeState(this));
    }
}
