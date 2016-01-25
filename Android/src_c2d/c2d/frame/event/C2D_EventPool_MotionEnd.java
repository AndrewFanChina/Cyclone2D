package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Array;
import c2d.mod.physic.C2D_Motion;

public class C2D_EventPool_MotionEnd extends C2D_EventPool
{
	public C2D_EventPool_MotionEnd(C2D_Widget carrier)
	{
		super(carrier);
	}
	private static C2D_Array m_eventsBuf=new C2D_Array();
	/**
	 * 被系统调用执行事件，执行之后会自动删除执行完毕的事件
	 */
	public final void onCalled(C2D_Motion motion)
	{
		C2D_Array events = eventList;
		if(events!=null)
		{
			m_eventsBuf.clear();
			//标记事件
			for (int i = 0; i < events.size(); i++)
			{
				m_eventsBuf.addElement(events.elementAt(i));
			}
			//开始执行
			for(int i=0;i<m_eventsBuf.size();i++)
			{
				C2D_EventMsg message = (C2D_EventMsg)m_eventsBuf.elementAt(i);
				if(message!=null && message.m_event!=null)
				{
					C2D_Event_MotionEnd event = (C2D_Event_MotionEnd)message.m_event;
					if(event.doEvent(message.m_carrier))
					{
						events.remove(event);
					}
				}
			}
			//清空事件
			m_eventsBuf.clear();
		}
	}
	/**
	 * 增加事件，不允许重复
	 * @param event 事件
	 * @return 是否成功增加，false表示发现重复，未能执行增加
	 */
	public boolean add(C2D_Event_MotionEnd event)
	{
		return super.add(event);
	}
}
