package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Array;
import c2d.lang.obj.C2D_Object;

public abstract class C2D_EventPool extends C2D_Object
{
	/** �¼����� */
	protected C2D_Widget m_carrier;
	/** �¼��б� */
	protected C2D_Array eventList = new C2D_Array();

	/**
	 * ����һ���ؼ����崴�������¼���
	 * 
	 * @param carrier
	 */
	public C2D_EventPool(C2D_Widget carrier)
	{
		m_carrier = carrier;
	}

	/**
	 * �����¼����������ظ�
	 * 
	 * @param event
	 *            �¼�
	 * @return �Ƿ�ɹ����ӣ�false��ʾ�����ظ���δ��ִ������
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
	 * �Ƴ��¼�
	 * 
	 * @param event
	 *            �¼�
	 * @return �Ƿ�ɹ��Ƴ�������false��ʾû���ҵ�
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
	 * ��������¼�
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
