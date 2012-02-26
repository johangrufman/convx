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
