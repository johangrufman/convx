package org.convx.writer;

import java.io.ByteArrayInputStream;
import java.util.StringTokenizer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author johan
 * @since 2011-09-17
 */
public class Writer2Test {

    @Test
    public void parseCorrectSingleElementDocument() throws XMLStreamException {
        parse("<foo></foo>", "foo ( )");
    }

    @Test
    public void parseAnotherCorrectSingleElementDocument() throws XMLStreamException {
        parse("<bar></bar>", "bar ( )");
    }

    @Test(expected = RuntimeException.class)
    public void parseIncorrectSingleElementDocument() throws XMLStreamException {
        parse("<bar></bar>", "foo ( )");
    }

    @Test
    public void parseNestedElements() throws XMLStreamException {
        parse("<foo><bar></bar></foo>", "foo ( bar ( ) )");
    }

    @Test(expected = RuntimeException.class)
    public void parseIncorrectNestedElements() throws XMLStreamException {
        parse("<foo><baz></baz></foo>", "foo ( bar ( ) )");
    }

    @Test
    public void parseRepeatedElements() throws XMLStreamException {
        parse("<foo><bar></bar><bar></bar></foo>", "foo ( *bar ( ) )");
    }

    @Test
    public void parseDifferentRepeatedElements() throws XMLStreamException {
        parse("<foo><bar></bar><bar></bar><baz></baz></foo>", "foo ( *bar ( ) *baz ( ) )");
    }

    @Test(expected = RuntimeException.class)
    @Ignore
    public void parseDifferentIncorrectRepeatedElements() throws XMLStreamException {
        parse("<foo><bar></bar><bar></bar><baz></baz></foo>", "foo ( *bar ( ) *qux ( ) )");
    }

    private void parse(String xmlInput, String schema) throws XMLStreamException {
        XMLEventReader xmlFileReader = XMLInputFactory.newFactory().createXMLEventReader(new ByteArrayInputStream(xmlInput.getBytes()));
        Writer2 flatFileWriter = new Writer2(parseSchema(schema));
        flatFileWriter.add(xmlFileReader);
    }

    private WriterNode parseSchema(String schema) {
        StringTokenizer tokenizer = new StringTokenizer(schema);
        return parseSchema(tokenizer.nextToken(), tokenizer);
    }

    private WriterNode parseSchema(String name, StringTokenizer stringTokenizer) {
        WriterNode writerNodeToReturn;
        SequenceWriterNode sequenceNode;
        if (name.startsWith("*")) {
            sequenceNode = new SequenceWriterNode();
            writerNodeToReturn = new RepeatedWriterNode(new NamedWriterNode(name.substring(1), sequenceNode));
        } else {
            sequenceNode = new SequenceWriterNode();
            writerNodeToReturn = new NamedWriterNode(name, sequenceNode);
        }
        String openingParenthesis  = stringTokenizer.nextToken();
        assert openingParenthesis.equals("(");
        String nextToken = stringTokenizer.nextToken();
        while (!nextToken.equals(")")) {
            sequenceNode.addSubNode(parseSchema(nextToken, stringTokenizer));
            nextToken = stringTokenizer.nextToken();
        }
        return writerNodeToReturn;
    }
}
