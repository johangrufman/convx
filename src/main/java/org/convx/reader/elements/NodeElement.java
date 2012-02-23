/**
 *     Copyright (C) 2012 Johan Grufman
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.convx.reader.elements;

import org.convx.reader.ParserContext;
import org.convx.reader.ParsingException;
import org.convx.reader.ReaderNode;

import java.util.Stack;

/**
 * @author johan
 * @since 2011-06-27
 */
public class NodeElement implements Element {
    private ReaderNode readerNode;

    private ParsingNodeState state;

    public NodeElement(ReaderNode readerNode) {
        this(readerNode, null);
    }

    public NodeElement(ReaderNode readerNode, ParsingNodeState state) {
        this.readerNode = readerNode;
        this.state = state;
    }

    public void parse(Stack<Element> parserStack, ParserContext context) throws ParsingException {
        readerNode.parse(parserStack, context, state);
    }

    public ReaderNode node() {
        return readerNode;
    }

}
