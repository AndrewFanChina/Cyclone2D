package c2d.frame.event;

import c2d.frame.base.C2D_Widget;

public abstract class C2D_Event_Scroll extends C2D_Event
{
	/**
	 * 事件执行函数
	 * @param carrier 事件载体，这里是一个基本节点
	 * @param itemIndex 发生滚动事件的条目ID
	 * @param scrollIn true代表滚入，即代表获得高亮，false代表失去滚出
	 * @return 是否执行完毕
	 */
	protected abstract boolean doEvent(C2D_Widget carrier,int itemIndex,boolean scrollIn);

}
