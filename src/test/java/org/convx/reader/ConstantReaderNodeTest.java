package org.convx.reader;

import org.convx.reader.elements.Element;
import org.junit.Test;

import java.io.StringReader;
import java.util.Stack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author johan
 * @since 2011-06-06
 */
public class ConstantReaderNodeTest {
    @Test
    public void testSuccessfulParse() throws Exception {
        ConstantReaderNode xyz = new ConstantReaderNode("xyz");
        ParserContext context = new ParserContext(new StringReader("xyz"), 3);
        assertTrue(xyz.parse(new Stack<Element>(), context, null));
        assertFalse(context.hasMoreCharacters());
    }
}
