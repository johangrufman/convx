/*
   Copyright (C) 2012 Johan Grufman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
