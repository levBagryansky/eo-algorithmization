package org.eolang.algorithmize.AST;

import com.jcabi.xml.XML;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Or children or value is null.
 */
public class Node {

    public Variable toVar(final List<Variable> accumulator) {
        Variable ret = null;
        if (this.children == null || this.children.size() == 0) {
            if ("org.eolang.bytes".equals(this.base)) {
                ret = Variable.fromQQBytes(this);
            } else {
                throw new IllegalArgumentException("node.base = " + this.base);
            }
        }
        List<Variable> vars = this.children.stream().map(node -> {
            Variable var = node.toVar(accumulator);
            //System.out.println(var);
            return var;
        }).collect(Collectors.toList());
        switch (this.base) {
            case ".plus":
                ret = Variable.fromPlus(vars);
                break;
            case "org.eolang.int":
                ret = Variable.fromAsInt(vars);
                break;
        }
        accumulator.add(ret);
        if (ret == null) {
            throw new IllegalArgumentException("node.base = " + this.base);
        }
        return ret;
    }

    List<Node> children;
    String base;
    String value;

    public Node(List<Node> children, String base, String value) {
        this.children = children;
        this.base = base;
        this.value = value;
    }

    public static Node xml2Node(final XML xml) {
        final String base = xml.xpath("@base").get(0);
        List<XML> raw_children = xml.nodes("o");
        if (raw_children.size() == 0) {
            String value;

            if ("mem".equals(base)) {
                value = null;
            }
            else {
                value = xml.xpath("text()").get(0);
            }
            return new Node(new ArrayList<>(), base, value);
        } else {
            final List<Node> children = new ArrayList<>();
            for (final XML item: raw_children) {
                children.add(xml2Node(item));
            }
            return new Node(children, base, null);
        }
    }

    public StringBuilder toString(int indentation) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indentation; i++) {
            builder.append(" ");
        }
        builder.append(
            String.format("base = %s, value = %s, ", this.base, this.value)
        );
        if (children.size() != 0){
            builder.append(
                "children:\n"
            );
            for (final Node child: this.children) {
                builder.append(child.toString(indentation + 3));
            }
        }
        else {
            builder.append("\n");
        }
        return builder;
    }
}
