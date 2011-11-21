package org.convx.util;

import java.io.IOException;
import java.io.Writer;

/**
 * @author johan
 * @since 2011-11-18
 */
public class IndentationWriter {

    private static final String INDENTATION = "  ";

    private Writer writer;

    public static final String EOL = System.getProperty("line.separator");

    int indentationLevel = 0;

    public IndentationWriter(Writer writer) {
        this.writer = writer;
    }

    public int indentationLevel() {
        return indentationLevel;
    }

    public void writeLine(String line) {
        int braceCount = braceCount(line);
        if (line.startsWith("}") && indentationLevel > 0) {
            indentationLevel--;
            braceCount++;
        }
        try {
            for (int i = 0; i < indentationLevel; i++) {
                writer.append(INDENTATION);
            }
            writer.append(line);
            writer.append(EOL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        indentationLevel += braceCount;
        indentationLevel = Math.max(0, indentationLevel);
    }

    private int braceCount(String line) {
        int braceCount = 0;
        for (char c : line.toCharArray()) {
            if (c == '{') {
                braceCount++;
            } else if (c == '}') {
                braceCount--;
            }
        }
        return braceCount;
    }
}
