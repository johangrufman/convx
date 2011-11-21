package org.convx.fsd;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            throw new RuntimeException(e);
        }
        Map<String, ElementBaseType> topLevelElements = new HashMap<String, ElementBaseType>();
        for (JAXBElement<? extends ElementBaseType> jaxbElement : schema.getElementBase()) {
            ElementBaseType topLevelElement = jaxbElement.getValue();
            topLevelElements.put(topLevelElement.getId(), topLevelElement);
        }
        SchemaNode root = buildNode(schema.getRoot(), topLevelElements);
        return new org.convx.schema.Schema(root);
    }

    private static SchemaNode buildNode(ElementBaseType elementBaseType, Map<String, ElementBaseType> topLevelElements) {

        SchemaNode schemaNode = null;

        if (elementBaseType.getRef() != null) {
            if (!topLevelElements.containsKey(elementBaseType.getRef())) {
                throw new RuntimeException("Unknown top level element: " + elementBaseType.getRef());
            }
            schemaNode = buildNode(topLevelElements.get(elementBaseType.getRef()), topLevelElements);
        } else {
            if (elementBaseType instanceof FixedLengthElementType) {
                FixedLengthElementType fixedLengthElementType = (FixedLengthElementType) elementBaseType;
                schemaNode = new FixedLengthSchemaNode(Integer.parseInt(fixedLengthElementType.getLength()));
            }
            if (elementBaseType instanceof DelimitedElementType) {
                DelimitedElementType delimitedElementType = (DelimitedElementType) elementBaseType;
                List<Character> exceptions = new ArrayList<Character>();
                for (char e : CharacterUtil.unescapeCharacters(delimitedElementType.getExceptions()).toCharArray()) {
                    exceptions.add(e);
                }
                schemaNode = new DelimitedSchemaNode(exceptions.toArray(new Character[exceptions.size()]));
            }
            if (elementBaseType instanceof ConstantElementType) {
                ConstantElementType constantElementType = (ConstantElementType) elementBaseType;
                schemaNode = new ConstantSchemaNode(CharacterUtil.unescapeCharacters(constantElementType.getValue()));
            }
            if (elementBaseType instanceof ElementType) {
                ElementType elementType = (ElementType) elementBaseType;
                if (elementType.getSequence() != null) {
                    ElementType.Sequence sequence = elementType.getSequence();
                    SequenceSchemaNode.Builder builder = new SequenceSchemaNode.Builder();
                    for (JAXBElement<? extends ElementBaseType> subElement : sequence.getElementBase()) {
                        builder.add(buildNode(subElement.getValue(), topLevelElements));
                    }
                    schemaNode = builder.build();
                } else {
                    schemaNode = SequenceSchemaNode.sequence(buildNode(elementType.elementBase.getValue(), topLevelElements))
                            .build();
                }
            }
        }
        if (elementBaseType.getName() != null) {
            schemaNode = new NamedSchemaNode(elementBaseType.getName(), schemaNode);
        }

        int minOccurs = 1;
        if (elementBaseType.getMinOccurs() != null) {
            minOccurs = elementBaseType.getMinOccurs();
        }
        int maxOccurs = 1;
        if (elementBaseType.getMaxOccurs() != null) {
            if (elementBaseType.getMaxOccurs().equals("unbounded")) {
                maxOccurs = RepetitionSchemaNode.UNBOUNDED;
            } else {
                maxOccurs = Integer.parseInt(elementBaseType.getMaxOccurs());
            }
        }
        if (minOccurs != 1 || maxOccurs != 1) {
            schemaNode = new RepetitionSchemaNode(schemaNode, minOccurs, maxOccurs);
        }

        return schemaNode;

    }
}
