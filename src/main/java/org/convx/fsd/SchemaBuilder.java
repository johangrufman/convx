package org.convx.fsd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.convx.schema.ConstantSchemaNode;
import org.convx.schema.DelimitedSchemaNode;
import org.convx.schema.FixedLengthSchemaNode;
import org.convx.schema.NamedSchemaNode;
import org.convx.schema.RepetitionSchemaNode;
import org.convx.schema.SchemaNode;
import org.convx.schema.SequenceSchemaNode;
import org.convx.util.CharacterUtil;

/**
 * @author johan
 * @since 2011-11-13
 */
public class SchemaBuilder {


    public static org.convx.schema.Schema build(File schemaFile) {
        Schema schema;
        try {
            JAXBContext jc = JAXBContext.newInstance("org.convx.fsd");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            schema = (Schema) unmarshaller.unmarshal(schemaFile);
        } catch (JAXBException e) {
            throw new SchemaBuilderException(e);
        }
        SymbolTable symbolTable = new SymbolTable();
        for (JAXBElement<? extends ElementBase> jaxbElement : schema.getElementBase()) {
            symbolTable.add(jaxbElement.getValue());
        }
        SchemaNode root = buildNode(schema.getRoot(), symbolTable);
        return new org.convx.schema.Schema(root);
    }

    private static SchemaNode buildNode(ElementBase elementBase, SymbolTable symbolTable) {

        SchemaNode schemaNode = null;


        if (elementBase.getRef() != null) {
            if (!symbolTable.containsElement(elementBase.getRef())) {
                throw new SchemaBuilderException("Unknown top level element: " + elementBase.getRef());
            }
            schemaNode = buildNode(symbolTable.get(elementBase.getRef()), symbolTable);
        } else {
            if (elementBase instanceof FixedField) {
                schemaNode = buildFixedLengthNode((FixedField) elementBase);
            }
            if (elementBase instanceof DelimitedField) {
                schemaNode = buildDelimitedNode((DelimitedField) elementBase);
            }
            if (elementBase instanceof Constant) {
                schemaNode = buildConstantNode((Constant) elementBase);
            }
            if (elementBase instanceof Sequence) {
                schemaNode = buildElementNode((Sequence) elementBase, symbolTable);
            }
        }
        schemaNode = wrapWithNamedNode(elementBase, schemaNode);

        schemaNode = wrapWithRepititionNode(elementBase, schemaNode);

        return schemaNode;

    }

    private static SchemaNode wrapWithRepititionNode(ElementBase elementBase, SchemaNode schemaNode) {
        int minOccurs = minOccurs(elementBase);
        int maxOccurs = maxOccurs(elementBase);
        if (minOccurs != 1 || maxOccurs != 1) {
            schemaNode = new RepetitionSchemaNode(schemaNode, minOccurs, maxOccurs);
        }
        return schemaNode;
    }

    private static SchemaNode wrapWithNamedNode(ElementBase elementBase, SchemaNode schemaNode) {
        if (elementBase.getName() != null) {
            schemaNode = new NamedSchemaNode(elementBase.getName(), schemaNode);
        }
        return schemaNode;
    }

    private static int maxOccurs(ElementBase elementBase) {
        int maxOccurs = 1;
        if (elementBase.getMaxOccurs() != null) {
            if (elementBase.getMaxOccurs().equals("unbounded")) {
                maxOccurs = RepetitionSchemaNode.UNBOUNDED;
            } else {
                maxOccurs = Integer.parseInt(elementBase.getMaxOccurs());
            }
        }
        return maxOccurs;
    }

    private static int minOccurs(ElementBase elementBase) {
        int minOccurs = 1;
        if (elementBase.getMinOccurs() != null) {
            minOccurs = elementBase.getMinOccurs();
        }
        return minOccurs;
    }

    private static SchemaNode buildFixedLengthNode(FixedField fixedLengthElement) {
        return new FixedLengthSchemaNode(Integer.parseInt(fixedLengthElement.getLength()));
    }

    private static SchemaNode buildDelimitedNode(DelimitedField delimitedElement) {
        List<Character> exceptions = new ArrayList<Character>();
        for (char e : CharacterUtil.unescapeCharacters(delimitedElement.getExceptions()).toCharArray()) {
            exceptions.add(e);
        }
        boolean doTrim = delimitedElement.isTrim() != null ? delimitedElement.isTrim() : true;
        return new DelimitedSchemaNode(doTrim, exceptions.toArray(new Character[exceptions.size()]));
    }

    private static SchemaNode buildConstantNode(Constant constantElement) {
        return new ConstantSchemaNode(CharacterUtil.unescapeCharacters(constantElement.getValue()));
    }

    private static SchemaNode buildElementNode(Sequence sequenceElement, SymbolTable symbolTable) {
        SequenceSchemaNode.Builder builder = new SequenceSchemaNode.Builder();
        for (JAXBElement<? extends ElementBase> subElement : sequenceElement.getElementBase()) {
            builder.add(buildNode(subElement.getValue(), symbolTable));
        }
        return builder.build();
    }

}
