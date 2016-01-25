package c2d.thirdpt.xml;

import c2d.lang.math.C2D_Array;

/**
 * XML������
 * @author AndrewFan
 *
 */
public class C2D_XmlAtt
{
	String m_name;     //��������
	C2D_Array m_values;//ֵ�б�
	public C2D_XmlAtt(String name)
	{
		m_name=name;
	}
	/**
	 * ��ȡ����
	 * @return ��ǰ��������
	 */
	public String getName()
	{
		return m_name;
	}
	/**
	 * �������� 
	 * @param name ��ǰ��������
	 */
	public void setName(String name)
	{
		this.m_name = name;
	}
	/**
	 * ���ص�һ��ֵ
	 * @return ��һ��ֵ
	 */
	public String getValue()
	{
		if(m_values==null||m_values.size()==0)
		{
			return null;
		}
		return (String)m_values.elementAt(0);
	}
	/**
	 * ��ȡֵ�б���
	 * @return ֵ�б���
	 */
	public int getValueLen()
	{
		if(m_values==null)
		{
			return 0;
		}
		return m_values.size();
	}
	/**
	 * ��ȡ�±�λ��id��ֵ����
	 * @param id �±�
	 * @return ��Ӧֵ
	 */
	public String getValueAt(int id)
	{
		if(m_values==null||id<0||id>=m_values.size())
		{
			return null;
		}
		return (String)m_values.elementAt(id);
	}
	/**
	 * ��ֵ�б������ֵ��Ԫ
	 * @param value
	 */
	public void addValue(String value)
	{
		if(m_values==null)
		{
			m_values=new C2D_Array();
		}
		m_values.addElement(value);
	}
	/**
	 * �����������
	 */
	public void clear()
	{
		if(m_values!=null)
		{
			m_values.clear();
		}
	}
}