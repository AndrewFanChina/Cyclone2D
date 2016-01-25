package c2d.frame.com.shape;

import c2d.lang.math.type.C2D_Color;
import c2d.plat.gfx.C2D_Graphics;

public class C2D_RoundRect extends C2D_Rectangle
{
	/** 圆角宽度 */
	private int m_ArchWidth;
	/** 圆角高度 */
	private int m_ArchHeight;
	/**
	 * 获取宽度
	 *
	 * @return 宽度
	 */
	public int getArchWidth()
	{
		return m_ArchWidth;
	}
	
	/**
	 * 设置圆角宽度
	 * @param width 圆角新宽度
	 * @return 返回是否发生改变
	 */
	public boolean setArchWidth(int width)
	{
		if(m_ArchWidth==width)
		{
			return false;
		}
		m_ArchWidth = width;
		return true;
	}
	/**
	 * 获取圆角高度
	 *
	 * @return 高度
	 */
	public int getArchHeight()
	{
		return m_ArchHeight;
	}
	
	/**
	 * 设置圆角宽度
	 * @param height 新高度
	 * @return 返回是否发生改变
	 */
	public boolean setHeight(int height)
	{
		if(m_ArchHeight==height)
		{
			return false;
		}
		m_ArchHeight = height;
		layoutChanged();
		return true;
	}
	/**
	 * 设置圆角矩形控件的所有参数
	 * @param width       圆角矩形宽度
	 * @param height      圆角矩形高度
	 * @param bgColor	    圆角矩形背景颜色
	 * @param borderColor 圆角矩形边框颜色
	 * @param anchor	    锚点坐标
	 * @param archW		    圆角宽度
	 * @param archH		    圆角高度
	 */
	public void setParameters(int width,int height,int bgColor, int borderColor,int anchor,
			int archW,int archH)
	{
		m_width=width;
		m_height=height;
		m_bgColor=C2D_Color.makeARGB(bgColor);
		m_borderColor=C2D_Color.makeARGB(borderColor);
		m_anchor=anchor;
		m_ArchWidth=archW;
		m_ArchHeight=archH;
		layoutChanged();
	}
	/**
	 * 绘制节点
	 * @param g 画笔
	 */
	public void onPaint(C2D_Graphics g)
	{
		if (g == null || !m_visible ||m_hiddenByParent || !m_inCamera)
		{
			return;
		}
		//#if Platform=="J2me"
//@		if(m_bgColor!=null)
//@		{
//@			g.fillRoundRect(m_xToTop, m_yToTop, m_width, m_height,m_ArchWidth, m_ArchHeight,m_bgColor.getColor(),m_anchor,m_regionShow);
//@		}	
//@		if(m_borderColor!=null)
//@		{
//@			g.drawRoundRect(m_xToTop, m_yToTop, m_width, m_height,m_ArchWidth, m_ArchHeight,m_borderColor.getColor(),m_anchor,m_regionShow);
//@		}
//@		paintFocus(g,m_width,m_height);
		//#endif
	}
}
