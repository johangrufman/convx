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
import org.apache.commons.lang3.StringEscapeUtils;
import org.convx.reader.elements.Element;
import org.convx.reader.elements.ParsingNodeState;
import org.convx.util.CharacterUtil;

import java.util.Stack;

/**
 * @author johan
 * @since 2011-10-29
 */
public class ConstantReaderNode implements ReaderNode {
    private String constant;

    public ConstantReaderNode(String constant) {
        this.constant = constant;
    }

    public ConstantReaderNode(Character character) {
        this.constant = String.valueOf(character);
    }

    public int lookAhead() {
        return constant.length();
    }

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) throws ParsingException {
        if (context.nextCharacters().toString().startsWith(constant)) {
            context.advance(constant.length());
            return true;
        } else {
            throw new ParsingException(errorMessage(context), context.getFlatFileLocation());
        }
    }

    private String errorMessage(ParserContext context) {
        String expectation = "Expecting constant \"" + constant + "\"";
        if (context.hasMoreCharacters()) {
            return "Unexpected input: " + CharacterUtil.escapeCharacters(context.nextCharacters().toString()) + ". " + expectation;
        } else {
            return "Unexpected end of file. " + expectation;
        }
    }

    public PrefixMatcher prefixes() {
        return new PrefixMatcher(constant);
    }

    public boolean isOptional() {
        return false;
    }

    public void remove(UnicodeSet characters) {
        for (char c : constant.toCharArray()) {
            if (characters.contains(c)) {
                throw new RuntimeException("Cannot remove character " + StringEscapeUtils.escapeJava(String.valueOf(c)) + " from constant " + constant);
            }
        }
    }
}
