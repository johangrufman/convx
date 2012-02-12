package org.convx.reader;

/**
 * @author johan
 * @since 2012-02-11
 */
public class FlatFileLocation {
    private int lineNumber = 1;
    private int columnNumber = 1;
    private int characterOffset = 0;
    private boolean previousCharCarriageReturn = false;

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public int getCharacterOffset() {
        return characterOffset;
    }

    public void advance(CharSequence charSequence) {
        characterOffset += charSequence.length();
        for (char c : charSequence.toString().toCharArray()) {
            if (c == '\n') {
                if (!previousCharCarriageReturn) {
                    lineNumber++;
                    columnNumber = 1;
                }
                previousCharCarriageReturn = false;
            } else if (c == '\r') {
                lineNumber++;
                columnNumber = 1;
                previousCharCarriageReturn = true;
            } else {
                columnNumber++;
                previousCharCarriageReturn = false;
            }
        }
    }

}
