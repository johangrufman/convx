package org.convx.characters;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author johan
 * @since 2011-12-11
 */
public class CharacterRangesTest {

    private CharacterRanges characterRanges;

    @Before
    public void setup() throws Exception {
        characterRanges = CharacterRanges.empty();
    }

    @Test
    public void basicRange() throws Exception {
        characterRanges.addRange('b', 'd');
        assertContainsNot('a', 'e');
        assertContains('b', 'c', 'd');
        assertRangesEqualTo("[b-d]");
    }

    @Test
    public void twoRanges() {
        characterRanges.addRange('a', 'b');
        characterRanges.addRange('d', 'e');
        assertContains('a', 'b', 'd', 'e');
        assertContainsNot('c', 'f');
        assertRangesEqualTo("[a-b] [d-e]");
    }

    @Test
    public void twoOverlappingRanges() {
        characterRanges.addRange('a', 'b');
        characterRanges.addRange('b', 'c');
        assertContains('a', 'b', 'c');
        assertContainsNot('d');
        assertRangesEqualTo("[a-c]");
    }

    @Test
    public void twoOverlappingRangesWithSameFrom() {
        characterRanges.addRange('a', 'b');
        characterRanges.addRange('a', 'c');
        assertContains('a', 'b', 'c');
        assertContainsNot('d');
        assertRangesEqualTo("[a-c]");
    }

    @Test
    public void twoOverlappingRangesWithSameFromInReverseOrder() {
        characterRanges.addRange('a', 'c');
        characterRanges.addRange('a', 'b');
        assertContains('a', 'b', 'c');
        assertContainsNot('d');
        assertRangesEqualTo("[a-c]");
    }

    @Test
    public void twoNonOverlappingButAdjacentRangesShouldBeMerged() throws Exception {
        characterRanges.addRange('a', 'b');
        characterRanges.addRange('c', 'd');
        assertRangesEqualTo("[a-d]");
    }

    @Test
    public void twoMoreNonOverlappingButAdjacentRangesShouldBeMerged() throws Exception {
        characterRanges.addRange('c', 'd');
        characterRanges.addRange('a', 'b');
        assertRangesEqualTo("[a-d]");
    }

    @Test
    public void twoExistingRangesGetMergedByThird() {
        characterRanges.addRange('a', 'b');
        characterRanges.addRange('d', 'e');
        assertRangesEqualTo("[a-b] [d-e]");
        characterRanges.addRange('b', 'd');
        assertRangesEqualTo("[a-e]");
    }

    @Test
    public void threeExistingRangesAffectedWhenFourthAdded() {
        characterRanges.addRange('a', 'b');
        characterRanges.addRange('d', 'e');
        characterRanges.addRange('g', 'h');
        assertRangesEqualTo("[a-b] [d-e] [g-h]");
        characterRanges.addRange('b', 'g');
        assertRangesEqualTo("[a-h]");
    }

    @Test
    public void rangedRemovedInTheMiddle() {
        characterRanges.addRange('a', 'f');
        assertRangesEqualTo("[a-f]");
        characterRanges.removeRange('c', 'd');
        assertRangesEqualTo("[a-b] [e-f]");
    }

    @Test
    public void rangeRemovedFromTheEnd() throws Exception {
        characterRanges.addRange('a', 'd');
        assertRangesEqualTo("[a-d]");
        characterRanges.removeRange('c', 'e');
        assertRangesEqualTo("[a-b]");
    }

    @Test
    public void rangeRemovedFromTheStart() throws Exception {
        characterRanges.addRange('d', 'f');
        assertRangesEqualTo("[d-f]");
        characterRanges.removeRange('a', 'd');
        assertRangesEqualTo("[e-f]");
    }

    @Test
    public void prefixRangeRemoved() throws Exception {
        characterRanges.addRange('c', 'f');
        assertRangesEqualTo("[c-f]");
        characterRanges.removeRange('c', 'd');
        assertRangesEqualTo("[e-f]");
    }

    @Test
    public void prefixRangeRemoveAll() throws Exception {
        characterRanges.addRange('a', 'c');
        assertRangesEqualTo("[a-c]");
        characterRanges.removeRange('a', 'd');
        assertRangesEqualTo("");
    }

    @Test
    public void suffixRangeRemoved() throws Exception {
        characterRanges.addRange('a', 'f');
        assertRangesEqualTo("[a-f]");
        characterRanges.removeRange('d', 'f');
        assertRangesEqualTo("[a-c]");
    }

    @Test
    public void suffixRangeRemoveAll() throws Exception {
        characterRanges.addRange('d', 'f');
        assertRangesEqualTo("[d-f]");
        characterRanges.removeRange('a', 'f');
        assertRangesEqualTo("");
    }

    @Test
    public void rangeRemovedAffectingTwoExisting() throws Exception {
        characterRanges.addRange('a', 'c');
        characterRanges.addRange('e', 'g');
        assertRangesEqualTo("[a-c] [e-g]");
        characterRanges.removeRange('c', 'e');
        assertRangesEqualTo("[a-b] [f-g]");
    }

    @Test
    public void rangeRemovedAffectingThreeExisting() throws Exception {
        characterRanges.addRange('a', 'c');
        characterRanges.addRange('e', 'g');
        characterRanges.addRange('i', 'k');
        assertRangesEqualTo("[a-c] [e-g] [i-k]");
        characterRanges.removeRange('c', 'i');
        assertRangesEqualTo("[a-b] [j-k]");
    }

    private void assertContains(char... chars) {
        for (char c : chars) {
            assertTrue("Should contain " + c, characterRanges.contains(c));
        }
    }

    private void assertContainsNot(char... chars) {
        for (char c : chars) {
            assertFalse("Should not contain " + c, characterRanges.contains(c));
        }
    }

    private void assertRangesEqualTo(String rangeString) {
        assertEquals(rangeString, characterRanges.rangeString());
    }
}
