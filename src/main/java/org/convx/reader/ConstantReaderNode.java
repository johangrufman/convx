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

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        if (context.nextCharacters().toString().startsWith(constant)) {
            context.advance(constant.length());
            return true;
        } else {
            throw new RuntimeException("Unexpected input: " + CharacterUtil.escapeCharacters(context.nextCharacters().toString()));
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
