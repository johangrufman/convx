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

import org.convx.Schema;

/**
 * @author Johan
 * @since 2012-08-29
 */
public class SchemaImpl extends Schema {
    private SchemaNode root;

    public SchemaImpl(SchemaNode root) {
        this.root = root;
    }

    public SchemaNode root() {
        return root;
    }

}
