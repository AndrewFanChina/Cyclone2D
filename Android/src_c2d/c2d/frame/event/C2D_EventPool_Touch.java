package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.type.C2D_TouchData;

public class C2D_EventPool_Touch extends C2D_EventPool
{

	public C2D_EventPool_Touch(C2D_Widget carrier)
	{
		super(carrier);
	}
	/**
	 * 被系统调用执行事件，执行之后会自动删除执行完毕的事件
	 * @param keyCode 有效按键
	 */
	public final void onCalled(C2D_TouchData tochData)
	{
		C2D_Array events = eventList;
		if(events!=null)
		{
			for(int i=0;i<events.size();i++)
			{
				C2D_EventMsg message = (C2D_EventMsg)events.elementAt(i);
				if(message!=null && message.m_event!=null)
				{
					C2D_Event_Touch event = (C2D_Event_Touch)message.m_event;
					if(event.doEvent(tochData))
					{
						events.removeElementAt(i);
						i--;
					}
				}

			}
		}
	}
}
