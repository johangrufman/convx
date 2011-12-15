package org.convx.characters;

/**
 * @author johan
 * @since 2011-12-07
 */
public class CharacterSet {
    private CharacterRanges ranges;

    private CharacterSet(CharacterRanges ranges) {
        this.ranges = ranges;
    }

    public boolean contains(char c) {
        return ranges.contains(c);
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

        private Builder(CharacterRanges ranges) {
            this.ranges = ranges;
        }

        public Builder add(char c) {
            ranges.addRange(c, c);
            return this;
        }

        public Builder remove(char c) {
            ranges.removeRange(c, c);
            return this;
        }

        public Builder addRange(char from, char to) {
            ranges.addRange(from, to);
            return this;
        }

        public CharacterSet build() {
            return new CharacterSet(ranges);
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
