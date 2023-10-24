package org.eolang.algorithmize;

import java.util.Collection;
import java.util.List;

public class Expressions {

    private final Collection<EOObject> external;

    private final List<String> operations;

    public Expressions(Collection<EOObject> external, List<String> operations) {
        this.external = external;
        this.operations = operations;
    }

    public RustInsert rustRepresentation() {
        final StringBuilder rust = new StringBuilder(
            "use eo_env::EOEnv;\n" +
            "use eo_env::eo_enum::EO;\n" +
            "use eo_env::eo_enum::EO::{EOInt};\n" +
            "use byteorder::{BigEndian, ReadBytesExt};\n" +
            "\n" +
            "pub fn foo(env: &mut EOEnv) -> Option<EO> {\n");
        for (final EOObject ext: this.external) {
            rust.append(String.format(
                "   let mut %s = 0 as i64;\n",
                ext.locator.replace(".", "_")
            ));
        }
        for (final String expression: this.operations) {
            rust.append("       ");
            rust.append(expression);
            rust.append(";\n");
        }
        rust.append(String.format(
            "   return Some(EOInt(%s));\n}\n",
            "mem"
        ));
        System.out.println(rust);
        return new RustInsert(rust.toString(), null, null);
    }

    public int rustEstimation() {
        return Integer.MAX_VALUE;
    }
}
