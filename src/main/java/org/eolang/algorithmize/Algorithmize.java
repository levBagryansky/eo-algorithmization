package org.eolang.algorithmize;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.cactoos.bytes.BytesOf;
import org.cactoos.bytes.UncheckedBytes;
import org.eolang.algorithmize.AST.AST;
import org.eolang.algorithmize.AST.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Algorithmize {

    private final Path src;

    private final Path dest;

    private XMLDocument xml;

    public Algorithmize(final Path src, final Path dest) {
        this.src = src;
        this.dest = dest;
    }

    /**
     * Algorithmize and saves it to
     */
    public void exec() throws IOException, ParserConfigurationException, SAXException {
        //System.out.println("Hello world");
        //System.out.println(this.src);
        //System.out.println("Hello world");
        this.xml = new XMLDocument(this.src);
        List<XML> line_4_childs = this.xml.nodes("//o[@name='line-4']/o");
        final String code = new AST(Node.xml2Node(line_4_childs.get(0))).rustCode();
        System.out.println(code);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(this.src.toFile());
        Element rust = document.createElement("o");
        rust.setAttribute("base", ".rust");
        rust.setTextContent(code);
        document.getDocumentElement().appendChild(this.buildRustElement(document, Algorithmize.hex(code)));
        System.out.println(new XMLDocument(document));
        try (FileWriter out = new FileWriter(dest.toFile())) {
            out.write(this.xml.toString());
            out.flush();
        }
    }

    private static String hex(final String content)  {
        final StringBuilder out = new StringBuilder(0);
        for (final byte data : new UncheckedBytes(new BytesOf(content)).asBytes()) {
            if (out.length() > 0) {
                out.append('-');
            }
            out.append(String.format("%02X", data));
        }
        if (out.length() == 0) {
            out.append(' ');
        }
        return out.toString();
    }

    private Element buildRustElement(final Document document, final String code) {
        final Element rust = document.createElement("o");
        rust.setAttribute("base", "org.eolang.rust");

        final Element first_obj_bytes = document.createElement("o");
        first_obj_bytes.setAttribute("base", "org.eolang.bytes");
        first_obj_bytes.setAttribute("data", "bytes");
        first_obj_bytes.setAttribute("loc", "myloc");
        first_obj_bytes.setTextContent(code);

        final Element first_obj = document.createElement("o");
        first_obj.setAttribute("base", "org.eolang.string");
        first_obj.appendChild(first_obj_bytes);

        rust.appendChild(first_obj);


        return rust;
    }

    public List<AST> extractAST() {
//        List<XML> parents = this.xml.nodes("//o[@base='org.eolang.seq']/..");
//        //System.out.println("pretendents size = " + parents.size());
//        final List<AST_XML> ret = new ArrayList<>();
//        for (final XML parent: parents) {
//            for (final XML seq: parent.nodes("o[@base='org.eolang.seq']")) {
//                //System.out.println();
//                //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//                //System.out.println(seq);
////                ret.add(new AST_XML(this.exclusives(), seq));
////                System.out.println("\n\n");
////                System.out.println(Node.xml2Node(seq).toString(0));
////                System.out.println("\n\n");
//            }
//        }
        List<XML> line_4_childs = this.xml.nodes("//o[@name='line-4']/o");
        System.out.println(
            new AST(Node.xml2Node(line_4_childs.get(0)), new ArrayList<>()).rustCode()
        );
        for (XML item: line_4_childs) {
            System.out.println(item.node().getLocalName());
            System.out.println(item.node().getNodeName());
        }
        System.out.println();
        String code = new AST(Node.xml2Node(new XMLDocument(line_4_childs.get(0).node().cloneNode(true))), new ArrayList<>()).rustCode();
        XML line_4 = this.xml.nodes("//o[@name='line-4']").get(0);
        line_4.node().insertBefore(new XMLDocument("<o base=\"org.eolang.int\"\n" +
                "                  data=\"int\"\n" +
                "                  line=\"31\"\n" +
                "                  loc=\"Φ.orgg.eolangg.line.mem.α0.ρ\"\n" +
                "                  pos=\"4\">1</o>").nodes("//o").get(0).node()
            , line_4.node().getFirstChild());
        return line_4_childs.stream().map(node -> new AST(Node.xml2Node(node), new ArrayList<>())).collect(Collectors.toList());
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
