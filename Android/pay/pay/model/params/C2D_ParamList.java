package pay.model.params;

import c2d.util.math.C2D_Array;

public class C2D_ParamList
{
	private C2D_Array m_list = new C2D_Array();
	/**
	 * ���ò�����ָ�����������Լ���Ӧ�Ĳ���ֵ��
	 * ������������ڣ�����ӽ�����������������޸�
	 * @param name ������
	 * @param value ����ֵ
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
	 * ����int��ֵ������ָ�����������Լ���Ӧ�Ĳ���ֵ��
	 * ������������ڣ�����ӽ�����������������޸�
	 * @param name ������
	 * @param value ����ֵ
	 */
	public void setParam(String name, int value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * ����short��ֵ������ָ�����������Լ���Ӧ�Ĳ���ֵ��
	 * ������������ڣ�����ӽ�����������������޸�
	 * @param name ������
	 * @param value ����ֵ
	 */
	public void setParam(String name, short value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * ����byte��ֵ������ָ�����������Լ���Ӧ�Ĳ���ֵ��
	 * ������������ڣ�����ӽ�����������������޸�
	 * @param name ������
	 * @param value ����ֵ
	 */
	public void setParam(String name, byte value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * ����boolean��ֵ������ָ�����������Լ���Ӧ�Ĳ���ֵ��
	 * ������������ڣ�����ӽ�����������������޸�
	 * @param name ������
	 * @param value ����ֵ
	 */
	public void setParam(String name, boolean value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * ����String��ֵ������ָ�����������Լ���Ӧ�Ĳ���ֵ��
	 * ������������ڣ�����ӽ�����������������޸�
	 * @param name ������
	 * @param value ����ֵ
	 */
	public void setParam(String name, String value)
	{
		setParam(name,new C2D_ParamValue(value));
	}
	/**
	 * ��鵱ǰ�Ĳ����б��У��Ƿ���ָ���������Ĳ��������������ֵ��ָ������ֵ��ͬ
	 * @param name ���Ĳ�����
	 * @param value ���Ĳ���ֵ
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
	 * ���ݲ���������ȡ��Ӧ�Ĳ�������
	 * @param name ������
	 * @return ��������
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
	 * ���ݲ���������ȡ��Ӧ�Ĳ���ֵ
	 * @param name ������
	 * @return ����ֵ
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
	 * ���ݲ���������ȡ��Ӧ�Ĳ���ֵ����ת����int��ֵ
	 * @param name ������
	 * @return ����ֵ
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
	 * ���ݲ���������ȡ��Ӧ�Ĳ���ֵ����ת����short��ֵ
	 * @param name ������
	 * @return ����ֵ
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
	 * ���ݲ���������ȡ��Ӧ�Ĳ���ֵ����ת����byte��ֵ
	 * @param name ������
	 * @return ����ֵ
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
	 * ���ݲ���������ȡ��Ӧ�Ĳ���ֵ����ת����boolean��ֵ
	 * @param name ������
	 * @return ����ֵ
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
	 * ���ݲ���������ȡ��Ӧ�Ĳ���ֵ����ת����String��ֵ
	 * @param name ������
	 * @return ����ֵ
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
	 * ���ݲ���Id����ȡ��Ӧ�Ĳ�������
	 * @param id ����id
	 * @return ��������
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
	 * �жϵ�ǰ�������Ƿ�Ϊ��
	 * @return ��ǰ�������Ƿ�Ϊ��
	 */
	public boolean isEmpty()
	{
		return m_list.isEmpty();
	}
	/**
	 * ��ȡ��ǰ�������С
	 * @return ��ǰ�������С
	 */
	public int size()
	{
		return m_list.size();
	}
	/**
	 * ����ָ���Ĳ������ƣ��Ƴ�����
	 * @param name ��������
	 * @return ���Ƴ��Ĳ���
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
	 * ��ղ�����
	 */
	public void clear()
	{
		m_list.removeAllElements();
	}
}
