package c2d.mod.ai;

import c2d.frame.com.data.unit_type.C2D_UnitValue;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Order;

/**
 * 消息分选器 用于将不同类型的消息分类存储，处理和执行
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_MsgFilter implements C2D_Order
{
	/** 工作队列 */
	private C2D_MsgQueue m_QueWork = new C2D_MsgQueue();
	/** 空闲队列 */
	private C2D_Array m_QueIdle = new C2D_Array();
	/** 消息管理器 */
	private C2D_MsgManager m_manager;
	/** 消息类型编号 */
	int m_msgType;
	/** 本类消息的最多数目 */
	int m_maxNumber;
	/** 本类消息的最大停留时间(毫秒) */
	int m_timeRemain;
	/** 消息分选器的优先级 */
	int m_priority;
	/** 是否对优先级敏感 */
	private boolean m_sortByPriority;
	/**
	 * 构建消息分选器
	 * 
	 * @param msgType
	 *            消息类型编号
	 * @param maxNumber
	 *            本类消息的最多数目
	 * @param timeRemain
	 *            本类消息的最大停留时间(单位-秒)
	 * @param priority
	 *            本类消息的优先级
	 */
	public C2D_MsgFilter(int msgType, int maxNumber, int timeRemain,int priority)
	{
		m_msgType = msgType;
		m_maxNumber = maxNumber;
		m_timeRemain = timeRemain*1000;
		m_priority = priority;
	}
	/**
	 * 设置管理器
	 * @param manager 管理器
	 */
	void setManager(C2D_MsgManager manager)
	{
		m_manager = manager;
	}
	/**
	 * 设置在处理时，是否排序优先级
	 * @param sortByPriority
	 */
	public void setSortable(boolean sortByPriority)
	{
		m_sortByPriority=sortByPriority;
	}
	/**
	 * 广播消息
	 * 
	 * @param msgID
	 *            消息ID
	 * @param msgParam
	 *            消息参数
	 * @param msgPriority
	 *            消息优先级
	 */
	public void broadCast(int msgID, C2D_UnitValue msgParam,int msgPriority)
	{
		if (m_manager == null)
		{
			return;
		}
		//封装成完整的消息
		C2D_Msg msg = (C2D_Msg) m_manager.m_msgCreator.recoverElement();
		if (msg == null)
		{
			msg = new C2D_Msg(msgID, msgParam,msgPriority);
			m_manager.m_msgCreator.addElement(msg);
		}
		else
		{
			msg.setValue(msgID, msgParam,msgPriority);
		}
		//将消息加入队列
		if (m_manager.m_broadcasting)
		{
			m_QueWork.receiveMsg(msg);
		}
		else
		{
			m_QueIdle.addElement(msg);
		}
	}
	/**
	 * 广播消息，使用优先级参数0
	 * 
	 * @param msgID
	 *            消息ID
	 * @param msgParam
	 *            消息参数
	 */
	public void broadCast(int msgID, C2D_UnitValue msgParam)
	{
		broadCast(msgID, msgParam,0);
	}
	/**
	 * 处理消息队列
	 */
	public void processMsgQue(long time)
	{
		//先进行排序，如果对优先级敏感的话
		if(m_sortByPriority)
		{
			m_QueWork.sort();
		}
		// 首先剔除超数的消息
		int size = m_QueWork.size();
		if(size<=0)
		{
			return;
		}
		int delCount = size - m_maxNumber;
		for (int i = 0; i < delCount; i++)
		{
			C2D_Msg msg = m_QueWork.remove();
			m_manager.m_msgCreator.store(msg);
		}
		// 接着剔除过期的消息
		size = m_QueWork.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Msg msg = m_QueWork.elementAt(i);
			if(msg==null)
			{
				break;
			}
			if(time - msg.m_timeOccured>m_timeRemain)
			{
				m_QueWork.removeAt(i);
				m_manager.m_msgCreator.store(msg);
				i--;
			}
		}
		//进行消息的处理
		processMsg(m_QueWork);
		//清理失效的消息
		size = m_QueWork.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Msg msg = m_QueWork.elementAt(i);
			if(msg==null)
			{
				break;
			}
			if(msg.m_valid)
			{
				m_QueWork.removeAt(i);
				m_manager.m_msgCreator.store(msg);
				i--;
			}
		}
	}
	/** 处理消息的方法 */
	protected abstract void processMsg(C2D_MsgQueue msgQue);
	/**
	 * 做好准备，主要工作是将缓冲队列的消息转入工作队列。
	 */
	public void getReady()
	{
		int len = m_QueIdle.size();
		for (int i = 0; i < len; i++)
		{
			C2D_Msg msg = (C2D_Msg) m_QueIdle.elementAt(i);
			m_QueWork.receiveMsg(msg);
		}
		m_QueIdle.clear();
	}
	

	public int getOrderValue()
	{
		return m_priority;
	}
}
