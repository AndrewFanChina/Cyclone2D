/* kXML
 *
 * The contents of this file are subject to the Enhydra Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License
 * on the Enhydra web site ( http://www.enhydra.org/ ).
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific terms governing rights and limitations
 * under the License.
 *
 * The Initial Developer of kXML is Stefan Haustein. Copyright (C)
 * 2000, 2001 Stefan Haustein, D-46045 Oberhausen (Rhld.),
 * Germany. All Rights Reserved.
 *
 * Contributor(s): Paul Palaszewski, Wilhelm Fitzpatrick, 
 *                 Eric Foster-Johnson, Daniel Feygin,  Scott Daub
 *
 * */

package c2d.thirdpt.xml.kxml.parser;

import java.io.IOException;
import java.util.Vector;

import c2d.thirdpt.xml.kxml.C2D_Xml;
import c2d.thirdpt.xml.kxml.io.C2D_ParseException;



/** An abstract base class for the XML and WBXML parsers. Of course,
    you can implement your own subclass with additional features,
    e.g. a validating parser.  */


public abstract class C2D_AbstractXmlParser {

    
    protected boolean processNamespaces = true;


    /** Ignores a tree */

    public void ignoreTree () throws IOException {
	readTree (null);
    }

    /** Reads a complete element tree to the given event
	Vector. The next event must be a start tag. */


    public void readTree (Vector buf) throws IOException { 


	C2D_StartTag start = (C2D_StartTag) read ();
	//if (buf != null) buf.addElement (start); [caused duplication, fixed by SD]

	while (true) {
	    C2D_ParseEvent event = peek ();
	    if (buf != null) buf.addElement (event);

	    switch (event.getType ()) {

	    case C2D_Xml.START_TAG:
		readTree (buf);
		break;

	    case C2D_Xml.END_TAG:
	    case C2D_Xml.END_DOCUMENT:
		read ();
		return;

	    default: 
		read ();
	    }
	}	
    }


    /** Returns the current line number; -1 if unknown. Convenience
        method for peek ().getLineNumber (). */

    public int getLineNumber () throws IOException {
	return peek ().getLineNumber ();
    }
	

    /** reads the next event available from the parser. If the end of
       the parsed stream has been reached, null is returned.  */

    public abstract C2D_ParseEvent read () throws IOException;




    /** Reads an event of the given type. If the type is START_TAG or
	END_TAG, namespace and name are tested, otherwise
	ignored. Throws a ParseException if the actual event does not
	match the given parameters. */

    public C2D_ParseEvent read (int type, String namespace, 
			    String name) throws IOException {

	if (peek (type, namespace, name)) 
	    return read ();
	else throw new C2D_ParseException 
	    ("unexpected: "+peek (), null,  
	     peek().getLineNumber (), -1);
    }


    public boolean peek (int type, String namespace, 
			 String name) throws IOException {
	 
	C2D_ParseEvent pe = peek ();
	
	return pe.getType () == type 
	    && (namespace == null || namespace.equals (pe.getNamespace ()))
	    && (name == null || name.equals (pe.getName ()));
    }


    /** Convenience Method for skip (Xml.COMMENT | Xml.DOCTYPE 
	| Xml.PROCESSING_INSTRUCTION | Xml.WHITESPACE) */

    public void skip () throws IOException { 
	while (true) {
	    int type = peek ().type;
	    if (type != C2D_Xml.COMMENT 
		&& type != C2D_Xml.DOCTYPE 
		&& type != C2D_Xml.PROCESSING_INSTRUCTION 
		&& type != C2D_Xml.WHITESPACE) break;
	    read ();
	}
    } 


    /** reads the next event available from the parser
	without consuming it */

    public abstract C2D_ParseEvent peek () throws IOException;



    /** tells the parser if it shall resolve namespace prefixes to
	namespaces. Default is true */

    public void setProcessNamespaces (boolean processNamespaces) {
	this.processNamespaces = processNamespaces;
    }



    
    /** Convenience method for reading the content of text-only 
        elements. The method reads text until an end tag is
        reached. Processing instructions and comments are
	skipped. The end tag is NOT consumed.  The concatenated text
        String is returned. If the method reaches a start tag, an
        Exception is thrown. */
    

    public String readText () throws IOException {
	
	StringBuffer buf = new StringBuffer ();

	while (true) {

	    C2D_ParseEvent event = peek ();

	    switch (event.getType ()) {

	    case C2D_Xml.START_TAG:
	    case C2D_Xml.END_DOCUMENT:
	    case C2D_Xml.DOCTYPE:
		throw new RuntimeException 
		    ("Illegal event: "+event);

	    case C2D_Xml.WHITESPACE:
	    case C2D_Xml.TEXT:
		read ();
		buf.append (event.getText ());
		break;

	    case C2D_Xml.END_TAG:
		return buf.toString ();

	    default:
		read ();
	    }
	}	
    }
}

