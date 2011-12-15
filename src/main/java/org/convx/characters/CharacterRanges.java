package org.convx.characters;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author johan
 * @since 2011-12-11
 */
class CharacterRanges {

    private SortedMap<Character, Range> ranges = new TreeMap<Character, Range>();

    private CharacterRanges() {}

    private CharacterRanges(SortedMap<Character, Range> ranges) {
        this.ranges.putAll(ranges);
    }

    public void addRange(char from, char to) {
        removeRangesInBetween(from, to);
        Range newRange = new Range(from, to);
        leftMerge(newRange);
        rightMerge(newRange);
        add(newRange);
    }

    public void addAll(CharacterRanges characterRanges) {
        for (Range range : characterRanges.ranges.values()) {
            addRange(range.getFrom(), range.getTo());
        }
    }

    public void removeAll(CharacterRanges characterRanges) {
        for (Range range : characterRanges.ranges.values()) {
            removeRange(range.getFrom(), range.getTo());
        }
    }


    private void add(Range range) {
        ranges.put(range.getFrom(), range);
    }

    public void removeRange(char from, char to) {
        removeRangesInBetween(from, to);
        Range range = rangeAt(from);
        if (range != null && range.getFrom() < from) {
            if (range.getTo() > to) {
                ranges.remove(range.getFrom());
                add(new Range(range.getFrom(), previousChar(from)));
                add(new Range(nextChar(to), range.getTo()));
            } else {
                ranges.remove(range.getFrom());
                add(new Range(range.getFrom(), previousChar(from)));
            }
        }
        range = rangeAt(to);
        if (range != null) {
            ranges.remove(range.getFrom());
            add(new Range(nextChar(to), range.getTo()));
        }

    }

    private void removeRangesInBetween(char from, char to) {
        SortedMap<Character, Range> between = ranges.subMap(from, nextChar(to));
        for (Iterator<Range> iterator = between.values().iterator(); iterator.hasNext(); ) {
            Range range = iterator.next();
            if (range.getTo() <= to) {
                iterator.remove();
            }
        }
    }

    private char previousChar(char from) {
        return (char) (from - 1);
    }

    public boolean contains(char c) {
        return rangeAt(c) != null;
    }

    private Range rangeAt(char c) {
        SortedMap<Character, Range> head = ranges.headMap(nextChar(c));
        if (!head.isEmpty() && ranges.get(head.lastKey()).getTo() >= c) {
            return ranges.get(head.lastKey());
        }
        return null;
    }

    private char nextChar(char c) {
        return (char) (c + 1);
    }

    @Override
    public String toString() {
        return rangeString();
    }

    String rangeString() {
        StringBuilder sb = new StringBuilder();
        for (Range range : ranges.values()) {
            sb.append(range);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    private void rightMerge(Range newRange) {
        SortedMap<Character, Range> tail = ranges.tailMap(newRange.getFrom());
        if (!tail.isEmpty() && ranges.get(tail.firstKey()).getFrom() <= newRange.getTo() + 1) {
            Range overlappingRange = ranges.get(tail.firstKey());
            ranges.remove(overlappingRange.getFrom());
            newRange.merge(overlappingRange);
        }
    }

    private void leftMerge(Range newRange) {
        SortedMap<Character, Range> head = ranges.headMap(nextChar(newRange.getFrom()));
        if (!head.isEmpty() && ranges.get(head.lastKey()).getTo() + 1 >= newRange.getFrom()) {
            Range overlappingRange = ranges.get(head.lastKey());
            ranges.remove(overlappingRange.getFrom());
            newRange.merge(overlappingRange);
        }
    }

    public CharacterRanges copy() {
        return new CharacterRanges(ranges);
    }

    public static CharacterRanges empty() {
       return new CharacterRanges();
    }

    public static CharacterRanges complete() {
        CharacterRanges characterRanges = new CharacterRanges();
        characterRanges.addAllCharacters();
        return characterRanges;
    }

    public void addAllCharacters() {
        addRange(Character.MIN_VALUE, Character.MAX_VALUE);
    }

    private static class Range {
        private char from;

        private char to;

        private Range(char from, char to) {
            this.from = from;
            this.to = to;
        }

        public char getFrom() {
            return from;
        }

        public char getTo() {
            return to;
        }

        public void merge(Range overlappingRange) {
            from = min(overlappingRange.getFrom());
            to = max(overlappingRange.getTo());
        }

        @Override
        public String toString() {
            return "[" + from + "-" + to + "]";
        }

        private char min(char c) {
            return from < c ? from : c;
        }

        private char max(char c) {
            return to > c ? to : c;
        }
    }

}
