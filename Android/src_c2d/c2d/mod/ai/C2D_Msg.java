package c2d.mod.ai;

import c2d.frame.com.data.unit_type.C2D_UnitValue;
import c2d.lang.math.C2D_Order;
import c2d.lang.obj.C2D_Object;
/**
 * ��Ϣ��
 * һ����Ϣ���Ժ�����Ϣ���ͣ���Ϣ����������ʱ�䣬���ȼ���
 * @author AndrewFan
 *
 */
public class C2D_Msg extends C2D_Object implements  C2D_Order
{
	/** ����Ϣ����Ϣ����*/
	public int m_type;
	/** ����Ϣ�ĸ��Ӳ���*/
	public C2D_UnitValue m_param;
	/** ����Ϣ�ķ���ʱ��*/
	public long m_timeOccured = 0;
	/** ����Ϣ�����ȼ������ȼ��ߵ���Ϣ��������ִ�� */
	public int m_priority;
	public boolean m_valid=false;
	/**
	 * ������Ϣ
	 * @param msgType  ��Ϣ����
	 * @param msgParam ��������
	 * @param msgPriority ��Ϣ���ȼ�
	 */
	public C2D_Msg(int msgType, C2D_UnitValue msgParam,int msgPriority)
	{
		setValue(msgType, msgParam,msgPriority);
	}

	public void onRelease()
	{
		m_param=null;
	}
	/**
	 * ������Ϣ����
	 * @param msgType  ��Ϣ����
	 * @param msgParam ��������
	 * @param msgPriority ��Ϣ���ȼ�
	 */
	public void setValue(int msgType, C2D_UnitValue msgParam,int msgPriority)
	{
		m_type = msgType;
		m_param = msgParam;
		m_timeOccured = System.currentTimeMillis();
		m_priority=msgPriority;
		m_valid=false;
	}

	public int getOrderValue(int orderType)
	{
		return m_priority;
	}

	public void setValid(boolean b)
	{
		m_valid=b;
	}
}
