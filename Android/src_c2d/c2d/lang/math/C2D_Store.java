package c2d.lang.math;

/**
 * ת�ƴ洢�࣬����һ��C2D_Array�������ڲ���������һ��C2D_Array�����ڴ洢��
 * ���������С�ת�桱�����Ķ��󣬡�ת�桱��������ɾ����������ã����ǽ����ǼĴ���������Ǹ�
 * C2D_Array�ݴ������С���ˣ��ڽ����Ҫ��ʱ�򣬿��Դ��ݴ������ָ����������������
 * �������Ǿ��������ڴ���ظ�ж�غʹ����������removeXXX������Ȼ��C2D_Array
 * ������ͬ��������Ա������������ģ������Ҫ��ת�桱������Ҫʹ��storeXXX������
 * 
 * @author AndrewFan
 * 
 */
public class C2D_Store extends C2D_Array
{
	private C2D_Array storageVector = new C2D_Array();
	/**
	 * ��¡����
	 * @return �µĿ�¡����
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
	 * ת�ƴ洢ָ��λ�õĶ��󣬺����������������
	 * 
	 * @param index
	 * @return �Ƿ�ɹ�ת��
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
	 * ת��ĳ�����󣬼��ӱ�������ת�Ƶ��ݴ�����
	 * 
	 * @param obj
	 *            Ҫת��Ķ���
	 * @return �Ƿ�ɹ�ת��
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
	 * ת�����������е����е�Ԫ
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
	 * ��������ݴ�ĵ�Ԫ����Ὣ����ݴ�����ͷ���ָ�������������ĩβ�����ҷ�����
	 * 
	 * @return ԭ�����ݴ�ĵ�Ԫ
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
	 * ��ȡ�ݴ��е�Ԫ�ĸ���
	 * 
	 * @return ��Ԫ����
	 */
	public int getStoredCount()
	{
		return storageVector.size();
	}

	/**
	 * ��ȡ����һ�����ݴ�ĵ�Ԫ
	 * 
	 * @return ���ݴ�ĵ�Ԫ
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
	 * ���ݴ�������ɾ��ָ���ı��ݴ�ĵ�Ԫ
	 * 
	 * @param element
	 *            ָ��Ҫɾ���ĵ�Ԫ
	 * @return �Ƿ�ɹ�ɾ��
	 */
	public boolean removeStoredElement(C2D_Order element)
	{
		return storageVector.remove(element);
	}

	/**
	 * ����ݴ�����
	 */
	public void clearStorage()
	{
		storageVector.removeAllElements();
	}

	/**
	 * ��ձ����������ݴ�������ע��˷�����removeAllElements()������ removeAllElements()ֻ����ձ���������
	 */
	public void clearAll()
	{
		removeAllElements();
		storageVector.removeAllElements();
	}
	/**
	 * �������ݴ�ĵ�Ԫ�ָ��ɻ��Ԫ
	 */
	public void recoverAll()
	{
		recoverAll(null);
	}
	/**
	 * �������ݴ�ĵ�Ԫ�ָ��ɻ��Ԫ
	 * @param recoverList ���ָ��ĵ�Ԫ�����洢���б��з���,����Ϊnull
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
