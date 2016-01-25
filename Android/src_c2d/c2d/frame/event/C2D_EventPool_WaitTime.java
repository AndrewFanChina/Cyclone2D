package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Array;

public class C2D_EventPool_WaitTime extends C2D_EventPool
{
	public C2D_EventPool_WaitTime(C2D_Widget carrier)
	{
		super(carrier);
	}
	/**
	 * ��ϵͳ����ִ���¼���ִ��֮����Զ�ɾ��ִ����ϵ��¼�
	 * @param passedTime ��ȥ��ʱ��
	 */
	public final void onCalled(int passedTime)
	{
		C2D_Array events = eventList;
		if(events!=null)
		{
			for(int i=0;i<events.size();i++)
			{
				C2D_EventMsg message = (C2D_EventMsg)events.elementAt(i);
				if(message!=null && message.m_event!=null)
				{
					C2D_Event_WaitTime event = (C2D_Event_WaitTime)message.m_event;
					if(event.m_waitTime>0 && event.m_passedTime<event.m_waitTime)
					{
						event.m_passedTime+=passedTime;
						if(event.m_passedTime>=event.m_waitTime)
						{
							if(event.doEvent(message.m_carrier))
							{
								events.removeElementAt(i);
								i--;
							}
							else
							{
								event.m_waitTime%=event.m_waitTime;
							}
						}
					}
				}

			}
		}
	}
	/**
	 * �����¼����������ظ�
	 * @param event �¼�
	 * @return �Ƿ�ɹ����ӣ�false��ʾ�����ظ���δ��ִ������
	 */
	public boolean add(C2D_Event_WaitTime event)
	{
		return super.add(event);
	}
}
