package c2d.frame.com.text;

import c2d.frame.com.list.scroll.C2D_ScrollWidget;
import c2d.frame.event.C2D_EventPool_ChangeStrValue;
import c2d.lang.math.type.C2D_Color;
import c2d.lang.math.type.C2D_RectF;
import c2d.plat.font.C2D_TextFont;
import c2d.plat.gfx.C2D_GdiGraphics;

public abstract class C2D_Paragraph extends C2D_ScrollWidget
{
	/** 显示的文本 */
	protected String m_text;
	/** 页面的约束宽度 */
	protected float m_limitW = 2048;
	/** 页面的约束高度 */
	protected float m_limitH = 2048;
	/** 页面的宽度 */
	protected float m_PageWidth;
	/** 页面的高度 */
	protected float m_PageHeight;
	/** 文本区域背景颜色 */
	protected C2D_Color m_textBgColor;
	/** 事件池-改变内容 */
	protected C2D_EventPool_ChangeStrValue m_Events_value;
	/**
	 * 获取文本
	 * 
	 * @return 文本
	 */
	public String getText()
	{
		return m_text;
	}

	/**
	 * 设置文本
	 * 
	 * @param text
	 */
	public C2D_Paragraph setText(String text)
	{
		if (m_text == text || (m_text != null && m_text.equals(text)))
		{
			return this;
		}

		m_text = text;
		if (m_Events_value != null)
		{
			m_Events_value.onCalled(text);
		}

		refreshCharBuffer();

		return this;
	}

	/**
	 * 返回页面宽度
	 * 
	 * @return 页面的宽度
	 */
	public float getPageWidth()
	{
		return m_PageWidth;
	}

	/**
	 * 返回页面高度
	 * 
	 * @return 页面的宽高度
	 */
	public float getPageHeight()
	{
		return m_PageHeight;
	}

	/**
	 * 获取文本区域背景颜色
	 * 
	 * @return 文本区域背景颜色
	 */
	public C2D_Color getTxtBgColor()
	{
		return m_textBgColor;
	}

	/**
	 * 设置文本区域背景颜色
	 * 
	 * @param color
	 *            文本区域背景颜色
	 */
	public void setTxtBgColor(C2D_Color color)
	{

		m_textBgColor = color;

	}

	/**
	 * 设置页面的约束尺寸
	 * 
	 * @param limitW
	 *            页面约束宽度
	 * @param limitH
	 *            页面约束高度
	 */
	public void setLimitSize(float limitW, float limitH)
	{
		m_limitW = limitW;
		m_limitH = limitH;
		refreshCharBuffer();
	}

	/**
	 * 设置页面的约束尺寸
	 * 
	 * @param limitW
	 *            页面约束宽度
	 */
	public void setLimitWidth(float limitW)
	{
		m_limitW = limitW;
		refreshCharBuffer();
	}

	/**
	 * 设置页面的约束尺寸
	 * 
	 * @param limitH
	 *            页面约束高度
	 */
	public void setLimitHeight(int limitH)
	{
		m_limitH = limitH;
		refreshCharBuffer();
	}

	protected abstract void refreshCharBuffer();

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
		return C2D_GdiGraphics.computeLayoutRect(m_x, m_y, m_PageWidth, m_PageHeight, m_anchor, C2D_GdiGraphics.TRANS_NONE, resultRect);
	}

	/**
	 * 获得全部可以滚动的行数，文本只能进行首行滚动
	 * 
	 * @return 全部全部行数
	 */
	public int getTotalScroll()
	{
		if (m_pageRow >= m_totalRow)
		{
			return 0;
		}
		return m_totalRow - m_pageRow;
	}
	/**
	 * 改变数值事件池
	 * 
	 * @return 事件池
	 */
	public C2D_EventPool_ChangeStrValue Events_ChangeValue()
	{
		if (m_Events_value == null)
		{
			m_Events_value = new C2D_EventPool_ChangeStrValue(this);
		}
		return m_Events_value;
	}
	protected float getWidth()
	{
		return m_PageWidth;
	}
	protected float getHeight()
	{
		return m_PageHeight;
	}
	/**
	 * 卸载资源
	 */
	public void onRelease()
	{
		super.onRelease();
		m_text = null;
		m_textBgColor = null;
	}
}
