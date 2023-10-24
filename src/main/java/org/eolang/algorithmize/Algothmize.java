package org.eolang.algorithmize;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Algothmize {

    private final Path src;

    private final Path dest;

    private XMLDocument xml;

    public Algothmize(final Path src, final Path dest) {
        this.src = src;
        this.dest = dest;
    }

    /**
     * Algorithmize and saves it to
     */
    public void exec() throws IOException {
        //System.out.println("Hello world");
        //System.out.println(this.src);
        //System.out.println("Hello world");
        this.xml = new XMLDocument(this.src);
        //System.out.println("Hello world");
        this.insert(
            this.extractAST()
                .stream()
                .map(AST::toControlFlow)
                .map(Expressions::rustRepresentation)
                .collect(Collectors.toList())
        );
        try (FileWriter out = new FileWriter(dest.toFile())) {
            out.write(this.xml.toString());
            out.flush();
        }
    }

    public List<AST> extractAST() {
        List<XML> parents = this.xml.nodes("//o[@base='org.eolang.seq']/..");
        //System.out.println("pretendents size = " + parents.size());
        final List<AST> ret = new ArrayList<>();
        for (final XML parent: parents) {
            for (final XML seq: parent.nodes("o[@base='org.eolang.seq']")) {
                //System.out.println();
                //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                //System.out.println(seq);
                ret.add(new AST(this.exclusives(), seq));
            }
        }
        return ret;
    }

    public void insert(List<RustInsert> rusts) {
        //System.out.println("insert");
    }

    public List<EOObject> exclusives() {
        return List.of(
            new EOInt("mem", null, true)
        );
    }
}
