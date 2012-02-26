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

import java.util.HashMap;
import java.util.Map;

/**
* @author johan
* @since 2011-11-24
*/
class SymbolTable {
    private Map<String, ElementBase> elements = new HashMap<String, ElementBase>();

    public void addElement(ElementBase element) {
        if (elements.containsKey(element.getId())) {
            throw new SchemaBuilderException("Symbol table already contains element with id: " + element.getId());
        }
        elements.put(element.getId(), element);
    }

    public ElementBase getElement(String id) {
        return elements.get(id);
    }

    public boolean containsElement(String id) {
        return elements.containsKey(id);
    }
}
