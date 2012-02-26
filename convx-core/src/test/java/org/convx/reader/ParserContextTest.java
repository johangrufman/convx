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
package org.convx.reader;

import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * @author johan
 * @since 2011-06-06
 */
public class ParserContextTest {
    private static final int LONG_BUFFER_SIZE = 1024;
    private static final int SHORT_BUFFER_SIZE = 3;

    private String stringToParse;
    private int lookAhead;
    private ParserContext contextWithLongBuffer;
    private ParserContext contextWithShortBuffer;

    @Before
    public void setup() {
        stringToParse = "0123456789";
        lookAhead = 2;
        contextWithLongBuffer = new ParserContext(new StringReader(stringToParse), lookAhead, LONG_BUFFER_SIZE);
        contextWithShortBuffer = new ParserContext(new StringReader(stringToParse), lookAhead, SHORT_BUFFER_SIZE);
    }

    @Test( expected = IllegalArgumentException.class )
    public void testCreateParserContextWithShorterBufferThanLookAhead() {
        new ParserContext(new StringReader(stringToParse), 2, 1);
    }

    @Test
    public void testNextCharactersStartsWithPrefix() throws Exception {
        String prefix = stringToParse.substring(0, lookAhead);
        assertTrue(contextWithLongBuffer.nextCharacters().toString().startsWith(prefix));
    }

    @Test
    public void testAdvanceByOne() throws Exception {
        contextWithLongBuffer.advance(1);
        String prefix = stringToParse.substring(1, lookAhead+1);
        assertEquals(contextWithLongBuffer.nextCharacters().toString().substring(0, lookAhead), prefix);
    }

    @Test
    public void testAdvanceByTwo() throws Exception {
        contextWithLongBuffer.advance(2);
        String prefix = stringToParse.substring(2, lookAhead+2);
        assertTrue(contextWithLongBuffer.nextCharacters().toString().startsWith(prefix));
    }

    @Test
    public void testSubsequentCallsToAdvance() throws Exception {
        contextWithLongBuffer.advance(1);
        String prefix = stringToParse.substring(1, lookAhead+1);
        assertEquals(contextWithLongBuffer.nextCharacters().toString().substring(0, lookAhead), prefix);

        contextWithLongBuffer.advance(2);
        prefix = stringToParse.substring(3, lookAhead+3);
        assertTrue(contextWithLongBuffer.nextCharacters().toString().startsWith(prefix));
    }

    @Test
    public void testLimit() {
        contextWithLongBuffer.advance(10);
        assertFalse(contextWithLongBuffer.hasMoreCharacters());
    }

    @Test
    public void testAdvanceWithShortBuffer() {
        contextWithShortBuffer.advance(2);
        String prefix = stringToParse.substring(2, lookAhead+2);
        assertTrue(contextWithShortBuffer.nextCharacters().length() >= 2);
        assertEquals(contextWithShortBuffer.nextCharacters().toString().substring(0, lookAhead), prefix);

        contextWithShortBuffer.advance(2);
        prefix = stringToParse.substring(4, lookAhead+4);
        assertTrue(contextWithShortBuffer.nextCharacters().length() >= 2);
        assertEquals(contextWithShortBuffer.nextCharacters().toString().substring(0, lookAhead), prefix);

    }


}
