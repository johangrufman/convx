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

import com.ibm.icu.text.UnicodeSet;
import org.convx.format.Format;
import org.convx.reader.elements.Element;
import org.convx.reader.elements.MarkupElement;
import org.convx.reader.elements.ParsingNodeState;

import java.util.Stack;

/**
 * @author johan
 * @since 2011-10-29
 */
public class FieldReaderNode implements ReaderNode {
    private UnicodeSet characterSet;

    private Character quoteCharacter;

    private Format format;

    private int minLength;
    private int maxLength;

    private boolean trim;

    public FieldReaderNode(boolean trim, UnicodeSet characterSet, Integer length, Character quoteCharacter, Format format) {
        this.trim = trim;
        this.characterSet = characterSet;
        this.quoteCharacter = quoteCharacter;
        this.format = format;
        if (length != null) {
            this.minLength = length;
            this.maxLength = length;
        } else {
            this.minLength = 0;
            this.maxLength = Integer.MAX_VALUE;
        }
    }

    public String consume(ParserContext context) throws ParsingException {
        StringBuilder sb = new StringBuilder();
        boolean quoted = false;
        while (context.hasMoreCharacters() && sb.length() < maxLength) {
            if (quoted) {
                if (context.nextCharacter() == quoteCharacter) {
                    quoted = false;
                } else {
                    sb.append(context.nextCharacter());
                }
            } else {
                if (quoteCharacter != null && context.nextCharacter() == quoteCharacter) {
                    quoted = true;
                } else if (characterSet.contains(context.nextCharacter())) {
                    sb.append(context.nextCharacter());
                } else {
                    break;
                }

            }
            context.advance(1);
        }
        if (sb.length() < minLength) {
            throw new ParsingException("Could not reach minimum length of field: " + minLength, context.getFlatFileLocation());
        }
        return sb.toString();
    }


    public int lookAhead() {
        return 1;
    }

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) throws ParsingException {
        String content = consume(context);
        if (trim) {
            content = content.trim();
        }
        content = format.parse(content);
        parserStack.push(MarkupElement.characters(content));
        return true;
    }

    public PrefixMatcher prefixes() {
        return new PrefixMatcher(characterSet);
    }

    public boolean isOptional() {
        return false;
    }

    public void remove(UnicodeSet characters) {
        characterSet.removeAll(characters);
    }
}
