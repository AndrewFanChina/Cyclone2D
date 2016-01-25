package c2d.frame.com.data.unit_type;

public class C2D_UV_Size extends C2D_UnitValue
{
	public int m_width,m_height;
	public C2D_UV_Size(int width,int height)
	{
		m_width= width;
		m_height=height;
	}
	public C2D_UnitValue cloneSelf()
	{
		C2D_UV_Size newInstance =new C2D_UV_Size(m_width,m_height);
		return newInstance;
	}
	public String toString()
	{
		return m_width+","+m_height;
	}
}
