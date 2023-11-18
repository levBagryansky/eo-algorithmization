package org.eolang.algorithmize.AST;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// let var1 = 1 + 1;
public class Variable {

    public final String name;
    public final String definition;
    public final String type;

    public Variable(final String name, final String definition, String type) {
        this.name = name;
        this.definition = definition;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format(
            " let %s: %s = %s;",
            this.name, this.type, this.definition
        );
    }

    public static Variable fromAsInt(List<Variable> args){
        return new Variable(
            "var_" + AST.nextId.getAndIncrement(),
            String.format("%s.as_slice().read_i64::<BigEndian>().unwrap()", args.get(0).name),
            "i64"
        );
        //System.out.println("returning null, fromAsInt");
        //return null;
    }
    public static Variable fromPlus(List<Variable> args){
        if (args != null && args.size() == 2 && "i64".equals(args.get(0).type) && "i64".equals(args.get(1).type)) {
            return new Variable(
                "var" + AST.nextId.getAndIncrement(),
                String.format("%s + %s", args.get(0).name, args.get(1).name),
                "i64"
            );
        }
        System.out.println("returning null, frommPlus");
        return null;
    }
    public static Variable fromQQInt(List<Variable> args){
        if (args != null && args.size() == 1 && "Vec<u8>".equals(args.get(0).type)) {
            return new Variable(
                "var" + AST.nextId.getAndIncrement(),
                String.format("%s.as_slice().read_i64::<BigEndian>().ok()?;", args.get(0).name),
                "i64"
            );
        }
        return null;
    }
    public static Variable fromQQBytes(final Node node){
        if (node.children.size() == 0 && node.value != null) {
            //System.out.println("node.value = " + node.value);
            if (!"org.eolang.bytes".equals(node.base)) {
                throw new IllegalArgumentException("node.base = " + node.base);
            }
            return new Variable(
                "var_" + AST.nextId.getAndIncrement(),
                    String.format(
                        "vec![%s]",
                        Arrays.stream(node.value.split(" ")).map(word -> "0x" + word).reduce("", (a, b) -> a + b + ", ")
                        ),
                    "Vec<u8>"
                );
        }
        System.out.println("returning null, fromQQBytes");
        return null;
    }

}
