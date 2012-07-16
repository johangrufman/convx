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
package org.convx.reader;

import com.ibm.icu.text.UnicodeSet;

import java.util.Collections;
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
    private Set<UnicodeSet> characterSets = new HashSet<UnicodeSet>();

    protected PrefixMatcher() {
    }

    public PrefixMatcher(UnicodeSet characterSet) {
        this.characterSets.add(characterSet);
    }

    public PrefixMatcher(String... prefixes) {
        Collections.addAll(this.prefixes, prefixes);
    }

    public boolean matches(String nextCharacters) {
        if (nextCharacters.length() == 0) {
            return false;
        }
        for (String prefix : prefixes) {
            if (nextCharacters.startsWith(prefix)) {
                return true;
            }
        }
        for (UnicodeSet characterSet : characterSets) {
            if (characterSet.contains(nextCharacters.charAt(0))) {
                return true;
            }
        }
        return false;
    }

    public PrefixMatcher combine(PrefixMatcher prefixMatcher) {
        if (this == ALL || prefixMatcher == ALL) {
            return ALL;
        }
        PrefixMatcher combinedPrefixMatcher = new PrefixMatcher();
        combinedPrefixMatcher.prefixes.addAll(prefixes);
        combinedPrefixMatcher.prefixes.addAll(prefixMatcher.prefixes);
        combinedPrefixMatcher.characterSets.addAll(characterSets);
        combinedPrefixMatcher.characterSets.addAll(prefixMatcher.characterSets);
        return combinedPrefixMatcher;
    }

    private static class AllMatcher extends PrefixMatcher {
        @Override
        public boolean matches(String nextCharacters) {
            return true;
        }

    }

}
