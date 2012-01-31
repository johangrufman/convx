package org.convx.characters;

/**
 * @author johan
 * @since 2011-12-27
 */
public class Char implements Comparable<Char> {
    public static final Char EOF = new Char(-1);
    public static final Char MIN_VALUE = new Char(Character.MIN_VALUE);
    public static final Char MAX_VALUE = new Char(Character.MAX_VALUE);

    private int character;

    private Char(int character) {
        this.character = character;
    }

    public Char(char character) {
        this.character = character;
    }

    public char asCharacter() {
        return (char) character;
    }

    public boolean isEOF() {
        return this == EOF;
    }

    public int compareTo(Char otherChar) {
        return character - otherChar.character;
    }

    public Char previous() {
        return new Char(character - 1);
    }

    public Char next() {
        return new Char(character + 1);
    }

    public boolean greaterThanOrEquals(Char c) {
        return compareTo(c) >= 0;
    }

    public boolean lessThanOrEquals(Char c) {
        return compareTo(c) <= 0;
    }

    public boolean greaterThan(Char c) {
        return compareTo(c) > 0;
    }

    public boolean lessThan(Char c) {
        return compareTo(c) < 0;
    }

    public static Char valueOf(char c) {
        return new Char(c);
    }
}
