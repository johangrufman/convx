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
package org.convx.acceptance;

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

/**
 * @author johan
 * @since 2012-02-11
 */
public class FixedTest extends AcceptanceTest {
    public FixedTest() throws SAXException, JAXBException, TransformerException {
        super("fixed");
    }

    @Test
    public void testParsingNonCanonicalFile1() throws Exception {
        convertFlatFilesToXmlAndValidate("nc_fixed1.txt");
    }
}
