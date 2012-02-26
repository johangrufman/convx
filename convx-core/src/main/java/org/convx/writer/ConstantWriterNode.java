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
public class ConstantWriterNode implements WriterNode {
    private String constant;

    public ConstantWriterNode(String constant) {
        this.constant = constant;
    }

    public ConstantWriterNode(Character character) {
        this.constant = String.valueOf(character);
    }

    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {

    }

    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {

    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {

    }

    public boolean startsWith(StartElement startElement) {
        return false;
    }

    public boolean isOptional() {
        return false;
    }

    public boolean isTriggeredByEvent() {
        return false;
    }

    public void init(WriterContext context) {
        context.write(constant);
    }
}
