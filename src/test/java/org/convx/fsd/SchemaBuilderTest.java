package org.convx.fsd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author johan
 * @since 2011-12-18
 */
public class SchemaBuilderTest {

    @Test
    public void unescapingCharacters() throws Exception {
        assertEquals('\n', (char)SchemaBuilder.fromEscapedStringToCharacter("\\n"));
        assertEquals('\t', (char)SchemaBuilder.fromEscapedStringToCharacter("\\t"));
        assertEquals(' ', (char)SchemaBuilder.fromEscapedStringToCharacter("\\u0020"));
        assertEquals('\\', (char)SchemaBuilder.fromEscapedStringToCharacter("\\\\"));
        assertEquals('\'', (char)SchemaBuilder.fromEscapedStringToCharacter("\\\'"));
    }
}
