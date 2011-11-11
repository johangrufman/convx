package org.convx.acceptance;


import org.convx.schema.ConstantSchemaNode;
import org.convx.schema.DelimitedSchemaNode;
import org.convx.schema.NamedSchemaNode;
import org.convx.schema.RepetitionSchemaNode;
import org.convx.schema.Schema;
import org.convx.schema.SchemaNode;
import org.convx.schema.SequenceSchemaNode;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * Acceptance test for CSV-files.
 *
 * @author johan
 * @since 2011-05-21
 */
public class CsvTest extends AbstractAcceptanceTest {

    @Override
    protected Schema schema() {
        char comma = ',';
        DelimitedSchemaNode field = new DelimitedSchemaNode(comma, '\n', '\r');
        SchemaNode firstName = new NamedSchemaNode("firstName", field);
        SchemaNode lastName = new NamedSchemaNode("lastName", field);
        SchemaNode age = new NamedSchemaNode("age", field);
        SchemaNode eol = new ConstantSchemaNode("\n");
        SequenceSchemaNode personSequence = SequenceSchemaNode.sequence(firstName, lastName, age).separatedBy(comma).build();
        SchemaNode person = new NamedSchemaNode("person",
                SequenceSchemaNode.sequence(personSequence, eol).build());
        SchemaNode repeatedPerson = new RepetitionSchemaNode(person, 1, RepetitionSchemaNode.UNBOUNDED);
        SchemaNode root = new NamedSchemaNode("persons", SequenceSchemaNode.sequence().add(repeatedPerson).build());
        return new Schema(root);
    }


    protected String name() {
        return "csv";
    }
}
