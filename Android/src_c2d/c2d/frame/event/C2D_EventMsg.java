package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.lang.obj.C2D_Object;

class C2D_EventMsg extends C2D_Object
{
	C2D_Widget m_carrier;
	C2D_Event m_event;
	private C2D_EventMsg()
	{
	}
	/**
	 * 创建事件消息，必须给定存在的载体和事件
	 * @param carrier 事件载体，允许为空
	 * @param event   事件对象，不允许为空
	 * @return 事件消息
	 */
	static C2D_EventMsg create(C2D_Widget carrier,C2D_Event event)
	{
		if(event==null)
		{
			return null;
		}
		C2D_EventMsg message=new C2D_EventMsg();
		message.m_carrier=carrier;
		message.m_event=event;
		return message;
	}
	public void onRelease()
	{
		m_carrier=null;
		m_event=null;
	}
}
