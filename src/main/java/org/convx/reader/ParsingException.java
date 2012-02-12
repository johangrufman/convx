package org.convx.reader;

import org.convx.reader.elements.LocationWrapper;

import javax.xml.stream.XMLStreamException;

/**
 * @author johan
 * @since 2012-02-11
 */
public class ParsingException extends XMLStreamException {
    public ParsingException(String message, FlatFileLocation location) {
        super(message, new LocationWrapper(location));
    }
}
