package org.eolang.algorithmize;

import java.util.Collection;
import java.util.List;

/**
 * Structure to represent rust node in xmir.
 */
public class RustInsert {

    public final String locator;

    public final String xml;

    public final String content;

    public Collection<String> dependencies = List.of(
        "byteorder:1.4.3"
    );

    public RustInsert(final String content, String locator, final String xmlpath) {
        this.content = content;
        this.locator = locator;
        this.xml = xmlpath;
    }
}
