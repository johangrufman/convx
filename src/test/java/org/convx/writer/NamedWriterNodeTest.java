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
        NamedWriterNode foo = new NamedWriterNode("foo", new FieldWriterNode(null, null, null, null));
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
