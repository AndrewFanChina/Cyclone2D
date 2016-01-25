package c2d.lang.math;

import java.util.Enumeration;
import java.util.Hashtable;
/**
 * һ�������洢�ݴ��Ĺ�ϣ��
 * �ڲ����ݴ��ʹ������key�����������Է���洢���ݴ����
 * �����ڲ���ά����һ����̬���飬�����˵�ǰ���з��ݴ�Ķ��󼯺ϡ�
 * @author AndrewFan
 *
 */
public class C2D_StoreTable
{
	private Hashtable m_storeTable = new Hashtable();
	private static C2D_HashKey M_KEY = new C2D_HashKey(1);
	/** ��ǰ������ϣ���������ݴ���еĻ����ļ��� */
	private C2D_Array m_aliveList=new C2D_Array();
	
	/**
	 * ��Ӷ�������Ӧ���ݴ������͵�ǰ�Ļ��������ӣ�
	 * �������ظ�����Ѿ������ڻ�б���ִ߲������еĶ���
	 * @param obj ָ���Ķ���
	 * @param typeID ��������
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
	 * ת��ĳ�����󣬼��ӱ�������ת�Ƶ��ݴ�����
	 * 
	 * @param obj
	 *            Ҫת��Ķ���
	 * @param typeID
	 *            ����ID
	 * @return �Ƿ�ɹ�ת��
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
	 * �ݴ����������еĻ��Ԫ
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
	 * �Ӷ�Ӧ���ݴ������У��ָ�һ�����ݴ�Ķ��󣬱�ɻ����
	 * 
	 * @param typeID
	 *            ����ID
	 * @return ԭ�����ݴ�ĵ�Ԫ
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
	 * ��ȡָ�����͵Ļ��Ԫ����Ŀ
	 * 
	 * @param typeID
	 *            ����ID
	 * @return ���Ԫ����Ŀ
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
	 * ���������е��ݴ������е��ݴ浥Ԫ�ָ��ɻ��Ԫ
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
	 * ��ȡ��Ĵ�С
	 * 
	 * @return ��Ĵ�С
	 */
	public int size()
	{
		return m_aliveList.size();
	}

	/**
	 * ���ݴ�������ɾ��ָ���ı��ݴ�ĵ�Ԫ
	 * 
	 * @param typeID
	 *            ����ID
	 * @param element
	 *            ָ��Ҫɾ���ĵ�Ԫ
	 * @return �Ƿ�ɹ�ɾ��
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
	 * ����ݴ�����
	 * 
	 * @param typeID
	 *            ����ID
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
	 * ��ձ����������ݴ�������ע��˷�����removeAllElements()������ removeAllElements()ֻ����ձ���������
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
	 * ���ط��ݴ����ļ���
	 * 
	 * @return ���ݴ����ļ���
	 */
	public C2D_Array getAliveList()
	{
		return m_aliveList;
	}
	/**
	 * ���ָ���ĵ�Ԫ
	 * @param id ��ԪID
	 * @return ��Ԫ����
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
	 * ��ȡ�ִ��е�ָ�����͵ĵ�Ԫ��Ŀ
	 * @param typeID ��Ԫ����
	 * @return �ִ��е�ָ�����͵ĵ�Ԫ��Ŀ
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
