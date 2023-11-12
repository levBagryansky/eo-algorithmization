package org.eolang.algorithmize;

import com.jcabi.xml.XML;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AST_XML {
    private final Collection<EOObject> external;
    private final XML xml;

    public AST_XML(Collection<EOObject> external, final XML document) {
        this.external = external;
        this.xml = document;
    }

    public Expressions toControlFlow() {
        final List<String> expressions = new ArrayList<>();
        for(final XML item: this.xml.nodes("o")) {
            //System.out.println();
            //System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
            //System.out.println(item);
            if (".write".equalsIgnoreCase(item.xpath("@base").get(0))) {
                StringBuilder str = new StringBuilder(item.xpath("o/attribute(base)").get(0));
                str.append(" = ");
                XML left = item.nodes("o").get(1).nodes("o").get(0);
                //System.out.println("left = " + left + "\n");
                String operation = item.nodes("o").get(1).xpath("@base").get(0);
                //System.out.println("operation = " + operation + "\n");
                XML right = item.nodes("o").get(1).nodes("o").get(1);
                //System.out.println("right = " + right + "\n");
                expressions.add(makeInstruction(operation, left, right));
            }
        }
        return new Expressions(this.external, expressions);
    }

    private static String makeInstruction(final String operation, final XML left, final XML right) {
//        if (!"org.eolang.int".equals(left.xpath("@base").get(0))) {
//            throw new IllegalArgumentException(
//                String.format(
//                "left base = %s", left.xpath("@base").get(0)
//                )
//            );
//        }
//        if ("org.eolang.int".equals(right.xpath("@base").get(0))) {
//            throw new IllegalArgumentException(
//                String.format(
//                    "right base = %s", right.xpath("@base").get(0)
//                )
//            );
//        }
        String int1 = "mem";
        String int2 = XML2int(right);
        String ret = "NOTHING";
        switch (operation) {
            case ".plus":
                ret = String.format("%s=%s+%s", "mem", int1, int2);
                break;
            case ".minus":
                ret = String.format("%s=%s-%s", "mem", int1, int2);
                break;
        }
        //System.out.println("ret: " + ret);
        return ret;
    }

    private static String XML2int (final XML operand){
        if (!"int".equals(operand.xpath("@base").get(0))) {
            //return operand.xpath(".").get(0);
            return "2";
        } else {
            throw new IllegalArgumentException(
                String.format(
                    "%s data format is not implemented yet",
                    operand.xpath("@base").get(0)
                )
            );
        }
    }

    /**
     * Makes a text from Hexed text.
     * @param txt Hexed chars separated by backspace.
     * @return Normal text.
     */
    private static String unhex(final String txt) {
        final StringBuilder hex = new StringBuilder(txt.length());
        for (final char chr : txt.toCharArray()) {
            if (chr == ' ') {
                continue;
            }
            hex.append(chr);
        }
        final String result;
        try {
            final byte[] bytes = Hex.decodeHex(String.valueOf(hex).toCharArray());
            result = new String(bytes, "UTF-8");
        } catch (final DecoderException | UnsupportedEncodingException exception) {
            throw new IllegalArgumentException(
                String.format("Invalid String %s, cannot unhex", txt),
                exception
            );
        }
        return result;
    }

}
