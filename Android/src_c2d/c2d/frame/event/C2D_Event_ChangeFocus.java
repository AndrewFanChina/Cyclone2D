package c2d.frame.event;

import c2d.frame.base.C2D_Widget;

public abstract class C2D_Event_ChangeFocus extends C2D_Event
{
	/**
	 * 
	 * 事件执行函数
	 * @param carrier 事件主体，这里是一个基本节点
	 * @param focused 获得或者失去焦点
	 * @param another 另外一个获得或者失去焦点的组件，
	 * 如果主体获得焦点，则another是原焦点，如果主体失去焦点,则another是目标焦点
	 * @return 该事件是否完结，即不再需要响应
	 */
	protected abstract boolean doEvent(C2D_Widget carrier,boolean focused,C2D_Widget another);
}
