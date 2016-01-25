package c2d.lang.math.type;

public class C2D_SizeI
{
	public int m_width, m_height;

	public C2D_SizeI()
	{
		this(0, 0);
	}

	public C2D_SizeI(int w, int h)
	{
		m_width = w;
		m_height = h;
	}

	public static C2D_SizeI zero()
	{
		return new C2D_SizeI(0, 0);
	}

	public int getWidth()
	{
		return m_width;
	}

	public int getHeight()
	{
		return m_height;
	}

	public void setValue(int width, int height)
	{
		this.m_width = width;
		this.m_height = height;
	}

	public void setValue(C2D_SizeI size)
	{
		if (size != null)
		{
			this.m_width = size.m_width;
			this.m_height = size.m_height;
		}
	}

	public static boolean equalToSize(C2D_SizeI s1, C2D_SizeI s2)
	{
		return s1.m_width == s2.m_width && s1.m_height == s2.m_height;
	}

	public String toString()
	{
		return "<" + m_width + ", " + m_height + ">";
	}
}
