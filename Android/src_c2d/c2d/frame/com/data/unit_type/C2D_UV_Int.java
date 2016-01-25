package c2d.frame.com.data.unit_type;

public class C2D_UV_Int extends C2D_UnitValue
{
	public int m_value;
	public C2D_UV_Int(int value)
	{
		m_value= value;
	}
	public C2D_UnitValue cloneSelf()
	{
		C2D_UV_Int newInstance =new C2D_UV_Int(m_value);
		return newInstance;
	}
	public String toString()
	{
		return m_value+"";
	}
}
