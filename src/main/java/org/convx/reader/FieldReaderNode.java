package org.convx.reader;

import com.ibm.icu.text.UnicodeSet;
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

    private Integer length;

    private boolean trim;

    public FieldReaderNode(boolean trim, UnicodeSet characterSet, Integer length, Character quoteCharacter) {
        this.trim = trim;
        this.characterSet = characterSet;
        this.quoteCharacter = quoteCharacter;
        if (length != null) {
            this.length = length;
        } else {
            this.length = Integer.MAX_VALUE;
        }
    }

    public String consume(ParserContext context) {
        StringBuilder sb = new StringBuilder();
        boolean quoted = false;
        while (context.hasMoreCharacters() && sb.length() < length) {
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
        return sb.toString();
    }


    public int lookAhead() {
        return 1;
    }

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        String content = consume(context);
        if (trim) {
            content = content.trim();
        }
        parserStack.push(MarkupElement.characters(content));
        return true;
    }

    public PrefixMatcher prefixes() {
        return PrefixMatcher.ALL;
    }

    public boolean isOptional() {
        return false;
    }

    public void remove(UnicodeSet characters) {
        characterSet.removeAll(characters);
    }
}
