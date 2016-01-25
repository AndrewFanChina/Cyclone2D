package c2d.frame.event;

import c2d.frame.base.C2D_Stage;
import c2d.lang.math.C2D_Array;
import c2d.lang.obj.C2D_Object;

public class C2D_EventPool_SysLoop extends C2D_Object
{
	/** 事件载体 */
	protected C2D_Stage m_carrier;
	/** 事件列表 */
	protected C2D_Array eventList = new C2D_Array();

	/**
	 * 基于一个舞台创建它的事件池
	 * 
	 * @param carrier
	 */
	public C2D_EventPool_SysLoop(C2D_Stage carrier)
	{
		m_carrier = carrier;
	}

	/**
	 * 被系统调用执行事件，执行之后会自动删除执行完毕的事件
	 */
	public final void onCalled()
	{
		C2D_Array events = eventList;
		if (events != null)
		{
			for (int i = 0; i < events.size(); i++)
			{
				C2D_EventMsg_SysLoop message = (C2D_EventMsg_SysLoop) events.elementAt(i);
				if (message != null && message.m_event != null)
				{
					C2D_Event_SysLoop event = (C2D_Event_SysLoop) message.m_event;
					if (event.doEvent(message.m_carrier))
					{
						events.removeElementAt(i);
						i--;
					}
				}
			}
		}
	}

	/**
	 * 增加事件，不允许重复
	 * 
	 * @param event
	 *            事件
	 * @return 是否成功增加，false表示发现重复，未能执行增加
	 */
	public boolean add(C2D_Event_Update event)
	{
		return add(event);
	}

	/**
	 * 增加事件，不允许重复
	 * 
	 * @param event
	 *            事件
	 * @return 是否成功增加，false表示发现重复，未能执行增加
	 */
	protected boolean add(C2D_Event_SysLoop event)
	{
		if (event == null)
		{
			return false;
		}
		for (int i = 0; i < eventList.size(); i++)
		{
			C2D_EventMsg messageI = (C2D_EventMsg) eventList.elementAt(i);
			if (messageI != null && messageI.m_event != null && messageI.m_event.equals(event))
			{
				return false;
			}
		}
		C2D_EventMsg_SysLoop message = new C2D_EventMsg_SysLoop(m_carrier, event);
		eventList.addElement(message);
		return true;
	}

	/**
	 * 移除事件
	 * 
	 * @param event
	 *            事件
	 * @return 是否成功移除，返回false表示没有找到
	 */
	public boolean remove(C2D_Event event)
	{
		for (int i = 0; i < eventList.size(); i++)
		{
			C2D_EventMsg messageI = (C2D_EventMsg) eventList.elementAt(i);
			if (messageI.m_carrier.equals(m_carrier))
			{
				eventList.removeElementAt(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * 清除所有事件
	 */
	public void clear()
	{
		eventList.removeAllElements();
	}

	public void onRelease()
	{
		m_carrier = null;
		if (eventList != null)
		{
			eventList.clear();
			eventList = null;
		}
	}

	class C2D_EventMsg_SysLoop extends C2D_Object
	{
		C2D_Stage m_carrier;
		C2D_Event_SysLoop m_event;

		/**
		 * 创建事件消息，必须给定存在的载体和事件
		 * 
		 * @param carrier
		 *            事件载体，允许为空
		 * @param event
		 *            事件对象，不允许为空
		 * @return 事件消息
		 */
		C2D_EventMsg_SysLoop(C2D_Stage carrier, C2D_Event_SysLoop event)
		{
			m_carrier = carrier;
			m_event = event;
		}

		public void onRelease()
		{
			m_carrier = null;
			m_event = null;
		}
	}
	abstract class C2D_Event_SysLoop
	{
		C2D_Stage m_carrier;

		/**
		 * 创建事件消息，必须给定存在的载体和事件
		 * 
		 * @param carrier
		 *            事件载体，允许为空
		 * @return 事件消息
		 */
		public C2D_Event_SysLoop(C2D_Stage carrier)
		{
			m_carrier = carrier;
		}

		public abstract boolean doEvent(C2D_Stage m_carrier2);

		public boolean doRelease()
		{
			m_carrier = null;
			return true;
		}
	}
}
