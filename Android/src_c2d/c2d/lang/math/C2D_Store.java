package c2d.lang.math;

/**
 * 转移存储类，基于一个C2D_Array容器，内部含有另外一个C2D_Array，用于存储从
 * 本地容器中“转存”出来的对象，“转存”操作不会删除对象的引用，而是将它们寄存在另外的那个
 * C2D_Array暂存容器中。因此，在今后需要的时候，可以从暂存容器恢复到本地容器。这个
 * 类作用是尽力避免内存的重复卸载和创建。本身的removeXXX操作仍然与C2D_Array
 * 本身相同，都是针对本地容器操作的，如果需要“转存”，这需要使用storeXXX操作。
 * 
 * @author AndrewFan
 * 
 */
public class C2D_Store extends C2D_Array
{
	private C2D_Array storageVector = new C2D_Array();
	/**
	 * 克隆自身
	 * @return 新的克隆对象
	 */
	public C2D_Array cloneSelf()
	{
		C2D_Store newInstance = new C2D_Store();
		newInstance.m_capacity = m_capacity;
		newInstance.m_length = m_length;
		newInstance.m_datas = new C2D_Order[m_datas.length];
		System.arraycopy(m_datas, 0, newInstance.m_datas, 0, m_datas.length);
		newInstance.storageVector=storageVector.cloneSelf();
		return newInstance;
	}
	/**
	 * 转移存储指定位置的对象，后续对象会依次上移
	 * 
	 * @param index
	 * @return 是否成功转存
	 */
	public boolean storeElementAt(int index)
	{
		if (index < 0 || index >= m_length)
		{
			return false;
		}
		storageVector.addElement(m_datas[index]);
		for (int i = index; i <= m_length - 2; i++)
		{
			m_datas[i] = m_datas[i + 1];
		}
		m_datas[m_length - 1] = null;
		m_length--;
		return true;
	}

	/**
	 * 转存某个对象，即从本地容器转移到暂存容器
	 * 
	 * @param obj
	 *            要转存的对象
	 * @return 是否成功转存
	 */
	public boolean store(C2D_Order obj)
	{
		int index = indexOf(obj);
		if (index < 0)
		{
			return false;
		}
		
		return storeElementAt(index);
	}

	/**
	 * 转存整个容器中的所有单元
	 */
	public void storeAllElements()
	{
		for (int i = 0; i < m_length; i++)
		{
			storageVector.addElement(m_datas[i]);
			m_datas[i] = null;
		}
		m_length = 0;
	}

	/**
	 * 如果存在暂存的单元，则会将其从暂存容器头部恢复到本地容器的末尾，并且返回它
	 * 
	 * @return 原来被暂存的单元
	 */
	public Object recoverElement()
	{
		Object storaged = null;
		if (storageVector.size() > 0)
		{
			storaged = storageVector.elementAt(0);
			storageVector.removeElementAt(0);
			addElement(storaged);
		}
		return storaged;
	}

	/**
	 * 获取暂存中单元的个数
	 * 
	 * @return 单元个数
	 */
	public int getStoredCount()
	{
		return storageVector.size();
	}

	/**
	 * 获取任意一个被暂存的单元
	 * 
	 * @return 被暂存的单元
	 */
	public Object getStoredElement()
	{
		Object storaged = null;
		if (storageVector.size() > 0)
		{
			storaged = storageVector.elementAt(0);
		}
		return storaged;
	}

	/**
	 * 从暂存容器中删除指定的被暂存的单元
	 * 
	 * @param element
	 *            指定要删除的单元
	 * @return 是否成功删除
	 */
	public boolean removeStoredElement(C2D_Order element)
	{
		return storageVector.remove(element);
	}

	/**
	 * 清空暂存容器
	 */
	public void clearStorage()
	{
		storageVector.removeAllElements();
	}

	/**
	 * 清空本地容器和暂存容器，注意此方法与removeAllElements()的区别， removeAllElements()只能清空本地容器。
	 */
	public void clearAll()
	{
		removeAllElements();
		storageVector.removeAllElements();
	}
	/**
	 * 将所有暂存的单元恢复成活动单元
	 */
	public void recoverAll()
	{
		recoverAll(null);
	}
	/**
	 * 将所有暂存的单元恢复成活动单元
	 * @param recoverList 被恢复的单元将被存储在列表中返回,可以为null
	 */
	public void recoverAll(C2D_Array recoverList)
	{
		if(storageVector==null)
		{
			return;
		}
		int size=storageVector.size();
		if ( size> 0)
		{
			for (int i = 0; i < size; i++)
			{
				Object storaged = storageVector.elementAt(0);
				storageVector.removeElementAt(0);
				addElement(storaged);
				if(recoverList!=null)
				{
					recoverList.addElement(storaged);
				}
			}
		}
	}
}
