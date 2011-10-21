package org.convx.writer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        sequenceWriterNode.addSubNode(new DelimitedWriterNode());
        sequenceWriterNode.addSubNode(new ConstantWriterNode("baz"));
        NamedWriterNode foo = new NamedWriterNode("foo", sequenceWriterNode);
        foo.init(context);
        pushStartElement("foo");
        pushCharacters("bar");
        pushEndElement("foo");
        assertEquals("barbaz", output());
    }
}
