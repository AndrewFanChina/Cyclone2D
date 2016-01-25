package c2d.frame.com.shape;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.math.C2D_Math;
import c2d.lang.math.type.C2D_PointF;
import c2d.lang.math.type.C2D_RectF;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;

/**
 * 线段控件，当前位置为起始端点，需要指定显示的颜色和结束端点。
 * 结束端点坐标是相对于当前位置（起点）的相对坐标。对于线段来说，锚点没有作用
 * @author AndrewFan
 */
public class C2D_Segement extends C2D_Widget
{
	/** 结束端点X坐标 */
	protected float m_xEnd;
	/** 结束端点Y坐标*/
	protected float m_yEnd;
	/** 线段颜色 */
	protected int m_Color=0xFFFFFFFF;

	/**
	 * 构造函数
	 */
	public C2D_Segement()
	{
	}

	/**
	 * 获取颜色
	 * 
	 * @return 边框颜色
	 */
	public int getColor()
	{
		return m_Color;
	}

	/**
	 * 设置颜色,ARGB
	 * 
	 * @param color
	 *            ARGB颜色数值
	 */
	public void setColor(int color)
	{
		this.m_Color = color;
	}
	/**
	 * 获取控件占据区域的左上角的世界坐标，<br>
	 * 对于无区域概念的控件来说，是其绝对坐标，对于视图来说，是其左上角绝对坐标 。<br>
	 * 注意如果在其父类层级中含有缓冲视图(BufferedView)的话，<br>
	 * 这个坐标将变成相对于最接近自身的缓冲视图。
	 * 
	 * @return 左上角点的世界坐标
	 */
	protected C2D_PointF getLeftTop()
	{
		if (m_needUpdateLT)
		{
			m_LT2Root.m_x = m_xEnd<0?(m_xToTop+m_xEnd):m_xToTop;
			m_LT2Root.m_y = m_yEnd<0?(m_yToTop+m_yEnd):m_yToTop;
			m_needUpdateLT = false;
		}
		return m_LT2Root;
	}
	/**
	 * 获取宽度
	 * 
	 * @return 宽度
	 */
	public float getWidth()
	{
		float w=C2D_Math.abs(m_xEnd);
		return w;
	}
	/**
	 * 获取高度
	 * 
	 * @return 高度
	 */
	protected float getHeight()
	{
		float h=C2D_Math.abs(m_yEnd);
		return h;
	}
	/**
	 * 设置宽度
	 * 
	 * @param width
	 *            新宽度
	 * @return 返回是否发生改变
	 */
	public boolean setWidth(int width)
	{
		if (m_xEnd == width)
		{
			return false;
		}

		m_xEnd = width;
		layoutChanged();
		return true;
	}

	/**
	 * 获取结束点Y坐标
	 * 
	 * @return 结束点Y坐标
	 */
	public float getYEnd()
	{
		return m_yEnd;
	}

	/**
	 * 设置高度
	 * 
	 * @param yEnd
	 *            新高度
	 * @return 返回是否发生改变
	 */
	public boolean setYEnd(int yEnd)
	{
		if (m_yEnd == yEnd)
		{
			return false;
		}

		m_yEnd = yEnd;
		layoutChanged();
		return true;
	}

	/**
	 * 设置结束端点坐标,它是相对于当前位置（起点）的相对坐标。
	 * 
	 * @param xEnd
	 *            新宽度
	 * @param yEnd
	 *            新高度
	 * @return 返回是否发生改变
	 */
	public boolean setPointEnd(float xEnd, float yEnd)
	{
		if (m_xEnd == xEnd && m_yEnd == yEnd)
		{
			return false;
		}

		m_xEnd = xEnd;
		m_yEnd = yEnd;
		layoutChanged();
		return true;
	}

	/**
	 * 绘制节点
	 * 
	 */
	protected void onPaint(C2D_Graphics g)
	{
		C2D_Stage stage = getStageAt();
		if ( !m_visible || stage == null)
		{
			return;
		}
		g.setColor(m_Color);
		g.drawLine(m_xToTop, m_yToTop, m_xEnd, m_yEnd);
		paintFocus(g,m_xEnd, m_yEnd);
	}

	/**
	 * 获取相对布局矩形，即基于当前的坐标，尺寸，锚点，翻转等信息， 计算出相对于其父节点所占据的矩形区域。将信息存放在传入的
	 * 矩形参数，并返回是否成功取得。
	 * 
	 * @param resultRect
	 *            用于结果存放的矩形对象
	 * @return 是否成功取得
	 */
	protected boolean getRectToParent(C2D_RectF resultRect)
	{
		if (resultRect == null)
		{
			return false;
		}
		float xMin=C2D_Math.min(m_x,m_x+m_xEnd);
		float yMin=C2D_Math.min(m_y,m_y+m_yEnd);
		float xMax=C2D_Math.max(m_x,m_x+m_xEnd);
		float yMax=C2D_Math.max(m_y,m_y+m_yEnd);
		return C2D_GdiGraphics.computeLayoutRect(xMin, yMin, xMax-xMin, yMax-yMin, m_anchor, C2D_GdiGraphics.TRANS_NONE, resultRect);
	}

	/**
	 * 设置控件的所有参数
	 * 
	 * @param xEnd
	 *            宽度
	 * @param yEnd
	 *            高度
	 * @param color
	 *            线段颜色
	 */
	public void setParameters(float xEnd, float yEnd, int color)
	{

		m_xEnd = xEnd;
		m_yEnd = yEnd;
		m_Color = color;
		layoutChanged();
	}
}
