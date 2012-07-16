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

import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;


/**
 * @author johan
 * @since 2012-07-08
 */
public class CsvParserTest {
    private static final String EXPECTED_OUTPUT = "Name: Timon Mckinney\n" +
            "Name: Basil Mitchell\n" +
            "Name: Dane Bishop\n" +
            "Name: Zachery Watkins\n";

    @Test
    public void csvParsing() throws Exception {
        StringWriter stringWriter = new StringWriter();
        new CsvParser().readCsv(new PrintWriter(stringWriter));
        assertEquals(EXPECTED_OUTPUT, stringWriter.toString());
    }
}
