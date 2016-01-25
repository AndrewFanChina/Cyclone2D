package c2d.lang.math.type;


public class C2D_SizeF {
    public float m_width, m_height;

    public C2D_SizeF() {
        this(0, 0);
    }

    public C2D_SizeF(float w, float h) {
        m_width = w;
        m_height = h;
    }

    public static C2D_SizeF zero() {
        return new C2D_SizeF(0, 0);
    }

    public float getWidth() {
        return m_width;
    }

    public float getHeight() {
        return m_height;
    }
    public void setValue(C2D_SizeF another)
    {
    	if(another==null)
    	{
    		return;
    	}
    	this.m_width=another.m_width;
    	this.m_height=another.m_height;
    }
    public void setValue(float width,float height)
    {
    	this.m_width=width;
    	this.m_height=height;
    }
    public static boolean equalToSize(C2D_SizeF s1, C2D_SizeF s2) {
        return s1.m_width == s2.m_width && s1.m_height == s2.m_height;
    }

    public String toString() {
        return "<" + m_width + ", " + m_height + ">";
    }
}
