package org.eolang.algorithmize;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.apache.commons.lang3.tuple.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Algothmize {

    private final Path src;

    private final Path dest;

    public Algothmize(final Path src, final Path dest) {
        this.src = src;
        this.dest = dest;
    }

    /**
     * Algorithmize and saves it to
     */
    public void exec() throws IOException {
        System.out.println("Hello world");
        System.out.println(this.src);
        System.out.println("Hello world");
        final XML input = new XMLDocument(this.src);
        System.out.println("Hello world");
        insert(
            extractAST()
                .stream()
                .map(AST::toControlFlow)
                .map(ControlFlow::rustRepresentation)
                .collect(Collectors.toList())
        );
        try (FileWriter out = new FileWriter(dest.toFile())) {
            out.write(input.toString());
            out.flush();
        }
    }

    public List<AST> extractAST() {
        return List.of(new AST(null));
    }

    public void insert(List<Pair<String, List<String>>> rusts) {
        System.out.println("insert");
    }
}
