package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Array;

public class C2D_EventPool_ChangeFocus extends C2D_EventPool
{

	public C2D_EventPool_ChangeFocus(C2D_Widget carrier)
	{
		super(carrier);
	}
	/**
	 * 被系统调用执行事件，执行之后会自动删除执行完毕的事件
	 * @param focused 是否被选中
	 */
	public final void onCalled(boolean focused,C2D_Widget another)
	{
		C2D_Array events = eventList;
		if(events!=null)
		{
			for(int i=0;i<events.size();i++)
			{
				C2D_EventMsg message = (C2D_EventMsg)events.elementAt(i);
				if(message!=null && message.m_event!=null)
				{
					C2D_Event_ChangeFocus event = ((C2D_Event_ChangeFocus)message.m_event);
					if(event.doEvent(message.m_carrier,focused,another))
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
	 * @param event 事件
	 * @return 是否成功增加，false表示发现重复，未能执行增加
	 */
	public boolean add(C2D_Event_ChangeFocus event)
	{
		return super.add(event);
	}
}
