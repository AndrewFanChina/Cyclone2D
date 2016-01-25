package c2d.frame.event;

import c2d.frame.com.list.scroll.C2D_ScrollView;

public abstract class C2D_Event_ClickItem extends C2D_Event
{
	/**
	 * 事件执行函数
	 * @param scrollView 事件载体，这里是一个滚动视图
	 * @param itemID 代表被点击的条目ID
	 * @return 该事件是否完结，即不再需要响应
	 */
	protected abstract boolean doEvent(C2D_ScrollView scrollView,int itemID);

}
