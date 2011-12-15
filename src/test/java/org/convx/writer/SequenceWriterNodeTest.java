package org.convx.writer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        sequenceWriterNode.addSubNode(new FieldWriterNode(null));
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
        sequenceWriterNode.addSubNode(new FieldWriterNode(null));
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
