package c2d.thirdpt.xml.kxml;


/** Like Attribute, this class is immutable for similar reasons */

public class C2D_PrefixMap {

    public static final C2D_PrefixMap DEFAULT = new C2D_PrefixMap (null, "", "");

    String prefix;
    String namespace;
    C2D_PrefixMap previous;


    public C2D_PrefixMap (C2D_PrefixMap previous, String prefix, String namespace) {       
	this.previous = previous;
	this.prefix = prefix;
	this.namespace = namespace;
    } 


    public String getNamespace () {
	return namespace;
    }

    public String getPrefix () {
	return prefix;
    }

    public C2D_PrefixMap getPrevious () {
	return previous;
    }

    /** returns the namespace associated with the given prefix,
	or null, if none is assigned */

    public String getNamespace (String prefix) {
	C2D_PrefixMap current = this;
	do {
	    if (prefix.equals (current.prefix)) return current.namespace;
	    current = current.previous;
	}
	while (current != null);
	return null;
    }


    public String getPrefix (String namespace) {
	C2D_PrefixMap current = this;
	
	do { 
	    //System.err.println ("found: "+current.namespace +"/"+ current.prefix + "/" +getNamespace (current.prefix));
	    if (namespace.equals (current.namespace)
		&& namespace.equals (getNamespace (current.prefix))) 
		return current.prefix;

	    current = current.previous;
	}
	while (current != null); 
	return null;
    }
}

