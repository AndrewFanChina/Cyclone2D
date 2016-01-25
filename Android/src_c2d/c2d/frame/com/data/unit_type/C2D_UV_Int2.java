package c2d.frame.com.data.unit_type;

public class C2D_UV_Int2 extends C2D_UnitValue
{
	public int m_v1,m_v2;
	public C2D_UV_Int2(int v1,int v2)
	{
		m_v1= v1;
		m_v2=v2;
	}
	public C2D_UnitValue cloneSelf()
	{
		C2D_UV_Int2 newInstance =new C2D_UV_Int2(m_v1,m_v2);
		return newInstance;
	}
	public String toString()
	{
		return m_v1+","+m_v2;
	}
}
