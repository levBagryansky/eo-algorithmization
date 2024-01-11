package org.eolang.algorithmize.AST;

import org.eolang.algorithmize.EOObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AST.
 */
public class AST {

    public final List<Variable> vars = new ArrayList<>();

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

    public AST(final Node node) {
        this.head = node;
        this.external = new ArrayList<>();
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

    public String rustCode() {
        final List<Variable> res = this.getVars();
        StringBuilder builder = new StringBuilder("use eo::Portal;\n" +
            "use eo::eo_enum::EO;\n" +
            "use eo::eo_enum::EO::{EOInt};\n" +
            "use byteorder::{BigEndian, ReadBytesExt};\n" +
            "\n" +
            "pub fn foo(env: &mut Portal) -> Option<EO> {\n");
        res.forEach(var -> builder.append("   ").append(var.toString()).append("\n"));
        builder.append(String.format("    return Some(EOInt(%s));\n}\n", res.get(res.size()-1).name));
        return builder.toString();
    }

    public boolean algorithmizable() {
        if (this.external == null || this.external.size() == 0) {
            System.out.println("DEPTH = " + this.head.depth());
            return this.head.depth() > 10;
        }
        return false;
    }
}
