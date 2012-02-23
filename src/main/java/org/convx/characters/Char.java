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
