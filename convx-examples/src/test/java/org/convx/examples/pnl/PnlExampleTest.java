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
package org.convx.examples.pnl;

import org.junit.Before;
import org.junit.Test;

import static org.convx.examples.ResourceUtil.getResource;
import static org.convx.test.TestUtil.readFile;
import static org.junit.Assert.assertEquals;

/**
 * @author johan
 * @since 2012-07-20
 */
public class PnlExampleTest {

    private PnlExample pnlExample;

    @Before
    public void setUp() throws Exception {
        pnlExample = new PnlExample();
    }

    @Test
    public void testParsePnl() throws Exception {
        assertEquals(readFile(getResource("pnl/pnl.xml")), pnlExample.parsePnl());
    }

    @Test
    public void testWritePnl() throws Exception {
        assertEquals(readFile(getResource("pnl/pnl.txt")), pnlExample.writePnl());
    }
}
