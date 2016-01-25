package pay.model.params;

import c2d.util.math.C2D_Array;

public class C2D_ParamList
{
	private C2D_Array m_list = new C2D_Array();
	/**
	 * 设置参数，指定参数名称以及对应的参数值，
	 * 如果参数不存在，则添加进参数表，如果存在则修改
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void setParam(String name, C2D_ParamValue value)
	{
		C2D_Param param = getParamByName(name);
		if (param == null)
		{
			param = new C2D_Param(name);
			m_list.addElement(param);
		}
		param.setValue(value);
	}
	/**
	 * 设置int形值参数，指定参数名称以及对应的参数值，
	 * 如果参数不存在，则添加进参数表，如果存在则修改
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void setParam(String name, int value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * 设置short形值参数，指定参数名称以及对应的参数值，
	 * 如果参数不存在，则添加进参数表，如果存在则修改
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void setParam(String name, short value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * 设置byte形值参数，指定参数名称以及对应的参数值，
	 * 如果参数不存在，则添加进参数表，如果存在则修改
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void setParam(String name, byte value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * 设置boolean形值参数，指定参数名称以及对应的参数值，
	 * 如果参数不存在，则添加进参数表，如果存在则修改
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void setParam(String name, boolean value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * 设置String形值参数，指定参数名称以及对应的参数值，
	 * 如果参数不存在，则添加进参数表，如果存在则修改
	 * @param name 参数名
	 * @param value 参数值
	 */
	public void setParam(String name, String value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * 检查当前的参数列表中，是否含有指定参数名的参数，并且其参数值与指定参数值相同
	 * @param name 检查的参数名
	 * @param value 检查的参数值
	 * @return
	 */
	public boolean checkParam(String name, C2D_ParamValue value)
	{
		C2D_Param param = getParamByName(name);
		if (param == null)
		{
			return false;
		}
		C2D_ParamValue valueT = param.getValue();
		if(valueT==null)
		{
			return false;
		}
		return valueT.equals(value);
	}
	/**
	 * 根据参数名，获取对应的参数对象
	 * @param name 参数名
	 * @return 参数对象
	 */
	public C2D_Param getParamByName(String name)
	{
		if (name == null)
		{
			return null;
		}
		for (int i = 0; i < m_list.size(); i++)
		{
			C2D_Param param = (C2D_Param) m_list.elementAt(i);
			if (param.getName() != null && param.getName().equals(name))
			{
				return param;
			}
		}
		return null;
	}
	/**
	 * 根据参数名，获取对应的参数值
	 * @param name 参数名
	 * @return 参数值
	 */
	public C2D_ParamValue getValueByName(String name)
	{
		C2D_Param p = getParamByName(name);
		if(p!=null)
		{
			return p.getValue();
		}
		return null;
	}
	/**
	 * 根据参数名，获取对应的参数值，并转化成int型值
	 * @param name 参数名
	 * @return 参数值
	 */
	public int getIntByName(String name)
	{
		C2D_ParamValue pv = getValueByName(name);
		if(pv!=null)
		{
			return pv.parseInt();
		}
		return -1;
	}
	/**
	 * 根据参数名，获取对应的参数值，并转化成short型值
	 * @param name 参数名
	 * @return 参数值
	 */
	public short getShortByName(String name)
	{
		C2D_ParamValue pv = getValueByName(name);
		if(pv!=null)
		{
			return pv.parseShort();
		}
		return -1;
	}
	/**
	 * 根据参数名，获取对应的参数值，并转化成byte型值
	 * @param name 参数名
	 * @return 参数值
	 */
	public byte getByteByName(String name)
	{
		C2D_ParamValue pv = getValueByName(name);
		if(pv!=null)
		{
			return pv.parseByte();
		}
		return -1;
	}
	/**
	 * 根据参数名，获取对应的参数值，并转化成boolean型值
	 * @param name 参数名
	 * @return 参数值
	 */
	public boolean getBooleanByName(String name)
	{
		C2D_ParamValue pv = getValueByName(name);
		if(pv!=null)
		{
			return pv.parseBoolean();
		}
		return false;
	}
	/**
	 * 根据参数名，获取对应的参数值，并转化成String型值
	 * @param name 参数名
	 * @return 参数值
	 */
	public String getStringByName(String name)
	{
		C2D_ParamValue pv = getValueByName(name);
		if(pv!=null)
		{
			return pv.parseString();
		}
		return null;
	}
	/**
	 * 根据参数Id，获取对应的参数对象
	 * @param id 参数id
	 * @return 参数对象
	 */
	public C2D_Param getParamById(int id)
	{
		if (id<0||id>=m_list.size())
		{
			return null;
		}
		C2D_Param param = (C2D_Param) m_list.elementAt(id);
		return param;
	}
	/**
	 * 判断当前参数表是否为空
	 * @return 当前参数表是否为空
	 */
	public boolean isEmpty()
	{
		return m_list.isEmpty();
	}
	/**
	 * 获取当前参数表大小
	 * @return 当前参数表大小
	 */
	public int size()
	{
		return m_list.size();
	}
	/**
	 * 根据指定的参数名称，移除参数
	 * @param name 参数名称
	 * @return 被移除的参数
	 */
	public C2D_Param removeParam(String name)
	{
		if (name == null)
		{
			return null;
		}
		for (int i = 0; i < m_list.size(); i++)
		{
			C2D_Param param = (C2D_Param) m_list.elementAt(i);
			if (param.getName() != null && param.getName().equals(name))
			{
				m_list.removeElementAt(i);
				return param;
			}
		}
		return null;
	}
	/**
	 * 清空参数表
	 */
	public void clear()
	{
		m_list.removeAllElements();
	}
}
