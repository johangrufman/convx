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
