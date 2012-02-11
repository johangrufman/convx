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
