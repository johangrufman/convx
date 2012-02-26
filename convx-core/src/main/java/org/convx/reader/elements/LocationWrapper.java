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
package org.convx.reader.elements;

import org.convx.reader.FlatFileLocation;

import javax.xml.stream.Location;

/**
 * @author johan
 * @since 2012-02-12
 */
public class LocationWrapper implements Location {
    private FlatFileLocation flatFileLocation;

    public LocationWrapper(FlatFileLocation flatFileLocation) {
        this.flatFileLocation = flatFileLocation;
    }

    public int getLineNumber() {
        return flatFileLocation.getLineNumber();
    }

    public int getColumnNumber() {
        return flatFileLocation.getColumnNumber();
    }

    public int getCharacterOffset() {
        return flatFileLocation.getCharacterOffset();
    }

    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        return null;
    }
}
