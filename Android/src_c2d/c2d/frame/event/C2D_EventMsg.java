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
	 * �����¼���Ϣ������������ڵ�������¼�
	 * @param carrier �¼����壬����Ϊ��
	 * @param event   �¼����󣬲�����Ϊ��
	 * @return �¼���Ϣ
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
