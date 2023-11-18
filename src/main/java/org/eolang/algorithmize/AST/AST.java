package org.eolang.algorithmize.AST;

import org.eolang.algorithmize.EOObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AST.
 */
public class AST {

    public static AtomicInteger nextId = new AtomicInteger(0);

    final private Node head;

    /**
     * Basically there are variables to be found and dataized in rust insert.
     *  It can be any eo object that is constant, dataizable to bytearray and
     *  represented as bytes, int, float or string.
     */
    private final List<EOObject> external;

    public AST(final Node node, final List<EOObject> external) {
        this.head = node;
        this.external = external;
    }

    public List<Variable> getVars() {
        final List<Variable> res = new ArrayList<>();
        head.toVar(res);
        return res;
    }

    public void dumpVars() {
        final List<Variable> res = this.getVars();
        res.forEach(System.out::println);
    }
}
