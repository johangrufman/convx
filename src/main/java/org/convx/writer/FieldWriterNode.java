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

import com.ibm.icu.text.UnicodeSet;
import org.apache.commons.lang3.StringUtils;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;

/**
 * @author johan
 * @since 2011-09-17
 */
public class FieldWriterNode implements WriterNode {
    private Integer length;

    private String defaultOutput;

    private UnicodeSet characterSet;
    private Character quoteCharacter;

    public FieldWriterNode(Integer length, String defaultOutput, UnicodeSet characterSet, Character quoteCharacter) {
        this.length = length;
        this.defaultOutput = defaultOutput;
        this.characterSet = characterSet;
        this.quoteCharacter = quoteCharacter;
    }

    public void consumeStartElement(StartElement startElement, WriterContext context, NodeState state) {
        throw new RuntimeException("Unexpected start element: " + startElement.getName());
    }

    public void consumeEndElement(EndElement endElement, WriterContext context, NodeState state) {
        String outputString = ((FieldWriterNodeState) state).buildString(length).toString();
        if (quoteCharacter != null) {
            for (char c : outputString.toCharArray()) {
                if (!characterSet.contains(c)) {
                    outputString = quoteCharacter + outputString + quoteCharacter;
                    break;
                }
            }
        }
        context.write(outputString);
        context.pop();
        context.consumeEndElement(endElement);
    }

    public void consumeCharacters(Characters characters, WriterContext context, NodeState state) {
        ((FieldWriterNodeState) state).append(characters.getData());
    }

    public boolean startsWith(StartElement startElement) {
        return false;
    }

    public boolean isOptional() {
        return false;
    }

    public boolean isTriggeredByEvent() {
        return defaultOutput == null;
    }

    public void init(WriterContext context) {
        if (!isTriggeredByEvent()) {
            context.write(defaultOutput);
        } else {
            context.push(new FieldWriterNodeState(this));
        }
    }

    static class FieldWriterNodeState extends NodeState {
        private StringBuilder stringBuilder = new StringBuilder();

        private FieldWriterNodeState(WriterNode writerNode) {
            super(writerNode);
        }

        public void append(CharSequence charSequence) {
            stringBuilder.append(charSequence);
        }

        public CharSequence buildString(Integer length) {
            if (length != null) {
                return StringUtils.rightPad(stringBuilder.toString(), length, ' ');
            } else {
                return stringBuilder.toString();
            }
        }
    }
}
