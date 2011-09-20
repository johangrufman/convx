package org.convx.schema;

import org.convx.schema.elements.Element;
import org.convx.schema.elements.NodeElement;
import org.junit.Test;

import java.io.StringReader;
import java.util.Stack;

import static org.junit.Assert.assertTrue;

/**
 * @author johan
 * @since 2011-06-26
 */
public class SequenceNodeTest {
    @Test
    public void testLookAhead() throws Exception {

    }

    @Test
    public void testParse() throws Exception {
        Stack<Element> parserStack = new Stack<Element>();
        ParserContext context = new ParserContext(new StringReader("**"), 1);
        SchemaNode mock = new ConstantSchemaNode("*");
        String elementName = "sequence";
        SequenceSchemaNode sequenceNode = new SequenceSchemaNode(mock, mock);
        sequenceNode.parse(parserStack, context, null);
        assertTrue(((NodeElement) parserStack.pop()).node().equals(mock));
        assertTrue(((NodeElement) parserStack.pop()).node().equals(mock));
    }
}
