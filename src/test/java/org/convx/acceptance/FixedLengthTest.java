package org.convx.acceptance;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.convx.schema.ConstantSchemaNode;
import org.convx.schema.FixedLengthSchemaNode;
import org.convx.schema.NamedSchemaNode;
import org.convx.schema.RepetitionSchemaNode;
import org.convx.schema.Schema;
import org.convx.schema.SchemaNode;
import org.convx.schema.SequenceSchemaNode;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import static junit.framework.Assert.assertEquals;


/**
 * Acceptance test for CSV-files.
 * @author johan
 * @since 2011-05-21
 */
public class FixedLengthTest extends AbstractAcceptanceTest {

    @Override
    protected String name() {
        return "fixed";
    }

    @Override
    protected Schema schema() {
        SchemaNode firstName = new NamedSchemaNode("firstName", new FixedLengthSchemaNode(15));
        SchemaNode lastName = new NamedSchemaNode("lastName", new FixedLengthSchemaNode(15));
        SchemaNode age = new NamedSchemaNode("age", new FixedLengthSchemaNode(2));
        SchemaNode eol = new ConstantSchemaNode("\n");
        SchemaNode person = new NamedSchemaNode("person", SequenceSchemaNode.sequence().add(firstName).add(lastName).add(age).add(eol).build());
        SchemaNode repeatedPerson = new RepetitionSchemaNode(person, 1, RepetitionSchemaNode.UNBOUNDED);
        SchemaNode root = new NamedSchemaNode("persons", SequenceSchemaNode.sequence().add(repeatedPerson).build());
        return new Schema(root);
    }

}
