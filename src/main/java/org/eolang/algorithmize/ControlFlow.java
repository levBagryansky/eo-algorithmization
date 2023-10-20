package org.eolang.algorithmize;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.plugin.logging.Log;
import java.util.Collection;
import java.util.List;

public class ControlFlow {

    private final Collection<EOObject> external;

    public ControlFlow(Collection<EOObject> external) {
        this.external = external;
    }

    public Pair<String, List<String>> rustRepresentation() {
        return null;
    }

    public int rustEstimation() {
        return Integer.MAX_VALUE;
    }
}
