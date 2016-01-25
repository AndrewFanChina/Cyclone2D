package c2d.frame.com.shape;

import c2d.frame.base.C2D_Stage;
import c2d.frame.base.C2D_Widget;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_RectF;
import c2d.plat.gfx.C2D_GdiGraphics;
import c2d.plat.gfx.C2D_Graphics;

/**
 * 矩形控件，需要指定显示的颜色，位置和尺寸。
 * 
 * @author AndrewFan
 */
public class C2D_Rectangle extends C2D_Widget
{
	/** 宽度 */
	protected float m_width;
	/** 高度 */
	protected float m_height;
	/** 填充颜色 */
	protected C2D_Color m_bgColor;
	/** 边框颜色 */
	protected C2D_Color m_borderColor;

	/**
	 * 构造函数
	 */
	public C2D_Rectangle()
	{
	}

	/**
	 * 获取填充颜色，你不应该改变获取到的颜色， 如果需要设置，请用setBgColor
	 * 
	 * @return 文本颜色
	 */
	public C2D_Color getBgColor()
	{
		return m_bgColor;
	}

	/**
	 * 设置填充颜色
	 * 
	 * @param color
	 *            颜色对象
	 */
	public void setBgColor(C2D_Color color)
	{

		this.m_bgColor = color;
		layoutChanged();
	}

	/**
	 * 设置填充颜色,ARGB
	 * 
	 * @param color
	 *            ARGB颜色数值
	 */
	public void setBgColor(int color)
	{

		this.m_bgColor = C2D_Color.makeARGB(color);
		layoutChanged();
	}

	/**
	 * 获取边框颜色，你不应该改变获取到的颜色， 如果需要设置，请用setBorderColor
	 * 
	 * @return 边框颜色
	 */
	public C2D_Color getBorderColor()
	{
		return m_borderColor;
	}

	/**
	 * 设置边框颜色
	 * 
	 * @param color
	 *            颜色
	 */
	public void setBorderColor(C2D_Color color)
	{

		m_borderColor = color;
		layoutChanged();
	}

	/**
	 * 设置填充颜色,ARGB
	 * 
	 * @param color
	 *            ARGB颜色数值
	 */
	public void setBorderColor(int color)
	{

		this.m_borderColor = C2D_Color.makeARGB(color);
		layoutChanged();
	}

	/**
	 * 获取宽度
	 * 
	 * @return 宽度
	 */
	public float getWidth()
	{
		return m_width;
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
		if (m_width == width)
		{
			return false;
		}

		m_width = width;

		layoutChanged();
		return true;
	}

	/**
	 * 获取高度
	 * 
	 * @return 高度
	 */
	public float getHeight()
	{
		return m_height;
	}

	/**
	 * 设置高度
	 * 
	 * @param height
	 *            新高度
	 * @return 返回是否发生改变
	 */
	public boolean setHeight(int height)
	{
		if (m_height == height)
		{
			return false;
		}

		m_height = height;

		layoutChanged();
		return true;
	}

	/**
	 * 设置宽度
	 * 
	 * @param width
	 *            新宽度
	 * @param height
	 *            新高度
	 * @return 返回是否发生改变
	 */
	public boolean setSize(float width, float height)
	{
		if (m_width == width && m_height == height)
		{
			return false;
		}

		m_width = width;
		m_height = height;

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
		if (!m_visible || stage == null)
		{
			return;
		}
		if (m_bgColor != null)
		{
			g.fillRect(m_xToTop, m_yToTop, m_width, m_height, m_bgColor.getColor(), m_anchor);
		}
		if (m_borderColor != null)
		{
			g.drawRect(m_xToTop, m_yToTop, m_width, m_height, m_borderColor.getColor(), m_anchor);
		}
		paintFocus(g, m_width, m_height);
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
		return C2D_GdiGraphics.computeLayoutRect(m_x, m_y, m_width, m_height, m_anchor, C2D_GdiGraphics.TRANS_NONE, resultRect);
	}

	/**
	 * 设置控件的所有参数
	 * 
	 * @param width
	 *            宽度
	 * @param height
	 *            高度
	 * @param bgColor
	 *            背景颜色
	 * @param borderColor
	 *            边框颜色
	 * @param anchor
	 *            锚点坐标
	 */
	public void setParameters(int width, int height, int bgColor, int borderColor, int anchor)
	{

		m_width = width;
		m_height = height;
		m_bgColor = C2D_Color.makeARGB(bgColor);
		m_borderColor = C2D_Color.makeARGB(borderColor);
		m_anchor = anchor;

		layoutChanged();
	}

	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		m_bgColor = null;
		m_borderColor = null;
	}
}
