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
package org.convx.reader;

import com.ibm.icu.text.UnicodeSet;
import org.convx.reader.elements.Element;
import org.convx.reader.elements.NodeElement;
import org.convx.reader.elements.ParsingNodeState;
import org.convx.util.CollectionUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author johan
 * @since 2011-10-29
 */
public class SequenceReaderNode implements ReaderNode {
    private LinkedList<ReaderNode> subReaderNodes;

    private PrefixMatcher prefixMatcher;

    private boolean isOptional;

    public SequenceReaderNode(ReaderNode... subReaderNodes) {
        this.subReaderNodes = new LinkedList<ReaderNode>(Arrays.asList(subReaderNodes));
        initPrefixMatcher(subReaderNodes);
        isOptional = true;
        for (ReaderNode subReaderNode : subReaderNodes) {
            if (!subReaderNode.isOptional()) {
                isOptional = false;
            }
        }
    }

    private void initPrefixMatcher(ReaderNode[] subReaderNodes) {
        prefixMatcher = PrefixMatcher.NONE;
        for (ReaderNode subReaderNode : subReaderNodes) {
            prefixMatcher = prefixMatcher.combine(subReaderNode.prefixes());
            if (!subReaderNode.isOptional()) {
                break;
            }
        }
    }


    public int lookAhead() {
        int lookAhead = 0;
        for (ReaderNode subReaderNode : subReaderNodes) {
            lookAhead = Math.max(lookAhead, subReaderNode.lookAhead());
        }
        return 0;
    }

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        for (ReaderNode subSchemaNode : CollectionUtil.reverseOrder(subReaderNodes)) {
            parserStack.push(new NodeElement(subSchemaNode));
        }
        return true;
    }


    public PrefixMatcher prefixes() {
        return prefixMatcher;
    }


    public boolean isOptional() {
        return isOptional;
    }

    public void remove(UnicodeSet characters) {
        for (ReaderNode subReaderNode : subReaderNodes) {
            subReaderNode.remove(characters);
        }
    }

}
