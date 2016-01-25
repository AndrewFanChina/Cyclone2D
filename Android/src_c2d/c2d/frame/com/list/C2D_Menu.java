package c2d.frame.com.list;

import c2d.frame.base.C2D_Widget;
import c2d.frame.com.list.scroll.C2D_ScrollView;

/**
 * 菜单类 菜单是一个抽象的滚动视图，你所添加的子单元将成为菜单项。 框架已经处理了菜单项之间进行的滚动事件，你需要添加一个 配置函数来响应菜单项的变化。
 * 
 * @author AndrewFan
 * 
 */
public abstract class C2D_Menu extends C2D_ScrollView
{
	public C2D_Menu()
	{
		m_focusable = true;
	}

	protected void refreshItemChange()
	{
		int pageRow = getPageRow();
		for (int i = 0; i < pageRow; i++)
		{
			int itemID = m_firstRow + i;
			C2D_Widget item = getChildAt(i);
			if (item == null)
			{
				continue;
			}
			configItem(item, i, itemID == m_currentScroll);
		}
	}

	/**
	 * 配置条目，当菜单项发生变化时，会调用此函数， 你需要在其内部实现条目所发生的变化。
	 * 
	 * @param item
	 *            条目组件
	 * @param itemIndex
	 *            条目组件对应的ID
	 * @param selected
	 *            条目是否被选中
	 */
	public abstract void configItem(C2D_Widget item, int itemIndex, boolean selected);

	/**
	 * 让当前控件拥有焦点，这个函数将自动将控件设置成可以拥有焦点。 将当前场景的焦点控件变成自身。你不应该手动调用这个函数。
	 * @param another 原焦点
	 */
	protected void gotFocus(C2D_Widget another)
	{
		super.gotFocus(another);
		refreshItemChange();
		// 重新触发一次滚动事件
		callScrollEvent(m_currentScroll, true);
	}

	/**
	 * 失去焦点，这个函数将自动将控件设置成可以拥有焦点。 你不应该手动调用这个函数，那样的话，当前页面将失去拥有焦点的控件
	 * @param another 目标焦点
	 */
	protected void lostFocus(C2D_Widget another)
	{
		super.lostFocus(another);
		refreshItemChange();
	}

	public int getTotalRow()
	{
		return getChildCount();
	}

	public int getPageRow()
	{
		return getChildCount();
	}

}
