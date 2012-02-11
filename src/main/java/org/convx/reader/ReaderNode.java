package org.convx.reader;

import com.ibm.icu.text.UnicodeSet;
import org.convx.reader.elements.Element;
import org.convx.reader.elements.ParsingNodeState;

import java.util.Stack;

/**
 * @author johan
 * @since 2011-10-29
 */
public interface ReaderNode {
    public int lookAhead();

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state);

    public PrefixMatcher prefixes();

    public boolean isOptional();

    void remove(UnicodeSet characters);
}
