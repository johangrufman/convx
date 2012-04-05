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

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @author johan
 * @since 2012-04-05
 */
public class DateFormat implements Format {
    private final static DateTimeFormatter isoFormatter = ISODateTimeFormat.date();

    private final DateTimeFormatter formatter;

    public DateFormat(String format) {
        formatter = DateTimeFormat.forPattern(format);
    }

    @Override
    public String parse(String text) {
        return isoFormatter.print(formatter.parseLocalDate(text));
    }

    @Override
    public String write(String xmlText) {
        return formatter.print(isoFormatter.parseLocalDate(xmlText));
    }
}
