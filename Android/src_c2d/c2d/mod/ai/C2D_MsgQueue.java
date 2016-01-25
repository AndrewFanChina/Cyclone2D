package c2d.mod.ai;

import c2d.lang.math.C2D_SortableArray;
import c2d.lang.obj.C2D_Object;
/**
 * 消息队列
 * @author AndrewFan
 *
 */
public class C2D_MsgQueue extends C2D_Object
{
	private C2D_SortableArray m_msgQ = new C2D_SortableArray();

	/**
	 * 接收消息
	 * 
	 * @param msg
	 *            消息
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
	 * 移除消息
	 * 
	 * @param msg
	 *            消息
	 * @return 是否成功移除
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
	 * 清除所有的消息
	 */
	public void clear()
	{
		m_msgQ.clear();
	}

	/**
	 * 获得队列的大小
	 * 
	 * @return 队列大小
	 */
	public int size()
	{
		return m_msgQ.size();
	}

	/**
	 * 从队列头部移除一个消息
	 * 
	 * @return 被移除的消息
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
	 * 移除指定位置的单元
	 * @param id 指定的位置
	 * @return 是否成功移除
	 */
	public boolean removeAt(int id)
	{
		return m_msgQ.removeElementAt(id);
	}
	/**
	 * 将队列按照优先级大小（从小到大）重新排序
	 */
	public void sort()
	{
		m_msgQ.quickSort2();
	}
	/**
	 * 获得头部单元
	 * @return 头部单元
	 */
	public C2D_Msg peekFront()
	{
		return (C2D_Msg)m_msgQ.elementAt(0);
	}
	/**
	 * 获得位于指定下标的单元
	 * @param id 指定下标
	 * @return 单元
	 */
	public C2D_Msg elementAt(int id)
	{
		return (C2D_Msg)m_msgQ.elementAt(id);
	}

}
