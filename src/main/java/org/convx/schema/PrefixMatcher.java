package org.convx.schema;

import java.util.HashSet;
import java.util.Set;

/**
 * @author johan
 * @since 2011-06-29
 */
public class PrefixMatcher {
    public static final PrefixMatcher NONE = new PrefixMatcher();
    public static final PrefixMatcher ALL = new AllMatcher();

    private Set<String> prefixes = new HashSet<String>();

    protected PrefixMatcher() {}

    private PrefixMatcher(Set<String> prefixSet1, Set<String> prefixSet2) {
        this.prefixes.addAll(prefixSet1);
        this.prefixes.addAll(prefixSet2);
    }

    public PrefixMatcher(String... prefixes) {
        for (String prefix : prefixes) {
            this.prefixes.add(prefix);
        }
    }

    public boolean matches(String nextCharacters) {
        for (String prefix : prefixes) {
            if (nextCharacters.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public PrefixMatcher combine(PrefixMatcher prefixMatcher) {
        if (this == ALL || prefixMatcher == ALL) {
            return ALL;
        }
        return new PrefixMatcher(this.prefixes, prefixMatcher.prefixes);
    }

    private static class AllMatcher extends PrefixMatcher {
        @Override
        public boolean matches(String nextCharacters) {
            return true;
        }

    }

}
