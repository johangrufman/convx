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

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * @author johan
 * @since 2011-06-06
 */
public class ParserContext {
    private static final int BUFFER_SIZE = 1024;

    private Reader reader;

    private CharBuffer buffer;

    private int lookAhead;

    private int position = 0;

    private FlatFileLocation flatFileLocation = new FlatFileLocation();

    public ParserContext(Reader reader, int lookAhead) {
        this(reader, lookAhead, BUFFER_SIZE);
    }

    public ParserContext(Reader reader, int lookAhead, int bufferSize) {
        if (bufferSize < lookAhead) {
            throw new IllegalArgumentException("Buffer size must be greater than or equal to look ahead.");
        }
        this.reader = reader;
        this.lookAhead = lookAhead;
        buffer = CharBuffer.allocate(bufferSize);
        fillBuffer();
    }

    private void fillBuffer() {
        try {
            this.reader.read(buffer);
            buffer.limit(buffer.position());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer.rewind();
    }

    public CharSequence nextCharacters() {
        return buffer;
    }

    public void advance(int length) {
        flatFileLocation.advance(buffer.subSequence(0, Math.min(buffer.remaining(), length)));
        position += length;
        buffer.position(position);
        if (buffer.length() < lookAhead) {
            buffer.compact();
            fillBuffer();
            position = 0;
        }
    }


    public boolean hasMoreCharacters() {
        return buffer.length() > 0;
    }

    public char nextCharacter() {
        return buffer.charAt(0);
    }

    public FlatFileLocation getFlatFileLocation() {
        return flatFileLocation;
    }
}
