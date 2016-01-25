package c2d.mod.ai;

import c2d.lang.math.C2D_SortableArray;
import c2d.lang.obj.C2D_Object;
/**
 * ��Ϣ����
 * @author AndrewFan
 *
 */
public class C2D_MsgQueue extends C2D_Object
{
	private C2D_SortableArray m_msgQ = new C2D_SortableArray();

	/**
	 * ������Ϣ
	 * 
	 * @param msg
	 *            ��Ϣ
	 */
	public void receiveMsg(C2D_Msg msg)
	{
		if (msg == null)
		{
			return;
		}
		m_msgQ.addElement(msg);
	}

	/**
	 * �Ƴ���Ϣ
	 * 
	 * @param msg
	 *            ��Ϣ
	 * @return �Ƿ�ɹ��Ƴ�
	 */
	public boolean removeMsg(C2D_Msg msg)
	{
		if (msg == null)
		{
			return false;
		}
		return m_msgQ.remove(msg);
	}

	public void onRelease()
	{
		if (m_msgQ != null)
		{
			int size = m_msgQ.size();
			for (int i = 0; i < size; i++)
			{
				C2D_Msg msg = (C2D_Msg) m_msgQ.elementAt(i);
				if (msg != null)
				{
					msg.doRelease();
				}
			}
			m_msgQ.clear();
		}
	}

	/**
	 * ������е���Ϣ
	 */
	public void clear()
	{
		m_msgQ.clear();
	}

	/**
	 * ��ö��еĴ�С
	 * 
	 * @return ���д�С
	 */
	public int size()
	{
		return m_msgQ.size();
	}

	/**
	 * �Ӷ���ͷ���Ƴ�һ����Ϣ
	 * 
	 * @return ���Ƴ�����Ϣ
	 */
	public C2D_Msg remove()
	{
		if (m_msgQ.size() <= 0)
		{
			return null;
		}
		C2D_Msg msg = (C2D_Msg) m_msgQ.elementAt(0);
		m_msgQ.removeElementAt(0);
		return msg;
	}
	/**
	 * �Ƴ�ָ��λ�õĵ�Ԫ
	 * @param id ָ����λ��
	 * @return �Ƿ�ɹ��Ƴ�
	 */
	public boolean removeAt(int id)
	{
		return m_msgQ.removeElementAt(id);
	}
	/**
	 * �����а������ȼ���С����С������������
	 */
	public void sort()
	{
		m_msgQ.quickSort2();
	}
	/**
	 * ���ͷ����Ԫ
	 * @return ͷ����Ԫ
	 */
	public C2D_Msg peekFront()
	{
		return (C2D_Msg)m_msgQ.elementAt(0);
	}
	/**
	 * ���λ��ָ���±�ĵ�Ԫ
	 * @param id ָ���±�
	 * @return ��Ԫ
	 */
	public C2D_Msg elementAt(int id)
	{
		return (C2D_Msg)m_msgQ.elementAt(id);
	}

}
