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
public class NamedReaderNode implements ReaderNode {
    private ReaderNode readerNode;

    private String name;

    public NamedReaderNode(String name, ReaderNode readerNode) {
        this.name = name;
        this.readerNode = readerNode;
    }

    public int lookAhead() {
        return readerNode.lookAhead();
    }

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) throws ParsingException {
        Stack<Element> tmpParserStack = new Stack<Element>();
        boolean successfulParse = readerNode.parse(tmpParserStack, context, state);
        if (successfulParse) {
            parserStack.push(MarkupElement.endElement(name));
            parserStack.addAll(tmpParserStack);
            parserStack.push(MarkupElement.startElement(name));
        }
        return successfulParse;
    }

    public PrefixMatcher prefixes() {
        return readerNode.prefixes();
    }

    public boolean isOptional() {
        return readerNode.isOptional();
    }

    public void remove(UnicodeSet characters) {
        readerNode.remove(characters);
    }
}
