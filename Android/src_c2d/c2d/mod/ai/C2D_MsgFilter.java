package c2d.mod.ai;

import c2d.frame.com.data.unit_type.C2D_UnitValue;
import c2d.lang.math.C2D_Array;
import c2d.lang.math.C2D_Order;

/**
 * ��Ϣ��ѡ�� ���ڽ���ͬ���͵���Ϣ����洢�������ִ��
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_MsgFilter implements C2D_Order
{
	/** �������� */
	private C2D_MsgQueue m_QueWork = new C2D_MsgQueue();
	/** ���ж��� */
	private C2D_Array m_QueIdle = new C2D_Array();
	/** ��Ϣ������ */
	private C2D_MsgManager m_manager;
	/** ��Ϣ���ͱ�� */
	int m_msgType;
	/** ������Ϣ�������Ŀ */
	int m_maxNumber;
	/** ������Ϣ�����ͣ��ʱ��(����) */
	int m_timeRemain;
	/** ��Ϣ��ѡ�������ȼ� */
	int m_priority;
	/** �Ƿ�����ȼ����� */
	private boolean m_sortByPriority;
	/**
	 * ������Ϣ��ѡ��
	 * 
	 * @param msgType
	 *            ��Ϣ���ͱ��
	 * @param maxNumber
	 *            ������Ϣ�������Ŀ
	 * @param timeRemain
	 *            ������Ϣ�����ͣ��ʱ��(��λ-��)
	 * @param priority
	 *            ������Ϣ�����ȼ�
	 */
	public C2D_MsgFilter(int msgType, int maxNumber, int timeRemain,int priority)
	{
		m_msgType = msgType;
		m_maxNumber = maxNumber;
		m_timeRemain = timeRemain*1000;
		m_priority = priority;
	}
	/**
	 * ���ù�����
	 * @param manager ������
	 */
	void setManager(C2D_MsgManager manager)
	{
		m_manager = manager;
	}
	/**
	 * �����ڴ���ʱ���Ƿ��������ȼ�
	 * @param sortByPriority
	 */
	public void setSortable(boolean sortByPriority)
	{
		m_sortByPriority=sortByPriority;
	}
	/**
	 * �㲥��Ϣ
	 * 
	 * @param msgID
	 *            ��ϢID
	 * @param msgParam
	 *            ��Ϣ����
	 * @param msgPriority
	 *            ��Ϣ���ȼ�
	 */
	public void broadCast(int msgID, C2D_UnitValue msgParam,int msgPriority)
	{
		if (m_manager == null)
		{
			return;
		}
		//��װ����������Ϣ
		C2D_Msg msg = (C2D_Msg) m_manager.m_msgCreator.recoverElement();
		if (msg == null)
		{
			msg = new C2D_Msg(msgID, msgParam,msgPriority);
			m_manager.m_msgCreator.addElement(msg);
		}
		else
		{
			msg.setValue(msgID, msgParam,msgPriority);
		}
		//����Ϣ�������
		if (m_manager.m_broadcasting)
		{
			m_QueWork.receiveMsg(msg);
		}
		else
		{
			m_QueIdle.addElement(msg);
		}
	}
	/**
	 * �㲥��Ϣ��ʹ�����ȼ�����0
	 * 
	 * @param msgID
	 *            ��ϢID
	 * @param msgParam
	 *            ��Ϣ����
	 */
	public void broadCast(int msgID, C2D_UnitValue msgParam)
	{
		broadCast(msgID, msgParam,0);
	}
	/**
	 * ������Ϣ����
	 */
	public void processMsgQue(long time)
	{
		//�Ƚ���������������ȼ����еĻ�
		if(m_sortByPriority)
		{
			m_QueWork.sort();
		}
		// �����޳���������Ϣ
		int size = m_QueWork.size();
		if(size<=0)
		{
			return;
		}
		int delCount = size - m_maxNumber;
		for (int i = 0; i < delCount; i++)
		{
			C2D_Msg msg = m_QueWork.remove();
			m_manager.m_msgCreator.store(msg);
		}
		// �����޳����ڵ���Ϣ
		size = m_QueWork.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Msg msg = m_QueWork.elementAt(i);
			if(msg==null)
			{
				break;
			}
			if(time - msg.m_timeOccured>m_timeRemain)
			{
				m_QueWork.removeAt(i);
				m_manager.m_msgCreator.store(msg);
				i--;
			}
		}
		//������Ϣ�Ĵ���
		processMsg(m_QueWork);
		//����ʧЧ����Ϣ
		size = m_QueWork.size();
		for (int i = 0; i < size; i++)
		{
			C2D_Msg msg = m_QueWork.elementAt(i);
			if(msg==null)
			{
				break;
			}
			if(msg.m_valid)
			{
				m_QueWork.removeAt(i);
				m_manager.m_msgCreator.store(msg);
				i--;
			}
		}
	}
	/** ������Ϣ�ķ��� */
	protected abstract void processMsg(C2D_MsgQueue msgQue);
	/**
	 * ����׼������Ҫ�����ǽ�������е���Ϣת�빤�����С�
	 */
	public void getReady()
	{
		int len = m_QueIdle.size();
		for (int i = 0; i < len; i++)
		{
			C2D_Msg msg = (C2D_Msg) m_QueIdle.elementAt(i);
			m_QueWork.receiveMsg(msg);
		}
		m_QueIdle.clear();
	}
	

	public int getOrderValue()
	{
		return m_priority;
	}
}
