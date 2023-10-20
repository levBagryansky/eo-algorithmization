package org.eolang.algorithmize;

import java.util.Collection;

public class AST {
    private final Collection<EOObject> external;

    public AST(Collection<EOObject> external) {
        this.external = external;
    }

    public ControlFlow toControlFlow() {
        return new ControlFlow(this.external);
    }
}
