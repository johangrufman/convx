package org.convx.util;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author johan
 * @since 2011-10-31
 */
public class CollectionUtil {
    public static <T> Iterable<T> reverseOrder(final LinkedList<T> list) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                return list.descendingIterator();
            }
        };
    }
}
