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
 * @since 2011-12-07
 */
public class CharacterSet {
    private final boolean containsEOF;
    private final CharacterRanges ranges;

    private CharacterSet(CharacterRanges ranges, boolean containsEOF) {
        this.ranges = ranges;
        this.containsEOF = containsEOF;
    }

    public boolean contains(char c) {
        return ranges.contains(c);
    }

    public boolean containsEOF() {
        return containsEOF;
    }

    public static Builder complete() {
        return new Builder(CharacterRanges.complete());
    }

    public static Builder empty() {
        return new Builder(CharacterRanges.empty());
    }

    public static Builder basedOn(CharacterSet characterSet) {
        return new Builder(characterSet.ranges.copy());
    }

    public static class Builder {
        private CharacterRanges ranges;
        private boolean containsEOF;

        private Builder(CharacterRanges ranges) {
            this.ranges = ranges;
        }

        public Builder add(Char c) {
            ranges.addRange(c, c);
            return this;
        }

        public Builder remove(Char c) {
            ranges.removeRange(c, c);
            return this;
        }

        public Builder addEOF() {
            containsEOF = true;
            return this;
        }

        public Builder removeEOF() {
            containsEOF = false;
            return this;
        }

        public Builder addRange(Char from, Char to) {
            ranges.addRange(from, to);
            return this;
        }

        public Builder removeRange(Char from, Char to) {
            ranges.removeRange(from, to);
            return this;
        }

        public CharacterSet build() {
            return new CharacterSet(ranges, containsEOF);
        }

        public void add(CharacterSet characterSet) {
            ranges.addAll(characterSet.ranges);
        }

        public void remove(CharacterSet characterSet) {
            ranges.removeAll(characterSet.ranges);
        }
        
        public void addAll() {
            ranges.addAllCharacters();
        }
    }
}
