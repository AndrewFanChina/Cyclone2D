package c2d.frame.com.list.scroll;

import c2d.frame.base.C2D_Widget;


public abstract class C2D_ScrollWidget extends C2D_Widget implements C2D_Scrollable
{
	/** 当前滚动在的行ID */
	protected int m_currentScroll=-1;
	/** 页面显示的行数，页面显示的行数*/
	protected int m_pageRow;	
	/** 全部可以滚动的行数 */
	protected int m_totalRow;
	/** 是否允许循环滚动 ，默认允许**/
	protected boolean m_loopScroll=true;
	/**
	 * 向前滚动
	 * @return 是否执行了滚动
	 */
	public boolean scrollFront()
	{
		int total=getTotalScroll();
		if(total<=0)
		{
			return false;
		}
		int nextScroll = m_currentScroll+1;
		if(nextScroll>=total&&!m_loopScroll)
		{
			return false;
		}
		m_currentScroll=(nextScroll)%total;
		layoutChanged();
		return true;
	}
	/**
	 * 向回滚动
	 * @return 是否执行了滚动
	 */
	public boolean scrollBack()
	{
		int total=getTotalScroll();
		if(total<=0)
		{
			return false;
		}
		int nextScroll = m_currentScroll-1;
		if(nextScroll<0&&!m_loopScroll)
		{
			return false;
		}
		m_currentScroll=(nextScroll+total)%total;
		layoutChanged();
		return true;
	}
	/**
	 * 获得当前滚动位置,设全部滚动距离L，滚动位置处于[0,L-1]区间
	 * @return 当前滚动位置
	 */
	public int getCurrentScroll()
	{
		return m_currentScroll;
	}
	/**
	 * 设置当前滚动位置
	 * @param scroll 滚动位置
	 */
	public void setCurrentScroll(int scroll)
	{
		if(scroll<0||scroll>getTotalScroll())
		{
			return;
		}
		m_currentScroll=scroll;
		layoutChanged();
	}

	/**
	 * 重置滚动位置
	 */
	public void resetScroll()
	{
		m_currentScroll=0;
		layoutChanged();
	}
	/**
	 * 获得全部行数
	 * @return 全部全部行数
	 */
	public int getTotalRow()
	{
		return m_totalRow;
	}
	/**
	 * 设置是否允许循环滚动 
	 * @param loopScroll 是否允许循环滚动
	 */
	public void setLoopScroll(boolean loopScroll)
	{
		m_loopScroll=loopScroll;
	}
	
}
