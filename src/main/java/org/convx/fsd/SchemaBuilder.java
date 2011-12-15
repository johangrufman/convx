package org.convx.fsd;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringEscapeUtils;
import org.convx.schema.ConstantSchemaNode;
import org.convx.schema.FieldSchemaNode;
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
            symbolTable.addElement(jaxbElement.getValue());
        }
        for (CharacterSet characterSet : schema.getCharacterSet()) {
            symbolTable.addCharacterSet(characterSet);
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
            schemaNode = buildNode(symbolTable.getElement(elementBase.getRef()), symbolTable);
        } else {
            if (elementBase instanceof Field) {
                schemaNode = buildFieldNode((Field) elementBase, symbolTable);
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


    private static SchemaNode buildFieldNode(Field elementBase, SymbolTable symbolTable) {
        boolean doTrim = elementBase.isTrim() != null ? elementBase.isTrim() : true;
        return new FieldSchemaNode(doTrim,
                buildCharacterSet(symbolTable.getCharacterSet(elementBase.getCharacterSet()), symbolTable),
                elementBase.getLength());
    }

    private static org.convx.characters.CharacterSet buildCharacterSet(CharacterSet characterSet, SymbolTable symbolTable) {
        org.convx.characters.CharacterSet.Builder builder = org.convx.characters.CharacterSet.empty();
        for (IncludeExclude include : characterSet.getInclude()) {
            if (include.getChar() != null && include.getChar().length() > 0) {
                builder.add(StringEscapeUtils.unescapeJava(include.getChar()).charAt(0));
            } else if (include.getControlChar() != null) {
                builder.add(toCharacter(include.getControlChar()));
            } else if (include.getSet() != null) {
                builder.add(buildCharacterSet(symbolTable.getCharacterSet(include.getSet()), symbolTable));
            } else if (include.getPredefined() != null && include.getPredefined() == Predefined.ALL) {
                builder.addAll();
            }
        }
        for (IncludeExclude exclude : characterSet.getExclude()) {
            if (exclude.getChar() != null && exclude.getChar().length() > 0) {
                builder.remove(StringEscapeUtils.unescapeJava(exclude.getChar()).charAt(0));
            } else if (exclude.getControlChar() != null) {
                builder.remove(toCharacter(exclude.getControlChar()));
            } else if (exclude.getSet() != null) {
                builder.remove(buildCharacterSet(symbolTable.getCharacterSet(exclude.getSet()), symbolTable));
            }
        }
        return builder.build();
    }

    private static char toCharacter(ControlChar controlChar) {
        switch (controlChar) {
            case CARRIAGE_RETURN:
                return '\r';
            case LINE_FEED:
                return '\n';
            case TAB:
                return '\t';
        }
        throw new RuntimeException("Unknown control character: " + controlChar);
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
