package org.convx.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author johan
 * @since 2011-11-19
 */
public class CharacterUtilTest {

    @Test
    public void testEscapeCharacter() throws Exception {
        assertEquals("a", CharacterUtil.escapeCharacter('a'));
        assertEquals("b", CharacterUtil.escapeCharacter('b'));
        assertEquals("\\n", CharacterUtil.escapeCharacter('\n'));
        assertEquals("\\r", CharacterUtil.escapeCharacter('\r'));
    }

    @Test
    public void testEscapeCharacters() throws Exception {
        assertEquals("abc", CharacterUtil.escapeCharacters("abc"));
        assertEquals("\\n\\r", CharacterUtil.escapeCharacters("\n\r"));
    }

    @Test
    public void testUnescapeCharacters() throws Exception {
        assertEquals("abc", CharacterUtil.unescapeCharacters("abc"));
        assertEquals("\n\r", CharacterUtil.unescapeCharacters("\\n\\r"));
    }
}
