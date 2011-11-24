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
        for (JAXBElement<? extends ElementBaseType> jaxbElement : schema.getElementBase()) {
            symbolTable.add(jaxbElement.getValue());
        }
        SchemaNode root = buildNode(schema.getRoot(), symbolTable);
        return new org.convx.schema.Schema(root);
    }

    private static SchemaNode buildNode(ElementBaseType elementBaseType, SymbolTable symbolTable) {

        SchemaNode schemaNode = null;

        if (elementBaseType.getRef() != null) {
            if (!symbolTable.containsElement(elementBaseType.getRef())) {
                throw new SchemaBuilderException("Unknown top level element: " + elementBaseType.getRef());
            }
            schemaNode = buildNode(symbolTable.get(elementBaseType.getRef()), symbolTable);
        } else {
            if (elementBaseType instanceof FixedLengthElementType) {
                schemaNode = buildFixedLengthNode((FixedLengthElementType) elementBaseType);
            }
            if (elementBaseType instanceof DelimitedElementType) {
                schemaNode = buildDelimitedNode((DelimitedElementType) elementBaseType);
            }
            if (elementBaseType instanceof ConstantElementType) {
                schemaNode = buildConstantNode((ConstantElementType) elementBaseType);
            }
            if (elementBaseType instanceof ElementType) {
                schemaNode = buildElementNode((ElementType) elementBaseType, symbolTable);
            }
        }
        schemaNode = wrapWithNamedNode(elementBaseType, schemaNode);

        schemaNode = wrapWithRepititionNode(elementBaseType, schemaNode);

        return schemaNode;

    }

    private static SchemaNode wrapWithRepititionNode(ElementBaseType elementBaseType, SchemaNode schemaNode) {
        int minOccurs = minOccurs(elementBaseType);
        int maxOccurs = maxOccurs(elementBaseType);
        if (minOccurs != 1 || maxOccurs != 1) {
            schemaNode = new RepetitionSchemaNode(schemaNode, minOccurs, maxOccurs);
        }
        return schemaNode;
    }

    private static SchemaNode wrapWithNamedNode(ElementBaseType elementBaseType, SchemaNode schemaNode) {
        if (elementBaseType.getName() != null) {
            schemaNode = new NamedSchemaNode(elementBaseType.getName(), schemaNode);
        }
        return schemaNode;
    }

    private static int maxOccurs(ElementBaseType elementBaseType) {
        int maxOccurs = 1;
        if (elementBaseType.getMaxOccurs() != null) {
            if (elementBaseType.getMaxOccurs().equals("unbounded")) {
                maxOccurs = RepetitionSchemaNode.UNBOUNDED;
            } else {
                maxOccurs = Integer.parseInt(elementBaseType.getMaxOccurs());
            }
        }
        return maxOccurs;
    }

    private static int minOccurs(ElementBaseType elementBaseType) {
        int minOccurs = 1;
        if (elementBaseType.getMinOccurs() != null) {
            minOccurs = elementBaseType.getMinOccurs();
        }
        return minOccurs;
    }

    private static SchemaNode buildFixedLengthNode(FixedLengthElementType fixedLengthElementType) {
        SchemaNode schemaNode;
        schemaNode = new FixedLengthSchemaNode(Integer.parseInt(fixedLengthElementType.getLength()));
        return schemaNode;
    }

    private static SchemaNode buildDelimitedNode(DelimitedElementType delimitedElementType) {
        SchemaNode schemaNode;
        List<Character> exceptions = new ArrayList<Character>();
        for (char e : CharacterUtil.unescapeCharacters(delimitedElementType.getExceptions()).toCharArray()) {
            exceptions.add(e);
        }
        schemaNode = new DelimitedSchemaNode(exceptions.toArray(new Character[exceptions.size()]));
        return schemaNode;
    }

    private static SchemaNode buildConstantNode(ConstantElementType constantElementType) {
        SchemaNode schemaNode;
        schemaNode = new ConstantSchemaNode(CharacterUtil.unescapeCharacters(constantElementType.getValue()));
        return schemaNode;
    }

    private static SchemaNode buildElementNode(ElementType elementType, SymbolTable symbolTable) {
        SchemaNode schemaNode;
        if (elementType.getSequence() != null) {
            ElementType.Sequence sequence = elementType.getSequence();
            SequenceSchemaNode.Builder builder = new SequenceSchemaNode.Builder();
            for (JAXBElement<? extends ElementBaseType> subElement : sequence.getElementBase()) {
                builder.add(buildNode(subElement.getValue(), symbolTable));
            }
            schemaNode = builder.build();
        } else {
            schemaNode = SequenceSchemaNode.sequence(buildNode(elementType.elementBase.getValue(), symbolTable))
                    .build();
        }
        return schemaNode;
    }

}
