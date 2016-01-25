package c2d.mod.ai;

import c2d.frame.com.data.unit_type.C2D_UnitValue;
import c2d.lang.math.C2D_Order;
import c2d.lang.obj.C2D_Object;
/**
 * 消息类
 * 一条消息可以含有消息类型，消息参数，发生时间，优先级等
 * @author AndrewFan
 *
 */
public class C2D_Msg extends C2D_Object implements  C2D_Order
{
	/** 本消息的消息类型*/
	public int m_type;
	/** 本消息的附加参数*/
	public C2D_UnitValue m_param;
	/** 本消息的发生时间*/
	public long m_timeOccured = 0;
	/** 本消息的优先级，优先级高的消息将被优先执行 */
	public int m_priority;
	public boolean m_valid=false;
	/**
	 * 构建消息
	 * @param msgType  消息类型
	 * @param msgParam 附带参数
	 * @param msgPriority 消息优先级
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
	 * 设置消息属性
	 * @param msgType  消息类型
	 * @param msgParam 附带参数
	 * @param msgPriority 消息优先级
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
