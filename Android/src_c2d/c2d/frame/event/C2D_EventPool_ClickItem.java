package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.frame.com.list.scroll.C2D_ScrollView;
import c2d.lang.math.C2D_Array;

public class C2D_EventPool_ClickItem extends C2D_EventPool
{
	public C2D_EventPool_ClickItem(C2D_Widget carrier)
	{
		super(carrier);
	}
	/**
	 * 被系统调用执行事件，执行之后不会删除执行完毕的事件
	 * @param itemID 代表被点击的条目ID
	 */
	public final boolean onCalled(int itemID)
	{
		C2D_Array events = eventList;
		int responsedNum=0;
		if(events!=null)
		{
			for(int i=0;i<events.size();i++)
			{
				C2D_EventMsg message = (C2D_EventMsg)events.elementAt(i);
				if(message!=null && message.m_event!=null)
				{
					C2D_Event_ClickItem event = (C2D_Event_ClickItem)message.m_event;
					if(event.doEvent((C2D_ScrollView)message.m_carrier,itemID))
					{
//						events.removeElementAt(i);
//						i--;
						responsedNum++;
					}
				}

			}
		}
		return responsedNum>0;
	}
	/**
	 * 增加事件，不允许重复
	 * @param event 事件
	 * @return 是否成功增加，false表示发现重复，未能执行增加
	 */
	public boolean add(C2D_Event_ClickItem event)
	{
		return super.add(event);
	}
}
