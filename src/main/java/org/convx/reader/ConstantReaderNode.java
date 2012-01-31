package org.convx.reader;

import java.util.Stack;

import org.convx.reader.elements.Element;
import org.convx.reader.elements.ParsingNodeState;
import org.convx.util.CharacterUtil;

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
//            return false;
        }
    }

    public PrefixMatcher prefixes() {
        return new PrefixMatcher(constant);
    }

    public boolean isOptional() {
        return false;
    }

    public void remove(Character character) {
        if (constant.indexOf(character) >= 0) {
            throw new RuntimeException("Cannot remove character " + character + " from constant " + constant);
        }
    }
}
