package org.eolang.algorithmize;

public abstract class EOObject {

    protected final String locator;

    protected final String XMLpath;

    protected final boolean exclusive;

    protected EOObject(String locator, String xmLpath, boolean exclusive) {
        this.locator = locator;
        XMLpath = xmLpath;
        this.exclusive = exclusive;
    }
}
