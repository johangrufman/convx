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
package org.convx.writer;

import org.junit.Test;

import static org.convx.format.IdentityFormat.IDENTITY_FORMAT;
import static org.junit.Assert.assertEquals;


/**
 * @author johan
 * @since 2011-10-21
 */
public class NamedWriterNodeTest extends AbstractWriterTest {

    @Test
    public void testSimpleNamedConstantNode() {
        NamedWriterNode foo = new NamedWriterNode("foo", new ConstantWriterNode("bar"));
        foo.init(context);
        pushEmptyElement("foo");
        assertEquals("bar", output());
    }

    @Test
    public void testSimpleNamedDelimitedNode() {
        NamedWriterNode foo = new NamedWriterNode("foo", new FieldWriterNode(null, null, null, null, IDENTITY_FORMAT));
        foo.init(context);
        pushStartElement("foo");
        assertEquals("", output());
        pushCharacters("b");
        pushCharacters("a");
        pushCharacters("r");
        pushEndElement("foo");
        assertEquals("bar", output());
    }
}
