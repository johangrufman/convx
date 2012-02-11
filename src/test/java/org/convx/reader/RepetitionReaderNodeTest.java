package org.convx.reader;

import org.convx.reader.elements.Element;
import org.convx.reader.elements.NodeElement;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Stack;

import static org.junit.Assert.assertTrue;

/**
 * @author johan
 * @since 2011-06-27
 */
public class RepetitionReaderNodeTest {

    @Before
    public void setup() {

    }

    @Test
    public void testParseOfNodeRepeatedExactlyOnce() throws Exception {
        Stack<Element> parserStack = new Stack<Element>();
        ParserContext context = new ParserContext(new StringReader("*"), 1);
        ReaderNode mock = new ConstantReaderNode("*");

        RepetitionReaderNode repetitionNode = new RepetitionReaderNode(mock, 1, 1);
        repetitionNode.parse(parserStack, context, null);

        assertTrue(((NodeElement) parserStack.pop()).node().equals(mock));

    }

    @Test
    public void testParseOfNodeRepeatedTwice() throws Exception {
        Stack<Element> parserStack = new Stack<Element>();
        ParserContext context = new ParserContext(new StringReader("**"), 1);
        ReaderNode mock = new ConstantReaderNode("*");

        RepetitionReaderNode repetitionNode = new RepetitionReaderNode(mock, 1, 2);
        repetitionNode.parse(parserStack, context, null);

        assertTrue(((NodeElement) parserStack.pop()).node().equals(mock));
        NodeElement repetitionNodeWithState = (NodeElement) parserStack.pop();
        assertTrue(repetitionNodeWithState.node().equals(repetitionNode));

    }

    @Test
    public void testParseOfOptionalNode() throws Exception {
        Stack<Element> parserStack = new Stack<Element>();
        ParserContext context = new ParserContext(new StringReader("#"), 1);
        ReaderNode mock = new ConstantReaderNode("*");

        RepetitionReaderNode repetitionNode = new RepetitionReaderNode(mock, 0, 1);
        repetitionNode.parse(parserStack, context, null);

        assertTrue(parserStack.isEmpty());

    }

}
