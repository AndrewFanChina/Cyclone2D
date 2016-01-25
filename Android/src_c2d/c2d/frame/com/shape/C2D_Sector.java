package c2d.frame.com.shape;

import c2d.lang.math.type.C2D_Color;
import c2d.plat.gfx.C2D_Graphics;

public class C2D_Sector extends C2D_Rectangle
{
	/** 扇形开口角度 */
	private int m_angleBegin;
	/** 扇形过渡角度 */
	private int m_angleSweep;
	/**
	 * 获取扇形开口角度
	 *
	 * @return 扇形开口角度
	 */
	public int getAngleBegin()
	{
		return m_angleBegin;
	}
	
	/**
	 * 设置扇形开口角度
	 * @param angle 扇形开口角度
	 * @return 返回是否发生改变
	 */
	public boolean setAngleBegin(int angle)
	{
		if(m_angleBegin==angle)
		{
			return false;
		}
		m_angleBegin = angle;
		return true;
	}
	/**
	 * 获取扇形闭口角度
	 *
	 * @return 扇形闭口角度
	 */
	public int getAngleEnd()
	{
		return m_angleSweep;
	}
	/**
	 * 设置扇形过渡角度
	 * @param angle 扇形过渡角度
	 * @return 返回是否发生改变
	 */
	public boolean setAngleSweep(int angle)
	{
		if(m_angleSweep==angle)
		{
			return false;
		}
		m_angleSweep = angle;
		return true;
	}
	/**
	 * 设置扇形控件的所有参数
	 * @param width       扇形宽度
	 * @param height      扇形高度
	 * @param bgColor	    扇形背景颜色
	 * @param borderColor 扇形边框颜色
	 * @param anchor	    锚点坐标
	 * @param angleBegin  扇形开口角度
	 * @param angleSweep  扇形过渡角度
	 */
	public void setParameters(int width,int height,int bgColor, int borderColor,int anchor,
			int angleBegin,int angleSweep)
	{
		m_width=width;
		m_height=height;
		m_bgColor=C2D_Color.makeARGB(bgColor);
		m_borderColor=C2D_Color.makeARGB(borderColor);
		m_anchor=anchor;
		m_angleBegin=angleBegin;
		m_angleSweep=angleSweep;
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
//@			g.fillArc(m_xToTop, m_yToTop, m_width, m_height,m_angleBegin, m_angleSweep,m_bgColor.getColor(),m_anchor,m_regionShow);
//@		}	
//@		if(m_borderColor!=null)
//@		{
//@			g.drawArc(m_xToTop, m_yToTop, m_width, m_height,m_angleBegin, m_angleSweep,m_borderColor.getColor(),m_anchor,m_regionShow);
//@		}
//@		paintFocus(g,m_width,m_height);
		//#endif
	}
}
