package pay.model.params;

public class C2D_Param
{
	private String m_name;
	private C2D_ParamValue m_value;

	public C2D_Param(String name)
	{
		m_name = name;
		if (m_name == null)
		{
			m_name = "NotNamed";
		}
	}

	public String getName()
	{
		return m_name;
	}

	public C2D_ParamValue getValue()
	{
		return m_value;
	}

	public void setValue(C2D_ParamValue value)
	{
		m_value = value;
	}
}
