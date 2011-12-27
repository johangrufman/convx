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
                elementBase.getLength(),
                StringEscapeUtils.unescapeJava(elementBase.getDefaultOutput()));
    }

    private static org.convx.characters.CharacterSet buildCharacterSet(CharacterSet characterSet, SymbolTable symbolTable) {
        org.convx.characters.CharacterSet.Builder builder = org.convx.characters.CharacterSet.empty();
        for (JAXBElement<IncludeExclude> includeExcludeElement : characterSet.getIncludeExclude()) {
            IncludeExclude includeExclude = includeExcludeElement.getValue();
            boolean include = includeExcludeElement.getName().getLocalPart().equals("include");
            if (includeExclude.getChar() != null && includeExclude.getChar().length() > 0) {
                char character = fromEscapedStringToCharacter(includeExclude.getChar());
                modifyWithCharacter(builder, include, character);
            } else if (includeExclude.getControlChar() != null) {
                char character = fromControlCharEnumToCharacter(includeExclude.getControlChar());
                modifyWithCharacter(builder, include, character);
            } else if (includeExclude.getSet() != null) {
                modifyWithSet(builder, include,
                        buildCharacterSet(symbolTable.getCharacterSet(includeExclude.getSet()), symbolTable));
            } else if (includeExclude.getCharacterClass() != null) {
                modifyWithCharacterClass(builder, include, includeExclude.getCharacterClass());
            }
        }

        return builder.build();
    }

    private static void modifyWithSet(org.convx.characters.CharacterSet.Builder builder, boolean include,
                                      org.convx.characters.CharacterSet characterSet) {
        if (include) {
            builder.add(characterSet);
        } else {
            builder.remove(characterSet);
        }
    }

    private static void modifyWithCharacter(org.convx.characters.CharacterSet.Builder builder, boolean include, char character) {
        if (include) {
            builder.add(character);
        } else {
            builder.remove(character);
        }
    }

    private static void modifyWithCharacterClass(org.convx.characters.CharacterSet.Builder builder, boolean include,
                                                 CharacterClass characterClass) {
        char from, to;
        switch (characterClass) {
            case ALL:
                from = Character.MIN_VALUE;
                to = Character.MAX_VALUE;
                break;
            case DIGIT:
                from = '0';
                to = '9';
                break;
            default:
                throw new RuntimeException("Unexpected character class: " + characterClass);
        }
        if (include) {
            builder.addRange(from, to);
        } else {
            builder.removeRange(from, to);
        }
    }


    static char fromEscapedStringToCharacter(String escapedString) {
        return StringEscapeUtils.unescapeJava(escapedString).charAt(0);
    }

    private static char fromControlCharEnumToCharacter(ControlChar controlChar) {
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
