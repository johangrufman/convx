/*
    Copyright (C) 2012 Johan Grufman

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package org.convx.examples.csv;

import java.io.File;
import java.net.URL;

/**
 * @author johan
 * @since 2012-02-29
 */
public class ResourceUtil {
    public static File getResource(String resource) {
        URL resourceUrl = ResourceUtil.class.getResource("/" + resource);
        if (resourceUrl != null) {
            return new File(resourceUrl.getFile());
        } else {
            throw new RuntimeException("Resource " + resource + " not found");
        }
    }
}
