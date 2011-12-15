package org.convx.schema;

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
                .add('A')
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
        CharacterSet allCharactersButA = CharacterSet.complete().remove('A').build();
        assertContainsNot(allCharactersButA, 'A');
        assertContains(allCharactersButA, 'B');
    }

    @Test
    public void addingCharacterRange() throws Exception {
        CharacterSet range = CharacterSet.empty().addRange('b', 'd').build();
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
