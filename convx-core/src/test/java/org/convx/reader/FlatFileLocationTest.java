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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author johan
 * @since 2012-02-11
 */
public class FlatFileLocationTest {

    @Test
    public void testInitialLocation() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        assertEquals(0, location.getCharacterOffset());
        assertEquals(1, location.getColumnNumber());
        assertEquals(1, location.getLineNumber());
    }

    @Test
    public void testLocationAfterOneChar() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("a");
        assertEquals(1, location.getCharacterOffset());
        assertEquals(2, location.getColumnNumber());
        assertEquals(1, location.getLineNumber());
    }

    @Test
    public void testLocationAfterFiveCharacters() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("abcde");
        assertEquals(5, location.getCharacterOffset());
        assertEquals(6, location.getColumnNumber());
        assertEquals(1, location.getLineNumber());
    }

    @Test
    public void testLocationAfterFiveCharactersWithLineFeedAfterThird() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("abc\nde");
        assertEquals(6, location.getCharacterOffset());
        assertEquals(3, location.getColumnNumber());
        assertEquals(2, location.getLineNumber());
    }

    @Test
    public void testLocationAfterFiveCharactersWithTwoLineFeed() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("abc\nd\ne");
        assertEquals(7, location.getCharacterOffset());
        assertEquals(2, location.getColumnNumber());
        assertEquals(3, location.getLineNumber());
    }

    @Test
    public void testLocationAfterFiveCharactersWithTwoLineFeedGivenSeparately() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("abc\nd");
        location.advance("\ne");
        assertEquals(7, location.getCharacterOffset());
        assertEquals(2, location.getColumnNumber());
        assertEquals(3, location.getLineNumber());
    }

    @Test
    public void testLocationAfterOneCarriageReturn() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("abc\rde");
        assertEquals(6, location.getCharacterOffset());
        assertEquals(3, location.getColumnNumber());
        assertEquals(2, location.getLineNumber());
    }

    @Test
    public void testLocationAfterALineFeedAndACarriageReturn() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("abc\n\rde");
        assertEquals(7, location.getCharacterOffset());
        assertEquals(3, location.getColumnNumber());
        assertEquals(3, location.getLineNumber());
    }

    @Test
    public void testLocationAfterALineFeedAndACarriageReturnGivenSeparately() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("abc\n");
        location.advance("\rde");
        assertEquals(7, location.getCharacterOffset());
        assertEquals(3, location.getColumnNumber());
        assertEquals(3, location.getLineNumber());
    }

    @Test
    public void testLocationAfterACarriageReturnAndALineFeed() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("abc\r\nde");
        assertEquals(7, location.getCharacterOffset());
        assertEquals(3, location.getColumnNumber());
        assertEquals(2, location.getLineNumber());
    }

    @Test
    public void testLocationAfterACarriageReturnAndALineFeedGivenSeparately() throws Exception {
        FlatFileLocation location = new FlatFileLocation();
        location.advance("abc\r");
        location.advance("\nde");
        assertEquals(7, location.getCharacterOffset());
        assertEquals(3, location.getColumnNumber());
        assertEquals(2, location.getLineNumber());
    }
}
