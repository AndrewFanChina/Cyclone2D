package c2d.mod.ai;

import java.util.Enumeration;
import java.util.Hashtable;

import c2d.frame.com.data.unit_type.C2D_UnitValue;
import c2d.lang.math.C2D_Order;
import c2d.lang.math.C2D_SortableArray;
import c2d.lang.math.C2D_Store;
/**
 * 消息管理器
 * @author AndrewFan
 *
 */
public class C2D_MsgManager
{
	/** 消息广播开关状态 */
	boolean m_broadcasting = true;
	/** 消息创建池 */
	C2D_Store m_msgCreator = new C2D_Store();
	/** 用于消息广播的消息分选器哈希表 */
	Hashtable m_castTable = new Hashtable();
	/** 用于消息处理的消息分选器有序表 */
	private C2D_SortableArray m_processList = new C2D_SortableArray();
	/** 消息分选器是否发生过变动 */
	private boolean m_changed = false;

	/**
	 * 广播消息
	 * 
	 * @param msgType
	 *            消息类型ID
	 * @param msgParam
	 *            消息参数
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
	 * 添加分选处理器，如果之前有过相同类型ID的分选处理器， 则会覆盖掉原有的分选处理器。
	 * 
	 * @param filter
	 *            分选处理器
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
	 * 处理消息队列
	 */
	public void processMsgQue()
	{
		//关闭广播
		m_broadcasting = false;
		//查看分选器是否发生过变动
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
			//从大到小
			int len=m_processList.size();
			for (int i = 0; i < len; i++)
			{
				C2D_Order temp=m_processList.m_datas[i];
				m_processList.m_datas[i]=m_processList.m_datas[len-1-i];
				m_processList.m_datas[len-1-i]=temp;
			}
			m_changed = false;
		}
		//分选处理消息
		long time = System.currentTimeMillis();
		int len = m_processList.size();
		for (int i = 0; i < len; i++)
		{
			C2D_MsgFilter filter = (C2D_MsgFilter) m_processList.elementAt(i);
			filter.processMsgQue(time);
		}
		//重新准备
		for (int i = 0; i < len; i++)
		{
			C2D_MsgFilter filter = (C2D_MsgFilter) m_processList.elementAt(i);
			filter.getReady();
		}
		//开启广播
		m_broadcasting = true;
	}
}
