package c2d.frame.event;

import c2d.frame.base.C2D_Widget;

public abstract class C2D_Event_KeyPress extends C2D_Event
{
	/**
	 * 事件执行函数
	 * @param carrier 事件载体，这里是一个基本节点
	 * @param keyCode 有效的按键
	 * @return 该事件是否完结，即不再需要响应
	 */
	protected abstract boolean doEvent(C2D_Widget carrier,int keyCode);

}
