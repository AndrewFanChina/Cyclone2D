package c2d.lang.math;

import c2d.lang.util.debug.C2D_Debug;

/**
 * 不具备排序功能的动态对象数组，相当于Java中的Vector，但是更为有效
 * @author AndrewFan
 *
 */
public class C2D_Array
{
	protected int m_capacity;  // 容量
	public Object m_datas[];// 内部数组
	protected int m_length;// 存放的元素个数
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
	 * 克隆自身
	 * @return 新的克隆对象
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
	 * 增加元素
	 * @param element
	 */
	public void addElement(Object element)
	{
		increaseCapacity();
		// 赋值
		m_datas[m_length] = element;
		m_length++;
	}

	/**
	 * 插入元素
	 * @param index 插入的位置
	 * @param element 插入的对象
	 * @return 返回是否成功插入
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
		// 向后拷贝
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
	 * 增长容量
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
	 * 清空对象数组
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
	 * 清空对象数组，同removeAllElements()
	 */
	public void clear()
	{
		removeAllElements();
	}

	/**
	 * 是否包含某个对象
	 * @param element  包含的对象
	 * @return 是否包含
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
	 * 取位于某个下标的对象
	 * @param index 对象下标
	 * @return 对应的对象
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
	 * 判断某个值位于当前Vector中的什么位置
	 * @param element 要判断的对象
	 * @return 对象在当前数组中的位置
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
	 * 判断当前Vector是否为空
	 * @return Vector是否为空
	 */
	public boolean isEmpty()
	{
		return m_length == 0;
	}

	/**
	 * 移除指定位置的对象，后续对象会依次上移
	 * @param index
	 * @return 是否成功移除
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
	 *  删除某个对象
	 * @param element 要删除的对象
	 * @return 是否成功删除
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
	 *  删除尾部对象
	 * @return 是否成功删除
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
	 * 在index位置重新设置对象
	 * @param index 指定的下标
	 * @param element 重新设置的对象
	 * @return 是否成功设置
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
	 * 获取大小
	 * @return 大小
	 */
	public int size()
	{
		return m_length;
	}

	/**
	 * 显示此数组，每个对象之间以逗号分隔
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
	 * 显示此数组，每个对象一行
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
