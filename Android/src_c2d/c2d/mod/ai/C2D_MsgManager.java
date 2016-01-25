package c2d.mod.ai;

import java.util.Enumeration;
import java.util.Hashtable;

import c2d.frame.com.data.unit_type.C2D_UnitValue;
import c2d.lang.math.C2D_Order;
import c2d.lang.math.C2D_SortableArray;
import c2d.lang.math.C2D_Store;
/**
 * ��Ϣ������
 * @author AndrewFan
 *
 */
public class C2D_MsgManager
{
	/** ��Ϣ�㲥����״̬ */
	boolean m_broadcasting = true;
	/** ��Ϣ������ */
	C2D_Store m_msgCreator = new C2D_Store();
	/** ������Ϣ�㲥����Ϣ��ѡ����ϣ�� */
	Hashtable m_castTable = new Hashtable();
	/** ������Ϣ�������Ϣ��ѡ������� */
	private C2D_SortableArray m_processList = new C2D_SortableArray();
	/** ��Ϣ��ѡ���Ƿ������䶯 */
	private boolean m_changed = false;

	/**
	 * �㲥��Ϣ
	 * 
	 * @param msgType
	 *            ��Ϣ����ID
	 * @param msgParam
	 *            ��Ϣ����
	 */
	public void broadCast(int msgType, C2D_UnitValue msgParam)
	{
		Object o = m_castTable.get(new Integer(msgType));
		if (o == null)
		{
			return;
		}
		C2D_MsgFilter filter = (C2D_MsgFilter) o;
		filter.broadCast(msgType, msgParam);
	}

	/**
	 * ��ӷ�ѡ�����������֮ǰ�й���ͬ����ID�ķ�ѡ�������� ��Ḳ�ǵ�ԭ�еķ�ѡ��������
	 * 
	 * @param filter
	 *            ��ѡ������
	 */
	public void addFilter(C2D_MsgFilter filter)
	{
		if (filter == null)
		{
			return;
		}
		int type = filter.m_msgType;
		Integer key = new Integer(type);
		Object o = m_castTable.get(key);
		if (o != null)
		{
			m_castTable.remove(key);
		}
		m_castTable.put(key, filter);
		filter.setManager(this);
		m_changed = true;
	}

	/**
	 * ������Ϣ����
	 */
	public void processMsgQue()
	{
		//�رչ㲥
		m_broadcasting = false;
		//�鿴��ѡ���Ƿ������䶯
		if (m_changed)
		{
			m_processList.clear();
			Enumeration er = m_castTable.elements();
			while (er.hasMoreElements())
			{
				C2D_MsgFilter filter = (C2D_MsgFilter) er.nextElement();
				m_processList.addElement(filter);
			}
			m_processList.quickSort3();
			//�Ӵ�С
			int len=m_processList.size();
			for (int i = 0; i < len; i++)
			{
				C2D_Order temp=m_processList.m_datas[i];
				m_processList.m_datas[i]=m_processList.m_datas[len-1-i];
				m_processList.m_datas[len-1-i]=temp;
			}
			m_changed = false;
		}
		//��ѡ������Ϣ
		long time = System.currentTimeMillis();
		int len = m_processList.size();
		for (int i = 0; i < len; i++)
		{
			C2D_MsgFilter filter = (C2D_MsgFilter) m_processList.elementAt(i);
			filter.processMsgQue(time);
		}
		//����׼��
		for (int i = 0; i < len; i++)
		{
			C2D_MsgFilter filter = (C2D_MsgFilter) m_processList.elementAt(i);
			filter.getReady();
		}
		//�����㲥
		m_broadcasting = true;
	}
}
