package c2d.thirdpt.xml.kxml.parser;


import c2d.thirdpt.xml.kxml.C2D_Xml;

/** A class for events indicating the 
 *  end of an element 
 */


public class C2D_XmlTag extends C2D_ParseEvent {

    C2D_StartTag parent;
    String namespace;
    String name;

    public C2D_XmlTag (int type, C2D_StartTag parent, String namespace, String name) {
	super (type, null);
	this.parent = parent;
	this.namespace = namespace == null ? C2D_Xml.NO_NAMESPACE : namespace;
	this.name = name;
    }



    /** returns the (local) name of the element */

    public String getName () {
	return name;
    }

    /** returns the namespace */ 
    
    public String getNamespace () {
	return namespace;
    }
  
    /** Returns the (corresponding) start tag or the start tag of the parent element, depending on the event type. */ 

    public C2D_StartTag getParent () {
	return parent;
    }

  

    public String toString () {
	return "EndTag </"+name + ">";
    }
}








