package c2d.thirdpt.xml.kxml.io;

import c2d.thirdpt.xml.kxml.C2D_PrefixMap;

public class C2D_State {
    public C2D_State prev;
    public C2D_PrefixMap prefixMap;
    public String namespace;
    public String name;
    public String tag;   // for auto-endtag writing

    public C2D_State (C2D_State prev, C2D_PrefixMap prefixMap, 
	       //String namespace, String name, 
	       String tag) {
	
	this.prev = prev;
	this.prefixMap = prefixMap;
	//	    this.namespace = namespace;
	//	    this.name = name;
	this.tag = tag;
    }
}
