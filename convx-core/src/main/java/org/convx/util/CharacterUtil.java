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
package org.convx.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author johan
 * @since 2011-11-19
 */
public class CharacterUtil {
    private static final Map<Character, String> escapedCharacters = new HashMap<Character, String>();
    static {
        escapedCharacters.put('\n', "\\n");
        escapedCharacters.put('\r', "\\r");
    }

    private CharacterUtil() {}

    public static String escapeCharacter(char c) {
        if (escapedCharacters.containsKey(c)) {
            return escapedCharacters.get(c);
        }
        return String.valueOf(c);
    }

    public static String unescapeCharacters(String string) {
        for (Character character : escapedCharacters.keySet()) {
            string = string.replace(escapedCharacters.get(character), String.valueOf(character));
        }
        return string;
    }

    public static String escapeCharacters(String string) {
        for (Character character : escapedCharacters.keySet()) {
            string = string.replace(String.valueOf(character), escapedCharacters.get(character));
        }
        return string;
    }
}
