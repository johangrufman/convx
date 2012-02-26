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
package org.convx.fsd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author johan
 * @since 2011-12-18
 */
public class SchemaBuilderTest {

    @Test
    public void unescapingCharacters() throws Exception {
        assertEquals('\n', (char)SchemaBuilder.fromEscapedStringToCharacter("\\n"));
        assertEquals('\t', (char)SchemaBuilder.fromEscapedStringToCharacter("\\t"));
        assertEquals(' ', (char)SchemaBuilder.fromEscapedStringToCharacter("\\u0020"));
        assertEquals('\\', (char)SchemaBuilder.fromEscapedStringToCharacter("\\\\"));
        assertEquals('\'', (char)SchemaBuilder.fromEscapedStringToCharacter("\\\'"));
    }
}
