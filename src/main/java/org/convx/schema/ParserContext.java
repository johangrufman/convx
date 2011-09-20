package org.convx.schema;

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
}
