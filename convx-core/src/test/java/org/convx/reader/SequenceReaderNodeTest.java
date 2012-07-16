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
        SequenceReaderNode sequenceNode = new SequenceReaderNode(mock, mock);
        sequenceNode.parse(parserStack, context, null);
        assertTrue(((NodeElement) parserStack.pop()).node().equals(mock));
        assertTrue(((NodeElement) parserStack.pop()).node().equals(mock));
    }
}
