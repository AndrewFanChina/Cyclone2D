package c2d.lang.math;

public class C2D_HashKey
{
	private int m_key;

	public C2D_HashKey(int key)
	{
		setValue(key);
	}

	public void setValue(int key)
	{
		m_key = key;
	}
	
	public int getValue()
	{
		return m_key;
	}
	
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof C2D_HashKey))
		{
			return false;
		}
		return ((C2D_HashKey) obj).m_key == m_key;
	}

	public int hashCode()
	{
		return m_key;
	}
	
	
}
