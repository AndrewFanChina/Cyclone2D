package c2d.frame.com.data.unit_type;

public class C2D_UV_String extends C2D_UnitValue
{
	public String m_value;
	public C2D_UV_String(String value)
	{
		if(value==null)
		{
			m_value=null;
		}
		else
		{
			m_value= new String(value);
		}

	}
	public C2D_UnitValue cloneSelf()
	{
		C2D_UV_String newInstance =new C2D_UV_String(m_value);
		return newInstance;
	}
	public String toString()
	{
		return m_value;
	}
}
