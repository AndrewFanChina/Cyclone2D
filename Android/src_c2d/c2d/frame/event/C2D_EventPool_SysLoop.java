package c2d.frame.event;

import c2d.frame.base.C2D_Stage;
import c2d.lang.math.C2D_Array;
import c2d.lang.obj.C2D_Object;

public class C2D_EventPool_SysLoop extends C2D_Object
{
	/** �¼����� */
	protected C2D_Stage m_carrier;
	/** �¼��б� */
	protected C2D_Array eventList = new C2D_Array();

	/**
	 * ����һ����̨���������¼���
	 * 
	 * @param carrier
	 */
	public C2D_EventPool_SysLoop(C2D_Stage carrier)
	{
		m_carrier = carrier;
	}

	/**
	 * ��ϵͳ����ִ���¼���ִ��֮����Զ�ɾ��ִ����ϵ��¼�
	 */
	public final void onCalled()
	{
		C2D_Array events = eventList;
		if (events != null)
		{
			for (int i = 0; i < events.size(); i++)
			{
				C2D_EventMsg_SysLoop message = (C2D_EventMsg_SysLoop) events.elementAt(i);
				if (message != null && message.m_event != null)
				{
					C2D_Event_SysLoop event = (C2D_Event_SysLoop) message.m_event;
					if (event.doEvent(message.m_carrier))
					{
						events.removeElementAt(i);
						i--;
					}
				}
			}
		}
	}

	/**
	 * �����¼����������ظ�
	 * 
	 * @param event
	 *            �¼�
	 * @return �Ƿ�ɹ����ӣ�false��ʾ�����ظ���δ��ִ������
	 */
	public boolean add(C2D_Event_Update event)
	{
		return add(event);
	}

	/**
	 * �����¼����������ظ�
	 * 
	 * @param event
	 *            �¼�
	 * @return �Ƿ�ɹ����ӣ�false��ʾ�����ظ���δ��ִ������
	 */
	protected boolean add(C2D_Event_SysLoop event)
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
		C2D_EventMsg_SysLoop message = new C2D_EventMsg_SysLoop(m_carrier, event);
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
		eventList.removeAllElements();
	}

	public void onRelease()
	{
		m_carrier = null;
		if (eventList != null)
		{
			eventList.clear();
			eventList = null;
		}
	}

	class C2D_EventMsg_SysLoop extends C2D_Object
	{
		C2D_Stage m_carrier;
		C2D_Event_SysLoop m_event;

		/**
		 * �����¼���Ϣ������������ڵ�������¼�
		 * 
		 * @param carrier
		 *            �¼����壬����Ϊ��
		 * @param event
		 *            �¼����󣬲�����Ϊ��
		 * @return �¼���Ϣ
		 */
		C2D_EventMsg_SysLoop(C2D_Stage carrier, C2D_Event_SysLoop event)
		{
			m_carrier = carrier;
			m_event = event;
		}

		public void onRelease()
		{
			m_carrier = null;
			m_event = null;
		}
	}
	abstract class C2D_Event_SysLoop
	{
		C2D_Stage m_carrier;

		/**
		 * �����¼���Ϣ������������ڵ�������¼�
		 * 
		 * @param carrier
		 *            �¼����壬����Ϊ��
		 * @return �¼���Ϣ
		 */
		public C2D_Event_SysLoop(C2D_Stage carrier)
		{
			m_carrier = carrier;
		}

		public abstract boolean doEvent(C2D_Stage m_carrier2);

		public boolean doRelease()
		{
			m_carrier = null;
			return true;
		}
	}
}
