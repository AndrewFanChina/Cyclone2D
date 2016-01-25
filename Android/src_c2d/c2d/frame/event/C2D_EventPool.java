package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Array;
import c2d.lang.obj.C2D_Object;

public abstract class C2D_EventPool extends C2D_Object
{
	/** 事件载体 */
	protected C2D_Widget m_carrier;
	/** 事件列表 */
	protected C2D_Array eventList = new C2D_Array();

	/**
	 * 基于一个控件载体创建它的事件池
	 * 
	 * @param carrier
	 */
	public C2D_EventPool(C2D_Widget carrier)
	{
		m_carrier = carrier;
	}

	/**
	 * 增加事件，不允许重复
	 * 
	 * @param event
	 *            事件
	 * @return 是否成功增加，false表示发现重复，未能执行增加
	 */
	protected boolean add(C2D_Event event)
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
		C2D_EventMsg message = C2D_EventMsg.create(m_carrier, event);
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
		if(eventList!=null)
		{
			int size=eventList.size();
			for (int i = 0; i < size; i++)
			{
				C2D_EventMsg ei = (C2D_EventMsg)eventList.elementAt(i);
				if(ei!=null)
				{
					ei.doRelease(this);
				}
			}
			eventList.clear();
		}
	}

	public void onRelease()
	{
		m_carrier = null;
		clear();
		eventList = null;
	}
}
