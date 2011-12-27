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
        assertEquals('\n', SchemaBuilder.fromEscapedStringToCharacter("\\n"));
        assertEquals('\t', SchemaBuilder.fromEscapedStringToCharacter("\\t"));
        assertEquals(' ', SchemaBuilder.fromEscapedStringToCharacter("\\u0020"));
        assertEquals('\\', SchemaBuilder.fromEscapedStringToCharacter("\\\\"));
        assertEquals('\'', SchemaBuilder.fromEscapedStringToCharacter("\\\'"));
    }
}
