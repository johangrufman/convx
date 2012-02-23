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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author johan
 * @since 2011-12-11
 */
public class CharacterRangesTest {

    private static final Char CHAR_A = Char.valueOf('a');

    private static final Char CHAR_B = Char.valueOf('b');

    private static final Char CHAR_C = Char.valueOf('c');

    private static final Char CHAR_D = Char.valueOf('d');

    private static final Char CHAR_E = Char.valueOf('e');

    private static final Char CHAR_F = Char.valueOf('f');

    private static final Char CHAR_G = Char.valueOf('g');

    private static final Char CHAR_H = Char.valueOf('h');

    private static final Char CHAR_I = Char.valueOf('i');

    private static final Char CHAR_K = Char.valueOf('k');

    private CharacterRanges characterRanges;

    @Before
    public void setup() throws Exception {
        characterRanges = CharacterRanges.empty();
    }

    @Test
    public void basicRange() throws Exception {
        characterRanges.addRange(CHAR_B, CHAR_D);
        assertContainsNot(CHAR_A, CHAR_E);
        assertContains(CHAR_B, CHAR_C, CHAR_D);
        assertRangesEqualTo("[b-d]");
    }

    @Test
    public void twoRanges() {
        characterRanges.addRange(CHAR_A, CHAR_B);
        characterRanges.addRange(CHAR_D, CHAR_E);
        assertContains(CHAR_A, CHAR_B, CHAR_D, CHAR_E);
        assertContainsNot(CHAR_C, CHAR_F);
        assertRangesEqualTo("[a-b] [d-e]");
    }

    @Test
    public void twoOverlappingRanges() {
        characterRanges.addRange(CHAR_A, CHAR_B);
        characterRanges.addRange(CHAR_B, CHAR_C);
        assertContains(CHAR_A, CHAR_B, CHAR_C);
        assertContainsNot(CHAR_D);
        assertRangesEqualTo("[a-c]");
    }

    @Test
    public void twoOverlappingRangesWithSameFrom() {
        characterRanges.addRange(CHAR_A, CHAR_B);
        characterRanges.addRange(CHAR_A, CHAR_C);
        assertContains(CHAR_A, CHAR_B, CHAR_C);
        assertContainsNot(CHAR_D);
        assertRangesEqualTo("[a-c]");
    }

    @Test
    public void twoOverlappingRangesWithSameFromInReverseOrder() {
        characterRanges.addRange(CHAR_A, CHAR_C);
        characterRanges.addRange(CHAR_A, CHAR_B);
        assertContains(CHAR_A, CHAR_B, CHAR_C);
        assertContainsNot(CHAR_D);
        assertRangesEqualTo("[a-c]");
    }

    @Test
    public void twoNonOverlappingButAdjacentRangesShouldBeMerged() throws Exception {
        characterRanges.addRange(CHAR_A, CHAR_B);
        characterRanges.addRange(CHAR_C, CHAR_D);
        assertRangesEqualTo("[a-d]");
    }

    @Test
    public void twoMoreNonOverlappingButAdjacentRangesShouldBeMerged() throws Exception {
        characterRanges.addRange(CHAR_C, CHAR_D);
        characterRanges.addRange(CHAR_A, CHAR_B);
        assertRangesEqualTo("[a-d]");
    }

    @Test
    public void twoExistingRangesGetMergedByThird() {
        characterRanges.addRange(CHAR_A, CHAR_B);
        characterRanges.addRange(CHAR_D, CHAR_E);
        assertRangesEqualTo("[a-b] [d-e]");
        characterRanges.addRange(CHAR_B, CHAR_D);
        assertRangesEqualTo("[a-e]");
    }

    @Test
    public void threeExistingRangesAffectedWhenFourthAdded() {
        characterRanges.addRange(CHAR_A, CHAR_B);
        characterRanges.addRange(CHAR_D, CHAR_E);
        characterRanges.addRange(CHAR_G, CHAR_H);
        assertRangesEqualTo("[a-b] [d-e] [g-h]");
        characterRanges.addRange(CHAR_B, CHAR_G);
        assertRangesEqualTo("[a-h]");
    }

    @Test
    public void rangedRemovedInTheMiddle() {
        characterRanges.addRange(CHAR_A, CHAR_F);
        assertRangesEqualTo("[a-f]");
        characterRanges.removeRange(CHAR_C, CHAR_D);
        assertRangesEqualTo("[a-b] [e-f]");
    }

    @Test
    public void rangeRemovedFromTheEnd() throws Exception {
        characterRanges.addRange(CHAR_A, CHAR_D);
        assertRangesEqualTo("[a-d]");
        characterRanges.removeRange(CHAR_C, CHAR_E);
        assertRangesEqualTo("[a-b]");
    }

    @Test
    public void rangeRemovedFromTheStart() throws Exception {
        characterRanges.addRange(CHAR_D, CHAR_F);
        assertRangesEqualTo("[d-f]");
        characterRanges.removeRange(CHAR_A, CHAR_D);
        assertRangesEqualTo("[e-f]");
    }

    @Test
    public void prefixRangeRemoved() throws Exception {
        characterRanges.addRange(CHAR_C, CHAR_F);
        assertRangesEqualTo("[c-f]");
        characterRanges.removeRange(CHAR_C, CHAR_D);
        assertRangesEqualTo("[e-f]");
    }

    @Test
    public void prefixRangeRemoveAll() throws Exception {
        characterRanges.addRange(CHAR_A, CHAR_C);
        assertRangesEqualTo("[a-c]");
        characterRanges.removeRange(CHAR_A, CHAR_D);
        assertRangesEqualTo("");
    }

    @Test
    public void suffixRangeRemoved() throws Exception {
        characterRanges.addRange(CHAR_A, CHAR_F);
        assertRangesEqualTo("[a-f]");
        characterRanges.removeRange(CHAR_D, CHAR_F);
        assertRangesEqualTo("[a-c]");
    }

    @Test
    public void suffixRangeRemoveAll() throws Exception {
        characterRanges.addRange(CHAR_D, CHAR_F);
        assertRangesEqualTo("[d-f]");
        characterRanges.removeRange(CHAR_A, CHAR_F);
        assertRangesEqualTo("");
    }

    @Test
    public void rangeRemovedAffectingTwoExisting() throws Exception {
        characterRanges.addRange(CHAR_A, CHAR_C);
        characterRanges.addRange(CHAR_E, CHAR_G);
        assertRangesEqualTo("[a-c] [e-g]");
        characterRanges.removeRange(CHAR_C, CHAR_E);
        assertRangesEqualTo("[a-b] [f-g]");
    }

    @Test
    public void rangeRemovedAffectingThreeExisting() throws Exception {
        characterRanges.addRange(CHAR_A, CHAR_C);
        characterRanges.addRange(CHAR_E, CHAR_G);
        characterRanges.addRange(CHAR_I, CHAR_K);
        assertRangesEqualTo("[a-c] [e-g] [i-k]");
        characterRanges.removeRange(CHAR_C, CHAR_I);
        assertRangesEqualTo("[a-b] [j-k]");
    }

    private void assertContains(Char... chars) {
        for (Char c : chars) {
            assertTrue("Should contain " + c, characterRanges.contains(c.asCharacter()));
        }
    }

    private void assertContainsNot(Char... chars) {
        for (Char c : chars) {
            assertFalse("Should not contain " + c, characterRanges.contains(c.asCharacter()));
        }
    }

    private void assertRangesEqualTo(String rangeString) {
        assertEquals(rangeString, characterRanges.rangeString());
    }
}
