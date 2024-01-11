package org.eolang.algorithmize;

import com.jcabi.xml.XMLDocument;
import org.cactoos.bytes.BytesOf;
import org.cactoos.bytes.UncheckedBytes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;

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
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(this.src.toFile());
        NodeList objects = document.getElementsByTagName("o");
        for (int i = 0; i < objects.getLength(); i++) {
            Node obj = objects.item(i);
            if (nodeIsPlus(obj)) {
                System.out.println("ABOBA");
                System.out.println(obj.getChildNodes().getLength());
            }
        }
        System.out.println("objects size = " + objects.getLength());
    }

    private static boolean nodeIsSqrt(final Node obj) {
        return false;
    }

    private static boolean nodeIsPlus(final Node obj) {
        final Node name = obj.getAttributes().getNamedItem("original-name");
        System.out.println(name);
        if (name == null) {
            return false;
        }
        if (obj.getAttributes().getNamedItem("abstract") == null) {
            return false;
        }
        if (obj.getChildNodes().getLength() != 7) {
            return false;
        }
        if (obj.getChildNodes().item(1).getAttributes().getNamedItem("base") != null || obj.getChildNodes().item(1).getAttributes().getNamedItem("abstract") != null) {
            return false;
        }
        String name_a = obj.getChildNodes().item(1).getAttributes().getNamedItem("name").getTextContent();
        //System.out.println("name a = " + name_a);
        if (obj.getChildNodes().item(3).getAttributes().getNamedItem("base") != null || obj.getChildNodes().item(3).getAttributes().getNamedItem("abstract") != null) {
            return false;
        }
        String name_b = obj.getChildNodes().item(3).getAttributes().getNamedItem("name").getTextContent();
        //System.out.println("name b = " + name_b);

        Node plus = obj.getChildNodes().item(5);
        if (!".plus".equals(plus.getAttributes().getNamedItem("base").getTextContent())) {
            return false;
        }
        if (plus.getChildNodes().getLength() != 5) {
            return false;
        }
        int first = 1;
        if (plus.getChildNodes().item(first).getAttributes().getNamedItem("base") == null) {
            return false;
        }
        if (!name_a.equals(plus.getChildNodes().item(first).getAttributes().getNamedItem("base").getTextContent())) {
            return false;
        }
        int second = 3;
        if (plus.getChildNodes().item(second).getAttributes().getNamedItem("base") == null) {
            return false;
        }
        if (!name_b.equals(plus.getChildNodes().item(second).getAttributes().getNamedItem("base").getTextContent())) {
            return false;
        }
        return "plus".equals(obj.getAttributes().getNamedItem("original-name").getTextContent());
    }

    private static String hex(final String content)  {
        final StringBuilder out = new StringBuilder(0);
        for (final byte data : new UncheckedBytes(new BytesOf(content)).asBytes()) {
            if (out.length() > 0) {
                out.append(' ');
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
}
