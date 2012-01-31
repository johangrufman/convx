package org.convx.characters;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author johan
 * @since 2011-12-11
 */
class CharacterRanges {

    private SortedMap<Char, Range> ranges = new TreeMap<Char, Range>();

    private CharacterRanges() {}

    private CharacterRanges(SortedMap<Char, Range> ranges) {
        this.ranges.putAll(ranges);
    }

    public void addRange(Char from, Char to) {
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

    public void removeRange(Char from, Char to) {
        removeRangesInBetween(from, to);
        Range range = rangeAt(from);
        if (range != null && range.getFrom().lessThan(from)) {
            if (range.getTo().greaterThan(to)) {
                ranges.remove(range.getFrom());
                add(new Range(range.getFrom(), from.previous()));
                add(new Range(to.next(), range.getTo()));
            } else {
                ranges.remove(range.getFrom());
                add(new Range(range.getFrom(), from.previous()));
            }
        }
        range = rangeAt(to);
        if (range != null) {
            ranges.remove(range.getFrom());
            add(new Range(to.next(), range.getTo()));
        }

    }

    private void removeRangesInBetween(Char from, Char to) {
        SortedMap<Char, Range> between = ranges.subMap(from, to.next());
        for (Iterator<Range> iterator = between.values().iterator(); iterator.hasNext(); ) {
            Range range = iterator.next();
            if (range.getTo().lessThanOrEquals(to)) {
                iterator.remove();
            }
        }
    }

    public boolean contains(char c) {
        return rangeAt(new Char(c)) != null;
    }

    private Range rangeAt(Char c) {
        SortedMap<Char, Range> head = ranges.headMap(c.next());
        if (!head.isEmpty() && ranges.get(head.lastKey()).getTo().greaterThanOrEquals(c)) {
            return ranges.get(head.lastKey());
        }
        return null;
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
        SortedMap<Char, Range> tail = ranges.tailMap(newRange.getFrom());
        if (!tail.isEmpty() && ranges.get(tail.firstKey()).getFrom().lessThanOrEquals(newRange.getTo().next())) {
            Range overlappingRange = ranges.get(tail.firstKey());
            ranges.remove(overlappingRange.getFrom());
            newRange.merge(overlappingRange);
        }
    }

    private void leftMerge(Range newRange) {
        SortedMap<Char, Range> head = ranges.headMap(newRange.getFrom().next());
        if (!head.isEmpty() && ranges.get(head.lastKey()).getTo().next().greaterThanOrEquals(newRange.getFrom())) {
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
        addRange(Char.MIN_VALUE, Char.MAX_VALUE);
    }

    private static class Range {
        private Char from;

        private Char to;

        private Range(Char from, Char to) {
            this.from = from;
            this.to = to;
        }

        public Char getFrom() {
            return from;
        }

        public Char getTo() {
            return to;
        }

        public void merge(Range overlappingRange) {

            from = min(from, overlappingRange.getFrom());
            to = max(to, overlappingRange.getTo());
        }

        @Override
        public String toString() {
            return "[" + from.asCharacter() + "-" + to.asCharacter() + "]";
        }

        private static Char min(Char c1, Char c2) {
            return c1.compareTo(c2) < 0 ? c1 : c2;
        }

        private static Char max(Char c1, Char c2) {
            return c1.compareTo(c2) >= 0 ? c1 : c2;
        }
    }

}
