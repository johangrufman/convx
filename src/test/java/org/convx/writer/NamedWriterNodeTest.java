package org.convx.writer;

import org.junit.Test;

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
        NamedWriterNode foo = new NamedWriterNode("foo", new DelimitedWriterNode());
        foo.init(context);
        pushStartElement("foo");
        assertEquals("", output());
        pushCharacters("b");
        assertEquals("b", output());
        pushCharacters("a");
        assertEquals("ba", output());
        pushCharacters("r");
        pushEndElement("foo");
        assertEquals("bar", output());
    }
}
