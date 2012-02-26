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
