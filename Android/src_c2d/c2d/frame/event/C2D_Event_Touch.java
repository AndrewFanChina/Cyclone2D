package c2d.frame.event;

import c2d.frame.base.C2D_Widget;
import c2d.lang.math.type.C2D_TouchData;
/**
 * 触屏事件，注意跟其它事件不同，触屏事件不应该被多个组件同时使用。
 * 否则，数据将会互相影响，造成错乱。因此机制中，也限制了当前监听的
 * 对象。可以使用setListenOn来切换监听，但是无法同时监听多个对象。
 * 而其它类型的事件是可以同时被多个组件所共享使用的。可以用来同时监听多
 * 个组件。因为其它类型事件内部基本不存在复杂的私有数据关系。
 * @author AndrewFan
 *
 */
public abstract class C2D_Event_Touch extends C2D_Event
{
	protected C2D_Widget m_carrier;
	protected C2D_Event_Touch(C2D_Widget carrier)
	{
		setListenOn(carrier);
	}
	public void setListenOn(C2D_Widget carrier)
	{
		m_carrier=carrier;
	}
	/**
	 * 事件执行函数
	 * @param carrier 事件载体，这里是一个基本节点
	 * @param tochData 触屏信息
	 * @return 该事件是否完结，即不再需要响应
	 */
	protected abstract boolean doEvent(C2D_TouchData tochData);

}
