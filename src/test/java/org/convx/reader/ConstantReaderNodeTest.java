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
