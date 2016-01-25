package c2d.frame.event;

import c2d.frame.base.C2D_Widget;

public abstract class C2D_Event_MotionEnd extends C2D_Event
{
	/**
	 * 事件执行函数
	 * @param carrier 事件载体，这里是一个基本节点
	 * @return 该事件是否完结，即不再需要响应
	 */
	protected abstract boolean doEvent(C2D_Widget carrier);

}
