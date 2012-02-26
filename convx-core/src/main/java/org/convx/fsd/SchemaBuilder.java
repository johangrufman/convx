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
package org.convx.fsd;

import com.ibm.icu.text.UnicodeSet;
import org.apache.commons.lang3.StringEscapeUtils;
import org.convx.schema.*;
import org.convx.util.CharacterUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

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
                schemaNode = buildFieldNode((Field) elementBase);
            }
            if (elementBase instanceof Constant) {
                schemaNode = buildConstantNode((Constant) elementBase);
            }
            if (elementBase instanceof Sequence) {
                schemaNode = buildSequenceNode((Sequence) elementBase, symbolTable);
            }
        }
        schemaNode = wrapWithNamedNode(elementBase, schemaNode);

        schemaNode = wrapWithRepetitionNode(elementBase, schemaNode);

        return schemaNode;

    }

    private static SchemaNode wrapWithRepetitionNode(ElementBase elementBase, SchemaNode schemaNode) {
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


    private static SchemaNode buildFieldNode(Field elementBase) {
        boolean doTrim = elementBase.isTrim() != null ? elementBase.isTrim() : true;
        String characterSet = elementBase.getCharacterSet() == null ? "^" : elementBase.getCharacterSet();
        return new FieldSchemaNode(doTrim,
                new UnicodeSet("[" + characterSet + "]"),
                elementBase.getLength(),
                StringEscapeUtils.unescapeJava(elementBase.getDefaultOutput()),
                fromEscapedStringToCharacter(elementBase.getQuoteCharacter()));
    }

    static Character fromEscapedStringToCharacter(String escapedString) {
        if (escapedString == null) {
            return null;
        }
        return StringEscapeUtils.unescapeJava(escapedString).charAt(0);
    }

    private static SchemaNode buildConstantNode(Constant constantElement) {
        return new ConstantSchemaNode(CharacterUtil.unescapeCharacters(constantElement.getValue()));
    }

    private static SchemaNode buildSequenceNode(Sequence sequenceElement, SymbolTable symbolTable) {
        SequenceSchemaNode.Builder builder;
        if (sequenceElement instanceof Line) {
            builder = SequenceSchemaNode.lineSequence();
        } else {
            builder = SequenceSchemaNode.sequence();
        }
        for (JAXBElement<? extends ElementBase> subElement : sequenceElement.getElementBase()) {
            builder.add(buildNode(subElement.getValue(), symbolTable));
        }
        if (sequenceElement.getSeparatedBy() != null) {
            builder.setSeparatedBy(fromEscapedStringToCharacter(sequenceElement.getSeparatedBy()));
        }
        return builder.build();
    }

}
