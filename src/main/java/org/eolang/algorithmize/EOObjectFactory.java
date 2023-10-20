package org.eolang.algorithmize;

import com.jcabi.xml.XML;

import java.util.List;
import java.util.Locale;

public class EOObjectFactory {

    public EOObject getEO(final XML node) {
        final List<String> types = node.xpath("./attribute(datatype)");
        if (types.size() != 1) {
            throw new IllegalArgumentException(
                String.format("Incorrect xml node %s", node)
            );
        }
        EOObject ret;
        switch (types.get(0).toLowerCase(Locale.ROOT)) {
            case "int":
                ret = new EOInt("", "", true);
                break;
            case "float":
                ret = new EOFloat("", "", true);
                break;
            case "string":
                ret = new EOString("", "", true);
                break;
            default:
                throw new IllegalArgumentException(
                    String.format(
                        "Incorrect type %s in xml %s was specified",
                        types.get(0),
                        node
                    )
                );
        }
        return ret;
    }

}
