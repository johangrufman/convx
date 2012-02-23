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
package org.convx.schema;

import org.convx.characters.Char;
import org.convx.characters.CharacterSet;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * @author johan
 * @since 2011-12-07
 */
public class CharacterSetTest {

    @Test
    public void givenEmptySetWhenOneCharacterAddedThenSetShouldContainThatCharacter() throws Exception {
        CharacterSet setWithOneCharacter = CharacterSet.empty()
                .add(Char.valueOf('A'))
                .build();
        assertTrue(setWithOneCharacter.contains('A'));
    }

    @Test
    public void aCompleteCharacterSetContainsEveryCharacter() throws Exception {
        CharacterSet completeSet = CharacterSet.complete().build();
        assertContains(completeSet, 'A', 'Ö', '&', '\n', '\u1234');
    }

    @Test
    public void aCompleteSetWithOneCharacterRemovedShouldNoLongerContainThatCharacter() throws Exception {
        CharacterSet allCharactersButA = CharacterSet.complete().remove(Char.valueOf('A')).build();
        assertContainsNot(allCharactersButA, 'A');
        assertContains(allCharactersButA, 'B');
    }

    @Test
    public void addingCharacterRange() throws Exception {
        CharacterSet range = CharacterSet.empty().addRange(Char.valueOf('b'), Char.valueOf('d')).build();
        assertContains(range, 'b', 'c', 'd');
        assertContainsNot(range, 'a', 'e');
    }

    private void assertContains(CharacterSet characterSet, char... chars) {
        for (char c : chars) {
            Assert.assertTrue("Should contain " + c, characterSet.contains(c));
        }
    }

    private void assertContainsNot(CharacterSet characterSet, char... chars) {
        for (char c : chars) {
            Assert.assertFalse("Should not contain " + c, characterSet.contains(c));
        }
    }
}
