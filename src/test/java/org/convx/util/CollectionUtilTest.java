package org.convx.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author johan
 * @since 2011-11-11
 */
public class CollectionUtilTest {

    @Test
    public void reverseOrderShouldIterateOverSameListBackwards() throws Exception {
        LinkedList<Integer> originalList = new LinkedList<Integer>(Arrays.asList(1, 2, 3));
        Iterator<Integer> iterator = CollectionUtil.reverseOrder(originalList).iterator();
        assertEquals(3, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertFalse(iterator.hasNext());
    }
}
