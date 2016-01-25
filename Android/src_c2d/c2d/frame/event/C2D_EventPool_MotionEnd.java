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
	 * ��ϵͳ����ִ���¼���ִ��֮����Զ�ɾ��ִ����ϵ��¼�
	 */
	public final void onCalled(C2D_Motion motion)
	{
		C2D_Array events = eventList;
		if(events!=null)
		{
			m_eventsBuf.clear();
			//����¼�
			for (int i = 0; i < events.size(); i++)
			{
				m_eventsBuf.addElement(events.elementAt(i));
			}
			//��ʼִ��
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
			//����¼�
			m_eventsBuf.clear();
		}
	}
	/**
	 * �����¼����������ظ�
	 * @param event �¼�
	 * @return �Ƿ�ɹ����ӣ�false��ʾ�����ظ���δ��ִ������
	 */
	public boolean add(C2D_Event_MotionEnd event)
	{
		return super.add(event);
	}
}
