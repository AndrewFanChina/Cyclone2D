package c2d.frame.event;

import c2d.frame.base.C2D_Scene;
import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Array;

public class C2D_EventPool_Scene extends C2D_EventPool
{
	public C2D_EventPool_Scene(C2D_Widget carrier)
	{
		super(carrier);
	}
	/**
	 * 被系统调用执行事件，执行之后会自动删除执行完毕的事件
	 * @param sceneState 场景的状态标志
	 */
	public final void onCalled(int sceneState)
	{
		C2D_Array events = eventList;
		if(events!=null)
		{
			for(int i=0;i<events.size();i++)
			{
				C2D_EventMsg message = (C2D_EventMsg)events.elementAt(i);
				if(message!=null && message.m_event!=null)
				{
					C2D_Event_Scene event = (C2D_Event_Scene)message.m_event;
					if(event.doEvent((C2D_Scene)(message.m_carrier),sceneState))
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
	public boolean add(C2D_Event_Scene event)
	{
		return super.add(event);
	}
}
