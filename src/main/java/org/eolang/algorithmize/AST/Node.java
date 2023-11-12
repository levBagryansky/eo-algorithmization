package org.eolang.algorithmize.AST;

import com.jcabi.xml.XML;
import java.util.ArrayList;
import java.util.List;

/**
 * Or children or value is null.
 */
public class Node {
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
        if (raw_children.size() == 0 && !"mem".equals(base)) {
            String value = xml.xpath("text()").get(0);
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
        if (this.value != null) {
            builder.append(
                String.format("base = %s, value = %s\n", this.base, this.value)
            );
        } else {
            assert this.children != null;
            for (final Node child: this.children) {
                builder.append(String.format(
                    "base = %s, children:\n", base
                ));
                builder.append(child.toString(3));
            }
        }
        return builder;
    }
}
