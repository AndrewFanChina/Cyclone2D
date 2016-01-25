package c2d.lang.math;

import java.util.Enumeration;
import java.util.Hashtable;
/**
 * 一个用来存储暂存表的哈希表。
 * 内部的暂存表使用整形key进行索引，以分类存储和暂存对象。
 * 另外内部还维护了一个动态数组，保存了当前所有非暂存的对象集合。
 * @author AndrewFan
 *
 */
public class C2D_StoreTable
{
	private Hashtable m_storeTable = new Hashtable();
	private static C2D_HashKey M_KEY = new C2D_HashKey(1);
	/** 当前整个哈希表下所有暂存表中的活动对象的集合 */
	private C2D_Array m_aliveList=new C2D_Array();
	
	/**
	 * 添加对象，在相应的暂存容器和当前的活动集合中添加，
	 * 不允许重复添加已经存在于活动列表或者仓储容器中的对象
	 * @param obj 指定的对象
	 * @param typeID 对象类型
	 */
	public void addElement(C2D_Order obj, int typeID)
	{
		if(m_aliveList.contains(obj))
		{
			return;
		}
		M_KEY.setValue(typeID);
		C2D_Store store = (C2D_Store) m_storeTable.get(M_KEY);
		if (store == null)
		{
			store = new C2D_Store();
			m_storeTable.put(new C2D_HashKey(typeID), store);
		}
		if(store.contains(obj))
		{
			return;
		}
		store.addElement(obj);
		m_aliveList.addElement(obj);
	}
	/**
	 * 转存某个对象，即从本地容器转移到暂存容器
	 * 
	 * @param obj
	 *            要转存的对象
	 * @param typeID
	 *            类型ID
	 * @return 是否成功转存
	 */
	public boolean store(C2D_Order obj, int typeID)
	{
		M_KEY.setValue(typeID);
		C2D_Store store = (C2D_Store) m_storeTable.get(M_KEY);
		if (store == null)
		{
			return false;
		}
		boolean stored =  store.store(obj);
		if(stored)
		{
			m_aliveList.remove(obj);
		}
		return stored;
	}

	/**
	 * 暂存所有容器中的活动单元
	 */
	public void storeAllElements()
	{
		Enumeration er = m_storeTable.elements();
		while (er.hasMoreElements())
		{
			C2D_Store store = (C2D_Store) er.nextElement();
			if (store != null)
			{
				store.storeAllElements();
			}
		}
		m_aliveList.removeAllElements();
	}

	/**
	 * 从对应的暂存容器中，恢复一个被暂存的对象，变成活动对象
	 * 
	 * @param typeID
	 *            类型ID
	 * @return 原来被暂存的单元
	 */
	public Object recoverElement(int typeID)
	{
		M_KEY.setValue(typeID);
		C2D_Store store = (C2D_Store) m_storeTable.get(M_KEY);
		if (store == null)
		{
			return null;
		}
		Object recovered = store.recoverElement();
		if(recovered!=null)
		{
			m_aliveList.addElement(recovered);
		}
		return recovered;
		
	}
	/**
	 * 获取指定类型的活动单元的数目
	 * 
	 * @param typeID
	 *            类型ID
	 * @return 活动单元的数目
	 */
	public int getSize(int typeID)
	{
		M_KEY.setValue(typeID);
		C2D_Store store = (C2D_Store) m_storeTable.get(M_KEY);
		if (store == null)
		{
			return 0;
		}
		return store.size();
	}
	/**
	 * 将表中所有的暂存容器中的暂存单元恢复成活动单元
	 */
	public void recoverAll()
	{
		Enumeration er = m_storeTable.elements();
		while (er.hasMoreElements())
		{
			C2D_Store store = (C2D_Store) er.nextElement();
			if (store != null)
			{
				store.recoverAll(m_aliveList);
			}
		}
	}
	/**
	 * 获取表的大小
	 * 
	 * @return 表的大小
	 */
	public int size()
	{
		return m_aliveList.size();
	}

	/**
	 * 从暂存容器中删除指定的被暂存的单元
	 * 
	 * @param typeID
	 *            类型ID
	 * @param element
	 *            指定要删除的单元
	 * @return 是否成功删除
	 */
	public boolean removeStoredElement(C2D_Order element, int typeID)
	{
		M_KEY.setValue(typeID);
		C2D_Store store = (C2D_Store) m_storeTable.get(M_KEY);
		if (store == null)
		{
			return false;
		}
		boolean removed = store.removeStoredElement(element);
		if(removed)
		{
			m_aliveList.remove(element);
		}
		return removed;
	}

	/**
	 * 清空暂存容器
	 * 
	 * @param typeID
	 *            类型ID
	 */
	public void clearStorage(int typeID)
	{
		M_KEY.setValue(typeID);
		C2D_Store store = (C2D_Store) m_storeTable.get(M_KEY);
		if (store == null)
		{
			return;
		}
		store.clearStorage();
	}

	/**
	 * 清空本地容器和暂存容器，注意此方法与removeAllElements()的区别， removeAllElements()只能清空本地容器。
	 */
	public void clearAll()
	{
		Enumeration er = m_storeTable.elements();
		while (er.hasMoreElements())
		{
			C2D_Store store = (C2D_Store) er.nextElement();
			if (store != null)
			{
				store.clearAll();
			}
		}
		m_aliveList.clear();
	}

	/**
	 * 返回非暂存对象的集合
	 * 
	 * @return 非暂存对象的集合
	 */
	public C2D_Array getAliveList()
	{
		return m_aliveList;
	}
	/**
	 * 获得指定的单元
	 * @param id 单元ID
	 * @return 单元内容
	 */
	public Object elementAt(int id)
	{
		if(m_aliveList==null)
		{
			return null;
		}
		return m_aliveList.elementAt(id);
	}
	/**
	 * 获取仓储中的指定类型的单元数目
	 * @param typeID 单元类型
	 * @return 仓储中的指定类型的单元数目
	 */
	public int getStoredSize(int typeID)
	{
		M_KEY.setValue(typeID);
		C2D_Store store = (C2D_Store) m_storeTable.get(M_KEY);
		if (store == null)
		{
			return 0;
		}
		return store.getStoredCount();
	}
}
