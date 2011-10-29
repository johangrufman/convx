package org.convx.reader;

import org.convx.reader.ParserContext;
import org.convx.reader.elements.Element;
import org.convx.reader.elements.NodeElement;
import org.convx.schema.ConstantSchemaNode;
import org.convx.schema.SchemaNode;
import org.convx.schema.SequenceSchemaNode;
import org.junit.Test;

import java.io.StringReader;
import java.util.Stack;

import static org.junit.Assert.assertTrue;

/**
 * @author johan
 * @since 2011-06-26
 */
public class SequenceReaderNodeTest {
    @Test
    public void testLookAhead() throws Exception {

    }

    @Test
    public void testParse() throws Exception {
        Stack<Element> parserStack = new Stack<Element>();
        ParserContext context = new ParserContext(new StringReader("**"), 1);
        ReaderNode mock = new ConstantReaderNode("*");
        String elementName = "sequence";
        SequenceReaderNode sequenceNode = new SequenceReaderNode(mock, mock);
        sequenceNode.parse(parserStack, context, null);
        assertTrue(((NodeElement) parserStack.pop()).node().equals(mock));
        assertTrue(((NodeElement) parserStack.pop()).node().equals(mock));
    }
}
