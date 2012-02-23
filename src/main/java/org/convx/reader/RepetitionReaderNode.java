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

import java.util.Stack;

/**
 * @author johan
 * @since 2011-10-29
 */
public class RepetitionReaderNode implements ReaderNode {
    private ReaderNode readerNode;

    private int minOccurs;

    private int maxOccurs;

    public static final int UNBOUNDED = Integer.MAX_VALUE;

    public RepetitionReaderNode(ReaderNode readerNode, int minOccurs, int maxOccurs) {
        this.readerNode = readerNode;
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    public int lookAhead() {
        return readerNode.lookAhead();
    }

    public boolean parse(Stack<Element> parserStack, ParserContext context, ParsingNodeState state) {
        int numberOfPreviousRepeats = state == null ? 1 : ((RepetitionState) state).numberOfPreviousRepeats;
        if (context.hasMoreCharacters() && readerNode.prefixes().matches(context.nextCharacters().toString())) {
            if (isUnbounded() || numberOfPreviousRepeats < maxOccurs) {
                parserStack.push(new NodeElement(this, new RepetitionState(numberOfPreviousRepeats + 1)));
            }
            parserStack.push(new NodeElement(readerNode));
        }

        return true;
    }

    public PrefixMatcher prefixes() {
        return readerNode.prefixes();
    }

    public boolean isOptional() {
        return minOccurs == 0;
    }

    public void remove(UnicodeSet characters) {
        readerNode.remove(characters);
    }

    private boolean isUnbounded() {
        return maxOccurs == UNBOUNDED;
    }

    private class RepetitionState implements ParsingNodeState {
        private int numberOfPreviousRepeats;

        private RepetitionState(int numberOfPreviousRepeats) {
            this.numberOfPreviousRepeats = numberOfPreviousRepeats;
        }
    }
}
