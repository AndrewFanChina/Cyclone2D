package pay.model.params;

public class C2D_ParamValue
{
	private Object m_data;
	public static C2D_ParamValue Value_True=new C2D_ParamValue(true);
	public static C2D_ParamValue Value_False=new C2D_ParamValue(false);
	public C2D_ParamValue(int data)
	{
		m_data = new Integer(data);
	}

	public C2D_ParamValue(short data)
	{
		this((int) data);
	}

	public C2D_ParamValue(byte data)
	{
		this((int) data);
	}
	
	public C2D_ParamValue(String data)
	{
		m_data = new String(data);
	}

	public C2D_ParamValue(boolean data)
	{
		m_data = new Boolean(data);
	}
	
	Object getData()
	{
		return m_data;
	}

	public int parseInt()
	{
		if(m_data!=null && m_data instanceof Integer)
		{
			return ((Integer)m_data).intValue();
		}
		return -1;
	}
	public short parseShort()
	{
		int value=parseInt();
		return (short)value;
	}
	public byte parseByte()
	{
		int value=parseInt();
		return (byte)value;
	}
	
	public boolean parseBoolean()
	{
		if(m_data!=null && m_data instanceof Boolean)
		{
			return ((Boolean)m_data).booleanValue();
		}
		return false;
	}
	
	public String parseString()
	{
		if(m_data!=null && m_data instanceof String)
		{
			return ((String)m_data);
		}
		return null;
	}
	
	public boolean equals(C2D_ParamValue value)
	{
		if (value == null)
		{
			return false;
		}
		Object valueData = value.getData();
		Object paramData = this.getData();
		if (valueData == null || paramData == null)
		{
			return false;
		}
		if (!paramData.getClass().equals(valueData.getClass()))
		{
			return false;
		}
		return paramData.equals(valueData);
	}
}
