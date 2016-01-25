package c2d.thirdpt.xml.kxml;

import java.io.IOException;

import c2d.thirdpt.xml.kxml.io.C2D_AbstractXmlWriter;
import c2d.thirdpt.xml.kxml.parser.C2D_AbstractXmlParser;

public interface C2D_XmlIO {

    public void parse (C2D_AbstractXmlParser parser) throws IOException;
    public void write (C2D_AbstractXmlWriter writer) throws IOException;
}
