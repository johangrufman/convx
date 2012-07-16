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

import org.convx.fsd.SchemaBuilder;
import org.convx.schema.Schema;
import org.convx.util.XmlUtil;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import java.io.FileReader;
import java.io.IOException;

import static org.convx.examples.ResourceUtil.getResource;

/**
 * @author johan
 * @since 2012-07-08
 */
public class PnlParser {
    public void parsePnl() throws IOException, XMLStreamException {
        Schema schema = SchemaBuilder.build(getResource("pnl.fsd"));
        XMLEventReader parser = schema.parser(new FileReader(getResource("pnl.txt")));
        System.out.println(XmlUtil.serialize(parser));
    }

    public static void main(String args[]) throws XMLStreamException, IOException {
        new PnlParser().parsePnl();
    }
}
