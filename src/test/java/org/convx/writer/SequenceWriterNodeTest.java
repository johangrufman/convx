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
package org.convx.writer;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author johan
 * @since 2011-10-21
 */
public class SequenceWriterNodeTest extends AbstractWriterTest {

    @Test
    public void testSequenceOfTwoNamedConstants() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        NamedWriterNode foo = new NamedWriterNode("foo", new ConstantWriterNode("bar"));
        sequenceWriterNode.addSubNode(foo);
        sequenceWriterNode.addSubNode(foo);
        sequenceWriterNode.init(context);
        pushEmptyElement("foo");
        pushEmptyElement("foo");
        assertEquals("barbar", output());
    }

    @Test
    public void testSequenceOfTwoNamedConstantsWithOneUnnamedInBetween() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        NamedWriterNode foo = new NamedWriterNode("foo", new ConstantWriterNode("bar"));
        sequenceWriterNode.addSubNode(foo);
        sequenceWriterNode.addSubNode(new ConstantWriterNode("baz"));
        sequenceWriterNode.addSubNode(foo);
        sequenceWriterNode.init(context);
        pushEmptyElement("foo");
        pushEmptyElement("foo");
        assertEquals("barbazbar", output());
    }

    @Test
    public void testNamedSequenceOfOneDelimitedNodeAndOneConstantNode() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        sequenceWriterNode.addSubNode(new FieldWriterNode(null, null, null, null));
        sequenceWriterNode.addSubNode(new ConstantWriterNode("baz"));
        NamedWriterNode foo = new NamedWriterNode("foo", sequenceWriterNode);
        foo.init(context);
        pushStartElement("foo");
        pushCharacters("bar");
        pushEndElement("foo");
        assertEquals("barbaz", output());
    }

    @Test
    public void testNamedSequenceOfTwoConstantNodesAndWith() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        sequenceWriterNode.addSubNode(new ConstantWriterNode("baz"));
        sequenceWriterNode.addSubNode(new ConstantWriterNode("baz"));
        NamedWriterNode foo = new NamedWriterNode("foo", sequenceWriterNode);
        foo.init(context);
        pushStartElement("foo");
        pushEndElement("foo");
        assertEquals("bazbaz", output());
    }

    @Test
    public void testNamedSequenceOfTwoConstantNodesAndWithOneDelimitedNodeBetween() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        sequenceWriterNode.addSubNode(new ConstantWriterNode("baz"));
        sequenceWriterNode.addSubNode(new FieldWriterNode(null, null, null, null));
        sequenceWriterNode.addSubNode(new ConstantWriterNode("baz"));
        NamedWriterNode foo = new NamedWriterNode("foo", sequenceWriterNode);
        foo.init(context);
        pushStartElement("foo");
        pushCharacters("bar");
        pushEndElement("foo");
        assertEquals("bazbarbaz", output());
    }

    @Test
    public void testSequenceOfSequences() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        SequenceWriterNode subSequenceWriterNode = new SequenceWriterNode();
        NamedWriterNode foo = new NamedWriterNode("foo", new ConstantWriterNode("foo"));
        NamedWriterNode bar = new NamedWriterNode("bar", new ConstantWriterNode("bar"));
        subSequenceWriterNode.addSubNode(foo);
        subSequenceWriterNode.addSubNode(bar);
        sequenceWriterNode.addSubNode(subSequenceWriterNode);
        sequenceWriterNode.addSubNode(subSequenceWriterNode);
        sequenceWriterNode.init(context);
        pushEmptyElement("foo");
        pushEmptyElement("bar");
        pushEmptyElement("foo");
        pushEmptyElement("bar");
        assertEquals("foobarfoobar", output());
    }

    @Test
    public void startsWithShouldReturnTrueIfFirstSubNodeStartsWith() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        NamedWriterNode foo = new NamedWriterNode("foo", new ConstantWriterNode("foo"));
        sequenceWriterNode.addSubNode(foo);
        assertTrue(sequenceWriterNode.startsWith(createStartElement("foo")));
    }

    @Test
    public void startsWithShouldReturnTrueIfSecondSubNodeStartsWithAndFirstIsOptional() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        NamedWriterNode foo = new NamedWriterNode("foo", new ConstantWriterNode("foo"));
        RepeatedWriterNode optionalFoo = RepeatedWriterNode.zeroOrOne(foo);

        NamedWriterNode bar = new NamedWriterNode("bar", new ConstantWriterNode("bar"));
        sequenceWriterNode.addSubNode(optionalFoo);
        sequenceWriterNode.addSubNode(bar);
        assertTrue(sequenceWriterNode.startsWith(createStartElement("bar")));
    }

    @Test
    public void startsWithShouldReturnFalseIfSecondSubNodeStartsWithButFirstIsMandatory() {
        SequenceWriterNode sequenceWriterNode = new SequenceWriterNode();
        NamedWriterNode foo = new NamedWriterNode("foo", new ConstantWriterNode("foo"));
        NamedWriterNode bar = new NamedWriterNode("bar", new ConstantWriterNode("bar"));
        sequenceWriterNode.addSubNode(foo);
        sequenceWriterNode.addSubNode(bar);
        assertFalse(sequenceWriterNode.startsWith(createStartElement("bar")));
    }
}
