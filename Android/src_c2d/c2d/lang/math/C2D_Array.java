package c2d.lang.math;

import c2d.lang.util.debug.C2D_Debug;

/**
 * ���߱������ܵĶ�̬�������飬�൱��Java�е�Vector�����Ǹ�Ϊ��Ч
 * @author AndrewFan
 *
 */
public class C2D_Array
{
	protected int m_capacity;  // ����
	public Object m_datas[];// �ڲ�����
	protected int m_length;// ��ŵ�Ԫ�ظ���
	public C2D_Array()
	{
		m_capacity=10;
		m_datas = new Object[m_capacity];
	}
	public C2D_Array(int capacity)
	{
		m_capacity=capacity;
		m_datas = new Object[m_capacity];
	}
	/**
	 * ��¡����
	 * @return �µĿ�¡����
	 */
	public C2D_Array cloneSelf()
	{
		C2D_Array newInstance = new C2D_Array();
		newInstance.m_capacity = m_capacity;
		newInstance.m_length = m_length;
		newInstance.m_datas = new Object[m_datas.length];
		System.arraycopy(m_datas, 0, newInstance.m_datas, 0, m_datas.length);
		return newInstance;
	}
	/**
	 * ����Ԫ��
	 * @param element
	 */
	public void addElement(Object element)
	{
		increaseCapacity();
		// ��ֵ
		m_datas[m_length] = element;
		m_length++;
	}

	/**
	 * ����Ԫ��
	 * @param index �����λ��
	 * @param element ����Ķ���
	 * @return �����Ƿ�ɹ�����
	 */
	public boolean insertElementAt(int index, Object element)
	{
		if (index < 0)
		{
			return false;
		}
		if (index >= m_length)
		{
			addElement(element);
			return true;
		}
		increaseCapacity();
		// ��󿽱�
		// for(int i=length;i>index;i--)
		// {
		// datas[i]=datas[i-1];
		// }
		System.arraycopy(m_datas, index, m_datas, index + 1, m_length - index);

		m_datas[index] = element;

		m_length++;
		return true;
	}
	/**
	 * ��������
	 */
	private void increaseCapacity()
	{
		if (m_length >= m_capacity)
		{
			int newCapacity = m_capacity;
			if(newCapacity==0)
			{
				newCapacity++;
			}
			newCapacity*=2;
			Object datasNew[] = new Object[newCapacity];
			System.arraycopy(m_datas, 0, datasNew, 0, m_length);
			m_datas = datasNew;
			m_capacity = newCapacity;
		}
	}

	/**
	 * ��ն�������
	 */
	public void removeAllElements()
	{
		if(m_datas!=null)
		{
			for (int i = 0; i < m_length; i++)
			{
				m_datas[i] = null;
			}
		}
		m_length = 0;
	}
	/**
	 * ��ն������飬ͬremoveAllElements()
	 */
	public void clear()
	{
		removeAllElements();
	}

	/**
	 * �Ƿ����ĳ������
	 * @param element  �����Ķ���
	 * @return �Ƿ����
	 */
	public boolean contains(Object element)
	{
		for (int i = 0; i < m_length; i++)
		{
			if (m_datas[i].equals(element))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * ȡλ��ĳ���±�Ķ���
	 * @param index �����±�
	 * @return ��Ӧ�Ķ���
	 */
	public Object elementAt(int index)
	{
		if (index < 0 || index >= m_length)
		{
			return null;
		}
		return m_datas[index];
	}

	/**
	 * �ж�ĳ��ֵλ�ڵ�ǰVector�е�ʲôλ��
	 * @param element Ҫ�жϵĶ���
	 * @return �����ڵ�ǰ�����е�λ��
	 */
	public int indexOf(Object element)
	{
		for (int i = 0; i < m_length; i++)
		{
			if (m_datas[i].equals(element))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * �жϵ�ǰVector�Ƿ�Ϊ��
	 * @return Vector�Ƿ�Ϊ��
	 */
	public boolean isEmpty()
	{
		return m_length == 0;
	}

	/**
	 * �Ƴ�ָ��λ�õĶ��󣬺����������������
	 * @param index
	 * @return �Ƿ�ɹ��Ƴ�
	 */
	public boolean removeElementAt(int index)
	{
		if (index < 0 || index >= m_length)
		{
			return false;
		}

		for (int i = index; i <= m_length - 2; i++)
		{
			m_datas[i] = m_datas[i + 1];
		}
		m_datas[m_length-1]=null;
		m_length--;
		return true;
	}

	/**
	 *  ɾ��ĳ������
	 * @param element Ҫɾ���Ķ���
	 * @return �Ƿ�ɹ�ɾ��
	 */
	public boolean remove(Object element)
	{
		int index = indexOf(element);
		if (index < 0)
		{
			return false;
		}
		return removeElementAt(index);
	}
	/**
	 *  ɾ��β������
	 * @return �Ƿ�ɹ�ɾ��
	 */
	public boolean removeEnd()
	{
		if (m_length <= 0)
		{
			return false;
		}
		m_datas[m_length-1]=null;
		m_length--;
		return true;
	}
	/**
	 * ��indexλ���������ö���
	 * @param index ָ�����±�
	 * @param element �������õĶ���
	 * @return �Ƿ�ɹ�����
	 */
	public boolean setElementAt( Object element,int index)
	{
		if (index < 0 || index >= m_length)
		{
			return false;
		}
		m_datas[index] = element;
		return true;
	}

	/**
	 * ��ȡ��С
	 * @return ��С
	 */
	public int size()
	{
		return m_length;
	}

	/**
	 * ��ʾ�����飬ÿ������֮���Զ��ŷָ�
	 */
	public void show()
	{
		for (int i = 0; i < m_length; i++)
		{
			Object obj = m_datas[i];
			C2D_Debug.logChunk(obj.toString() + ",");
		}
		C2D_Debug.log("");
	}

	/**
	 * ��ʾ�����飬ÿ������һ��
	 */
	public void showByLines()
	{
		for (int i = 0; i < m_length; i++)
		{
			Object obj = m_datas[i];
			C2D_Debug.log(obj.toString());
		}
	}
}
