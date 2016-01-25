package c2d.thirdpt.xml.kxml.parser;

import java.util.Vector;

import c2d.thirdpt.xml.kxml.C2D_PrefixMap;
import c2d.thirdpt.xml.kxml.C2D_Xml;
import c2d.thirdpt.xml.kxml.C2D_XmlAttribute;




/** A class for events indicating the start of a new element */


public class C2D_StartTag extends C2D_XmlTag {

    Vector attributes;
    boolean degenerated;
    C2D_PrefixMap prefixMap;


    /** creates a new StartTag. The attributes are not copied and may
	be reused in e.g. the DOM. So DO NOT CHANGE the attribute
	vector after handing over, the effects are undefined */

    public C2D_StartTag (C2D_StartTag parent, String namespace, 
		     String name, Vector attributes, 
		     boolean degenerated, boolean processNamespaces) {

	super (C2D_Xml.START_TAG, parent, namespace, name);

	this.attributes = (attributes == null || attributes.size () == 0) 
	    ? null 
	    : attributes;

	this.degenerated = degenerated;
	
	prefixMap = parent == null ? C2D_PrefixMap.DEFAULT : parent.prefixMap;

	if (!processNamespaces) return;

	boolean any = false; 

	for (int i = getAttributeCount () - 1; i >= 0; i--) {
	    C2D_XmlAttribute attr = (C2D_XmlAttribute) attributes.elementAt (i);
	    String attrName = attr.getName ();
	    int cut = attrName.indexOf (':');
	    String prefix;

	    if (cut != -1) {
		prefix = attrName.substring (0, cut);
		attrName = attrName.substring (cut+1);
	    }
	    else if (attrName.equals ("xmlns")) {
		prefix = attrName;
		attrName = "";
	    } 
	    else continue;

	    if (!prefix.equals ("xmlns")) {
		if (!prefix.equals ("xml")) any = true;
	    }
	    else {
		prefixMap = new C2D_PrefixMap (prefixMap, attrName, attr.getValue ());
	    
		//System.out.println (prefixMap);
		attributes.removeElementAt (i);
	    }
	}

	int len = getAttributeCount ();

	if (any) {
	    for (int i = 0; i < len; i++) {
		C2D_XmlAttribute attr = (C2D_XmlAttribute) attributes.elementAt (i);
		String attrName = attr.getName ();
		int cut = attrName.indexOf (':');
		
		if (cut == 0) 
		    throw new RuntimeException 
			("illegal attribute name: "+attrName+ " at "+this);
		
		else if (cut != -1) {		    
		    String attrPrefix = attrName.substring (0, cut);
		    if (!attrPrefix.equals ("xml")) {
			attrName = attrName.substring (cut+1);
			
			String attrNs = prefixMap.getNamespace (attrPrefix);
			
			if (attrNs == null) 
			    throw new RuntimeException 
				("Undefined Prefix: "+attrPrefix + " in " + this);
			
			attributes.setElementAt 
			    (new C2D_XmlAttribute (attrNs, 
					    attrName, attr.getValue ()), i);
		    }
		}
	    }
	}
	
	int cut = name.indexOf (':');
	
	String prefix;
	if (cut == -1) prefix = "";
	else if (cut == 0)
	    throw new RuntimeException 
		("illegal tag name: "+ name +" at "+this);
	else {
	    prefix = name.substring (0, cut);
	    this.name = name.substring (cut+1);
	}
	
	this.namespace = prefixMap.getNamespace (prefix);

	if (this.namespace == null) {
	    if (prefix.length () != 0) 
		throw new RuntimeException 
		    ("undefined prefix: "+prefix+" in "+prefixMap +" at "+this);
	    this.namespace = C2D_Xml.NO_NAMESPACE;
	}
    }


    /** returns the attribute vector. May return null for no attributes. */

    public Vector getAttributes () {
	return attributes;
    }



    public boolean getDegenerated () {
	return degenerated;
    }


    public C2D_PrefixMap getPrefixMap () {
	return prefixMap;
    }




    /** Simplified (!) toString method for debugging
	purposes only. In order to actually write valid XML,
        please use a XmlWriter. */


    public String toString () {
	return "StartTag <"+name+"> line: "+lineNumber+" attr: "+attributes;
    }

   
    public void setPrefixMap (C2D_PrefixMap map) {
	this.prefixMap = map;
    }
   
}

