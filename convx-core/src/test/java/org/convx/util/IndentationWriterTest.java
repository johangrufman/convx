/*
   Copyright (C) 2012 Johan Grufman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.convx.util;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * @author johan
 * @since 2011-11-18
 */
public class IndentationWriterTest {

    private StringWriter writer;

    private IndentationWriter indentationWriter;

    private static final String INDENTATION = "  ";

    @Before
    public void setup() {

        writer = new StringWriter();
        indentationWriter = new IndentationWriter(writer);
    }

    @Test
    public void startIndentationLevelShouldBeZero() {
        assertEquals(0, new IndentationWriter(new StringWriter()).indentationLevel());
    }

    @Test
    public void whenIndentationLevelIsZeroLinesShouldBePassedTroughWithEolAdded() {
        indentationWriter.writeLine("line");
        assertEquals("line\n", writer.toString());
    }

    @Test
    public void indentationLevelShouldIncreaseByOneWhenLineContainsLeftBraceAndDecreaseByOneWhenLineContainsRightBrace() {
        assertEquals(0, indentationWriter.indentationLevel());
        indentationWriter.writeLine("line with left brace {");
        assertEquals(1, indentationWriter.indentationLevel());
        indentationWriter.writeLine("line with left brace }");
        assertEquals(0, indentationWriter.indentationLevel());
    }

    @Test
    public void indentationLevelShouldNotDecreaseBelowZero() {
        assertEquals(0, indentationWriter.indentationLevel());
        indentationWriter.writeLine("line with left brace }");
        assertEquals(0, indentationWriter.indentationLevel());
    }


    @Test
    public void twoSpacesShouldBePrependedWhenIndentationLevelIsOne() {
        assertEquals(0, indentationWriter.indentationLevel());
        String line1 = "line 1 {";
        indentationWriter.writeLine(line1);
        String line2 = "line 2";
        indentationWriter.writeLine(line2);
        String[] lines = writer.toString().split(IndentationWriter.EOL);
        assertEquals(line1, lines[0]);
        assertEquals(INDENTATION + line2, lines[1]);
    }

    @Test
    public void indentationShouldDecreaseAfterLineIsAppendedUnlessLineStartsWithRightBrace() {
        String line1 = "line 1 {";
        indentationWriter.writeLine(line1);
        String line2 = "line 2 }";
        indentationWriter.writeLine(line2);
        String line3 = "line 3 {";
        indentationWriter.writeLine(line3);
        String line4 = "} line 4";
        indentationWriter.writeLine(line4);
        String[] lines = writer.toString().split(IndentationWriter.EOL);
        assertEquals(line1, lines[0]);
        assertEquals(INDENTATION + line2, lines[1]);
        assertEquals(line3, lines[2]);
        assertEquals(line4, lines[3]);
    }
}
