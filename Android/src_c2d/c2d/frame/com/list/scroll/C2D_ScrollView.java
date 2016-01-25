package c2d.frame.com.list.scroll;

import c2d.frame.base.C2D_Scene;
import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_View;
import c2d.frame.base.C2D_Widget;
import c2d.frame.event.C2D_EventPool_ClickItem;
import c2d.frame.event.C2D_EventPool_Scroll;
import c2d.lang.app.C2D_Device;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_TouchData;
import c2d.lang.math.type.C2D_PointF;

public abstract class C2D_ScrollView extends C2D_View  implements C2D_Scrollable
{
	/** 当前光标所在的行ID */
	protected int m_currentScroll;
	/** 当前光标所能到达的的最小行ID */
	protected int m_minScroll=0;
	/** 当前光标所能到达的的最大行ID */
	protected int m_maxScroll=65536;
	/** 滚动方式，默认采取条目滚动**/
	protected int m_scrollType=C2D_Scrollable.Scrool_Loop;
	/** 页面第一行所在的行ID*/
	protected int m_firstRow;
	/** 是否需要重新组装UI */
	protected boolean m_needRepack=true;
	/** 事件池-滚动*/
	protected C2D_EventPool_Scroll m_Events_Sroll;
	/** 按钮事件池*/
	protected C2D_EventPool_ClickItem m_Events_ClickItem;
	/**
	 * 根据视图类型构造视图
	 * @param partRedraw 是否采用局部绘图
	 */
	public C2D_ScrollView()
	{
	}
	/**
	 * 向前滚动一行，如果列表采用页面滚动，超出首尾时会进行光标切换
	 * @return 是否执需要清除触发滚动的按键
	 */
	public boolean scrollFront()
	{
		int total=getTotalScroll();
		int pageRow=getPageRow();
		if(total<=0)
		{
			return false;
		}
		int orgScroll = m_currentScroll;
		int nextScroll = m_currentScroll+1;
		if(nextScroll>m_maxScroll)
		{
			return true;
		}
		switch(m_scrollType)
		{
		case C2D_Scrollable.Scrool_Page:
			if(nextScroll>=m_firstRow+pageRow||nextScroll>=total)
			{
				C2D_Scene scene= getSceneAt();
				C2D_Stage stage=getStageAt();
				if(scene!=null&&stage!=null)
				{
					if(scene.moveFocus(this, stage.getSingleKey()))
					{
						stage.releaseKeys();
					}
				}
			}
			else
			{
				m_currentScroll = nextScroll;
			}
			refreshItemChange();
			break;
		case C2D_Scrollable.Scrool_Item:
			if(nextScroll>=total)
			{
				return false;
			}
			m_currentScroll = nextScroll;
			if(m_currentScroll>=m_firstRow+pageRow)
			{
				m_firstRow=C2D_Math.min(m_currentScroll, total-pageRow);
				m_needRepack=true;
			}
			else
			{
				refreshItemChange();
			}
			break;
		case C2D_Scrollable.Scrool_Loop:
			if(nextScroll>=m_firstRow+pageRow||nextScroll>=total)
			{
				m_needRepack=true;
				if(nextScroll>=total)
				{
					m_currentScroll=0;
					m_firstRow=0;
				}
				else
				{
					m_currentScroll=nextScroll;
					m_firstRow=m_currentScroll-pageRow+1;
				}
			}
			else
			{
				m_currentScroll=nextScroll;
				refreshItemChange();
			}
			break;
		}
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
			return true;
		}
		return false;
	}
	/**
	 * 向回滚动一行，如果列表采用页面滚动，超出首尾时会进行光标切换
	 * @return 是否执需要清除触发滚动的按键
	 */
	public boolean scrollBack()
	{
		int total=getTotalScroll();
		int pageRow=getPageRow();
		if(total<=0)
		{
			return false;
		}
		int orgScroll = m_currentScroll;
		int nextScroll = m_currentScroll-1;
		if(nextScroll<m_minScroll)
		{
			return true;
		}
		switch(m_scrollType)
		{
		case C2D_Scrollable.Scrool_Page:
			if(nextScroll<m_firstRow)
			{
				C2D_Scene scene= getSceneAt();
				C2D_Stage stage=getStageAt();
				if(scene!=null&&stage!=null)
				{
					if(scene.moveFocus(this, stage.getSingleKey()))
					{
						stage.releaseKeys();
					}
				}
			}
			else
			{
				m_currentScroll = nextScroll;
			}
			refreshItemChange();
			break;
		case C2D_Scrollable.Scrool_Item:
			if(nextScroll<0)
			{
				return false;
			}
			m_currentScroll = nextScroll;
			if(m_currentScroll<m_firstRow)
			{
				m_firstRow=m_currentScroll;
				m_needRepack=true;
			}
			else
			{
				refreshItemChange();
			}
			break;
		case C2D_Scrollable.Scrool_Loop:
			if(nextScroll<m_firstRow)
			{
				m_needRepack=true;
				if(nextScroll<0)
				{
					m_currentScroll=total-1;
					m_firstRow=m_currentScroll-(pageRow-1);
					if(m_firstRow<0)
					{
						m_firstRow=0;
					}
				}
				else
				{
					m_currentScroll=nextScroll;
					m_firstRow=nextScroll;
				}
			}
			else
			{
				m_currentScroll=nextScroll;
				refreshItemChange();
			}
			break;
		}
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
			return true;
		}
		return false;
	}
	/**
	 * 向前滚动一页
	 * @return 是否执行了滚动
	 */
	public boolean scrollFrontByPage()
	{
		int total=getTotalScroll();
		int pageRow=getPageRow();
		if(total<=0)
		{
			return false;
		}
		int orgScroll = m_currentScroll;
		int nextScroll = m_firstRow+pageRow;
		switch(m_scrollType)
		{
		case C2D_Scrollable.Scrool_Page:
		case C2D_Scrollable.Scrool_Item:
			if(nextScroll<total)
			{
				m_currentScroll = nextScroll;
				m_firstRow = nextScroll;
				m_needRepack=true;
			}
			break;
		case C2D_Scrollable.Scrool_Loop:
			if(nextScroll<total)
			{
				m_currentScroll = nextScroll;
				m_firstRow = nextScroll;
			}
			else
			{
				m_currentScroll = 0;
				m_firstRow = 0;
			}
			m_needRepack=true;
			break;
		}
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
			return true;
		}
		return false;
	}
	/**
	 * 向回滚动一页
	 * @return 是否执行了滚动
	 */
	public boolean scrollBackByPage()
	{
		int total=getTotalScroll();
		int pageRow=getPageRow();
		if(total<=0)
		{
			return false;
		}
		int orgScroll = m_currentScroll;
		int nextScroll = m_firstRow-pageRow;
		switch(m_scrollType)
		{
		case C2D_Scrollable.Scrool_Page:
		case C2D_Scrollable.Scrool_Item:
			if(nextScroll>=0)
			{
				m_currentScroll = nextScroll;
				m_firstRow = nextScroll;
				m_needRepack=true;
			}
			break;
		case C2D_Scrollable.Scrool_Loop:
			if(nextScroll>=0)
			{
				m_currentScroll = nextScroll;
				m_firstRow = nextScroll;
			}
			else
			{
				m_firstRow= ((total-1)/pageRow)*pageRow;
				m_currentScroll = m_firstRow;
			}
			m_needRepack=true;
			break;
		}
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
			return true;
		}
		return false;
	}
	
	/**
	 * 获得当前光标位置，即被选中行ID
	 * @return 当前光标位置
	 */
	public int getCurrentScroll()
	{
		return m_currentScroll;
	}
	/**
	 * 获得当前光标位置，即被选中行的中心所对应的Y坐标
	 * @return Y坐标，相对世界坐标
	 */
	public float getCurrentScrollTopY()
	{
		int itemID=m_currentScroll-m_firstRow;
		if(itemID>=0)
		{
			C2D_Widget child = getChildAt(itemID);
			if(child!=null)
			{
				return child.getYToTop();
			}
		}
		return 0;
	}
	/**
	 * 获得指定topY坐标对应的条目ID，如果不存在，则返回-1
	 * @return 条目ID
	 */
	public int getItemIDAtY(float Y)
	{
		int ih = getItemHeight()+getItemGap();
		if(ih<=0)
		{
			return -1;
		}
		C2D_PointF lt =getLeftTop();
		if(lt==null)
		{
			return -1;
		}
		int idT=((int)(Y-lt.m_y))/ih;
		int pageReach=C2D_Math.min(getTotalRow()-1, m_firstRow+getPageRow()-1);
		if(m_firstRow+idT<=pageReach)
		{
			return m_firstRow+idT;
		}
		return -1;
	}
	/**
	 * 重置滚动位置
	 */
	public void resetScroll()
	{
		int orgScroll = m_currentScroll;
		m_currentScroll=0;
		m_firstRow=0;
		m_needRepack=true;
		if(orgScroll!=m_currentScroll)
		{
			callScrollEvent(orgScroll,false);
			callScrollEvent(m_currentScroll,true);
		}
	}
	/**
	 * 设置到当前选中的行
	 * @param value 当前选中的行
	 */
	public void setCurrentScroll(int value)
	{
		if(value>=0&&value<getTotalRow())
		{
			int orgScroll = m_currentScroll;
			m_currentScroll = value;
			rebuildRowInf();
			m_needRepack=true;
			if(orgScroll!=m_currentScroll)
			{
				callScrollEvent(orgScroll,false);
				callScrollEvent(m_currentScroll,true);
			}
		}
	}
	/**
	 * 获得全部可以滚动的行数，列表可以全条目滚动
	 * @return 全部全部行数
	 */
	public int getTotalScroll()
	{
		return getTotalRow();
	}
	/**
	 * 触发滚动事件，由系统调用，你不该使用这个方法
	 * @param itemIndex 发生滚动事件的条目ID
	 * @param scrollIn true代表滚入，即代表获得高亮，false代表失去滚出
	 */
	protected void callScrollEvent(int itemIndex,boolean scrollIn)
	{
		if(m_Events_Sroll!=null)
		{
			m_Events_Sroll.onCalled(itemIndex,scrollIn);
		}
	}
	/**
	 * 重新建立行信息
	 */
	protected void rebuildRowInf()
	{
		int totalRow=getTotalRow();
		//总行数对页面显示行数的约束
		int m_pageRow=getPageRow();
		if(m_pageRow>totalRow)
		{
			m_pageRow = totalRow;
		}
		//总行数对当前页面首行的约束
		if(totalRow<=m_pageRow)
		{
			m_firstRow=0;
		}
		else
		{
			m_firstRow=C2D_Math.limitNumber(
					m_firstRow, 0, totalRow-m_pageRow);
		}
		//总行数对当前光标行的约束
		if(totalRow>0)
		{
			m_currentScroll = C2D_Math.limitNumber(m_currentScroll,
				0, totalRow-1);
		}
		else
		{
			m_currentScroll=-1;
		}
		//页面首行跟随光标行的迁移
		if(totalRow>0)
		{
			if(m_currentScroll<m_firstRow)
			{
				m_firstRow=m_currentScroll;
			}
			if(m_currentScroll>=m_firstRow+m_pageRow)
			{
				m_firstRow=m_currentScroll;
				m_firstRow=C2D_Math.limitNumber(
						m_firstRow, 0, totalRow-m_pageRow);
			}
		}
	}
	/**
	 * 获得滚动事件池
	 * @return 更新事件池
	 */
	public C2D_EventPool_Scroll Events_Scroll()
	{
		if(m_Events_Sroll==null)
		{
			m_Events_Sroll=new C2D_EventPool_Scroll(this);
		}
		return m_Events_Sroll;
	}
	/**
	 * 获得条目点击事件池
	 * @return 事件池
	 */
	public C2D_EventPool_ClickItem Events_ClickItem()
	{
		if(m_Events_ClickItem==null)
		{
			m_Events_ClickItem=new C2D_EventPool_ClickItem(this);
		}
		return m_Events_ClickItem;
	}
	/**
	 * 滚动时刷新变化的条目
	 */
	protected abstract void refreshItemChange();
	/**
	 * 设置滚动类型
	 * @param scrollType 滚动类型
	 */
	public void setScrollType(int scrollType)
	{
		m_scrollType = scrollType;
	}
	/**
	 * 获得滚动类型
	 * @return 滚动类型
	 */
	public int getScrollType()
	{
		return m_currentScroll;
	}
	/**
	 * 响应在控件上操作上右下左按键，实现焦点的移动.
	 * 此方法将被系统调用，不应该在外部手动调用。
	 * @param stage 所在场景
	 */
	protected void processNavigations(C2D_Stage stage)
	{
		if(!m_focused)
		{
			return;
		}
		C2D_Scene scene = getSceneAt();
		if(scene==null)
		{
			return;
		}
		int keyCode=stage.getSingleKey();
		//滚动事件响应
		if(keyCode>=C2D_Device.key_up&&keyCode<=C2D_Device.key_left)
		{
			switch(keyCode)
			{
				case C2D_Device.key_down:
					if(scrollFront())
					{
						stage.releaseKeys();
					}
					break;
				case C2D_Device.key_up:
					if(scrollBack())
					{
					stage.releaseKeys();
					}
					break;
			}
		}
		//处理左右方向的焦点事件
		super.processNavigations(stage);
	}
	/**
	 * 处理组件的按钮响应。 此方法将被系统调用，不应该在外部手动调用。
	 * 
	 * @param stage
	 *            所在场景
	 */
	protected void processBtnCalls(C2D_Stage stage)
	{
		if (!m_visible)
		{
			return;
		}
		int keyCode=stage.getSingleKey();
		//点击事件响应
		if(m_currentScroll>=0&&m_currentScroll<getTotalRow())
		{
			if (keyCode >= C2D_Device.key_up && keyCode < C2D_Device.key_other)
			{
				if (m_Events_KeyPress != null)
				{
					m_Events_KeyPress.onCalled(keyCode);
				}
			}
			//按键引发条目点击
			if (m_focused&&keyCode == C2D_Device.key_enter && !anyKeyUsed(keyCode))
			{
				if(m_Events_ClickItem!=null)
				{
					if(m_Events_ClickItem.onCalled(m_currentScroll))
					{
						stage.releaseKeys();
					}
				}
			}
			//触屏引发条目点击
			else if (stage != null&&m_focusable)
			{
				// 进行热区计算
				C2D_TouchData td = stage.m_curTouchData;
				if(td!=null&&td.m_touchCount>0)
				{
					if(Plat_Tochable && m_focusable)
					{
						C2D_PointF pt= getTouchPoint();
						if(pt!=null)
						{
							int id= getItemIDAtY(pt.m_y);
							if(id>=0 && m_Events_ClickItem!=null)
							{
								if(m_Events_ClickItem.onCalled(id))
								{
									stage.releaseTouchPoints();
								}
							}	
						}
					}
				}
	
			}

		}
	}
	public void setLimitScroll(int min,int max)
	{
		m_minScroll=C2D_Math.max(0, min);
		m_maxScroll=C2D_Math.min(getTotalRow()-1, max);
	}
	/**
	 * 获得全部行数
	 * @return 全部行数
	 */
	public abstract int getTotalRow();
	/**
	 * 获得页面显示行数
	 * @return 页面显示行数
	 */
	public abstract int getPageRow();
	/**
	 * 获取条目高度
	 * 
	 * @return 条目高度
	 */
	public abstract int getItemHeight();
	/**
	 * 获取条目间的间隔
	 * 
	 * @return 条目间的间隔
	 */
	public abstract int getItemGap();
	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		if(m_Events_Sroll!=null)
		{
			m_Events_Sroll.doRelease();
			m_Events_Sroll=null;
		}
		if(m_Events_ClickItem!=null)
		{
			m_Events_ClickItem.doRelease();
			m_Events_ClickItem=null;
		}
	}
}
