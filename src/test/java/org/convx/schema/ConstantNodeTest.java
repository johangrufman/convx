package org.convx.schema;

import org.junit.Test;

import java.io.StringReader;
import java.util.Stack;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author johan
 * @since 2011-06-06
 */
public class ConstantNodeTest {
    @Test
    public void testSuccessfulParse() throws Exception {
        ConstantSchemaNode xyz = new ConstantSchemaNode("xyz");
        ParserContext context = new ParserContext(new StringReader("xyz"), 3);
        assertTrue(xyz.parse(new Stack(), context, null));
        assertFalse(context.hasMoreCharacters());
    }
}
