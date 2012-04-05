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
package org.convx.format;

/**
 * @author johan
 * @since 2012-04-05
 */
public class IdentityFormat implements Format {
    public static IdentityFormat IDENTITY_FORMAT = new IdentityFormat();

    private IdentityFormat() {
    }

    @Override
    public String parse(String text) {
        return text;
    }

    @Override
    public String write(String xmlText) {
        return xmlText;
    }
}
