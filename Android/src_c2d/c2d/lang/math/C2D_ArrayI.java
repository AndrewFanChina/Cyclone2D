package c2d.lang.math;

import c2d.lang.util.debug.C2D_Debug;

/**
 * ���߱������ܵĶ�̬��Ԫ���飬�൱��Java�е�Vector�����Ǹ�Ϊ��Ч
 * @author AndrewFan
 *
 */
public class C2D_ArrayI
{
	protected int m_capacity;  // ����
	protected int m_datas[];// �ڲ�����
	protected int m_length;// ��ŵ�Ԫ�ظ���
	public C2D_ArrayI()
	{
		m_capacity=10;
		m_datas = new int[m_capacity];
	}
	public C2D_ArrayI(int capacity)
	{
		m_capacity=capacity;
		m_datas = new int[m_capacity];
	}
	/**
	 * ��¡����
	 * @return �µĿ�¡��Ԫ
	 */
	public C2D_ArrayI cloneSelf()
	{
		C2D_ArrayI newInstance = new C2D_ArrayI();
		newInstance.m_capacity = m_capacity;
		newInstance.m_length = m_length;
		newInstance.m_datas = new int[m_datas.length];
		System.arraycopy(m_datas, 0, newInstance.m_datas, 0, m_datas.length);
		return newInstance;
	}
	/**
	 * ����Ԫ��
	 * @param element
	 */
	public void addElement(int element)
	{
		increaseCapacity();
		// ��ֵ
		m_datas[m_length] = element;
		m_length++;
	}

	/**
	 * ����Ԫ��
	 * @param index �����λ��
	 * @param element ����ĵ�Ԫ
	 * @return �����Ƿ�ɹ�����
	 */
	public boolean insertElementAt(int index, int element)
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
			int newCapacity = m_capacity * 2;
			int datasNew[] = new int[newCapacity];
			System.arraycopy(m_datas, 0, datasNew, 0, m_length);
			m_datas = datasNew;
			m_capacity = newCapacity;
		}
	}

	/**
	 * ��յ�Ԫ����
	 */
	public void removeAllElements()
	{
		for (int i = 0; i < m_length; i++)
		{
			m_datas[i] = 0;
		}
		m_length = 0;
	}

	/**
	 * �Ƿ����ĳ����Ԫ
	 * @param element  �����ĵ�Ԫ
	 * @return �Ƿ����
	 */
	public boolean contains(int element)
	{
		for (int i = 0; i < m_length; i++)
		{
			if (m_datas[i]==(element))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * ȡλ��ĳ���±�ĵ�Ԫ��ȡ��������-1
	 * @param index ��Ԫ�±�
	 * @return ��Ӧ�ĵ�Ԫ
	 */
	public int elementAt(int index)
	{
		if (index < 0 || index >= m_length)
		{
			return -1;
		}
		return m_datas[index];
	}

	/**
	 * �ж�ĳ��ֵλ�ڵ�ǰVector�е�ʲôλ��
	 * @param element Ҫ�жϵĵ�Ԫ
	 * @return ��Ԫ�ڵ�ǰ�����е�λ��
	 */
	public int indexOf(int element)
	{
		for (int i = 0; i < m_length; i++)
		{
			if (m_datas[i] == (element))
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
	 * �Ƴ�ָ��λ�õĵ�Ԫ��������Ԫ����������
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
		m_datas[m_length-1]=0;
		m_length--;
		return true;
	}

	/**
	 *  ɾ��ĳ����Ԫ
	 * @param element Ҫɾ���ĵ�Ԫ
	 * @return �Ƿ�ɹ�ɾ��
	 */
	public boolean remove(int element)
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
		m_datas[m_length-1]=0;
		m_length--;
		return true;
	}
	/**
	 * ��indexλ���������õ�Ԫ
	 * @param index ָ�����±�
	 * @param element �������õĵ�Ԫ
	 * @return �Ƿ�ɹ�����
	 */
	public boolean setElementAt( int element,int index)
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
	 * ��ʾ�����飬ÿ����Ԫ֮���Զ��ŷָ�
	 */
	public void show()
	{
		for (int i = 0; i < m_length; i++)
		{
			int element = m_datas[i];
			C2D_Debug.logChunk(element + ",");
		}
		C2D_Debug.log("");
	}

	/**
	 * ��ʾ�����飬ÿ����Ԫһ��
	 */
	public void showByLines()
	{
		for (int i = 0; i < m_length; i++)
		{
			int element = m_datas[i];
			C2D_Debug.log(""+element);
		}
	}
}
