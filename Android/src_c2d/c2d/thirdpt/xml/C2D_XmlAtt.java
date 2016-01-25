package c2d.thirdpt.xml;

import c2d.lang.math.C2D_Array;

/**
 * XML属性类
 * @author AndrewFan
 *
 */
public class C2D_XmlAtt
{
	String m_name;     //属性名称
	C2D_Array m_values;//值列表
	public C2D_XmlAtt(String name)
	{
		m_name=name;
	}
	/**
	 * 获取名称
	 * @return 当前属性名称
	 */
	public String getName()
	{
		return m_name;
	}
	/**
	 * 设置名称 
	 * @param name 当前属性名称
	 */
	public void setName(String name)
	{
		this.m_name = name;
	}
	/**
	 * 返回第一个值
	 * @return 第一个值
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
	 * 获取值列表长度
	 * @return 值列表长度
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
	 * 获取下标位于id的值对象
	 * @param id 下标
	 * @return 对应值
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
	 * 向值列表中添加值单元
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
	 * 清除所有内容
	 */
	public void clear()
	{
		if(m_values!=null)
		{
			m_values.clear();
		}
	}
}